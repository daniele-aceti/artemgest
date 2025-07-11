package artemgest.artemgest.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.model.DettaglioOrdine;
import artemgest.artemgest.model.Fattura;
import artemgest.artemgest.model.Prodotto;

@Service
public class PdfFatturaService {

    private final FatturaService fatturaService;
    private final ClienteService clienteService;
    private final OrdineService ordineService;

    @Autowired
    public PdfFatturaService(FatturaService fatturaService, ClienteService clienteService, OrdineService ordineService) {
        this.fatturaService = fatturaService;
        this.clienteService = clienteService;
        this.ordineService = ordineService;
    }

    public ResponseEntity<byte[]> generaPdfFattura(Long idOrdine, Long idCliente, Fattura fattura, boolean nuovaFattura) throws IOException {
        Optional<Cliente> clienteOpt = clienteService.cliente(idCliente);
        List<DettaglioOrdine> dettaglioOrdine = ordineService.listaProdottoFattura(idOrdine);
        Cliente cliente = clienteOpt.orElseThrow(() -> new IllegalArgumentException("Cliente non trovato con ID: " + idCliente));

        Fattura fatturaCompleta = nuovaFattura ? fatturaService.creaNuovaFattura(fattura, idCliente) : fattura;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            PDPageContentStream content = new PDPageContentStream(doc, page);

            // Logo
            PDImageXObject logo = PDImageXObject.createFromFile("src/main/resources/static/img/logo.PNG", doc);
            content.drawImage(logo, 50, 750, 50, 50);

            // Intestazione
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 24);
            content.newLineAtOffset(120, 770);
            content.showText("ArtemGest");
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 36);
            content.newLineAtOffset(400, 770);
            content.showText("FATTURA");
            content.endText();

            // Numero e data
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(400, 740);
            content.showText("N. 00" + fatturaCompleta.getId() + "-" + LocalDate.now().getYear());
            content.newLineAtOffset(0, -15);
            content.showText("Del " + fatturaCompleta.getDataInizioFattura().format(dtf));
            content.endText();

            // Dati cliente
            float y = 700;
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(50, y);
            content.showText(cliente.getRagioneSociale());
            content.newLineAtOffset(0, -15);
            content.showText(cliente.getIndirizzo());
            content.newLineAtOffset(0, -15);
            content.showText("P.I. " + cliente.getpIvaCFiscale());
            content.endText();

            // Tabella prodotti
            y = 620;
            float x = 50;
            float rowHeight = 20;

            String[] headers = {"Quantità", "Descrizione", "Prezzo unitario", "IVA", "Importo"};
            float[] colWidths = {60, 200, 100, 50, 80};

            // Header
            content.setNonStrokingColor(Color.LIGHT_GRAY);
            content.addRect(x, y, 490, rowHeight);
            content.fill();
            content.setNonStrokingColor(Color.BLACK);

            float nextX = x;
            for (int i = 0; i < headers.length; i++) {
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 12);
                content.newLineAtOffset(nextX + 2, y + 5);
                content.showText(headers[i]);
                content.endText();
                nextX += colWidths[i];
            }

            y -= rowHeight;
            BigDecimal imponibileTotale = BigDecimal.ZERO;
            for (DettaglioOrdine dettaglio : dettaglioOrdine) {
                Prodotto p = dettaglio.getProdotto();
                int quantita = dettaglio.getQuantita();
                BigDecimal prezzo = BigDecimal.valueOf(p.getPrezzo());
                BigDecimal totaleRiga = prezzo.multiply(BigDecimal.valueOf(quantita));
                imponibileTotale = imponibileTotale.add(totaleRiga);

                nextX = x;
                String[] valori = {
                        String.valueOf(quantita),
                        p.getNome(),
                        String.format("%.2f €", prezzo),
                        fatturaCompleta.getIva().multiply(BigDecimal.valueOf(100)).intValue() + "%",
                        String.format("%.2f €", totaleRiga)
                };

                for (int i = 0; i < valori.length; i++) {
                    content.beginText();
                    content.setFont(PDType1Font.HELVETICA, 11);
                    content.newLineAtOffset(nextX + 2, y + 5);
                    content.showText(valori[i]);
                    content.endText();
                    nextX += colWidths[i];
                }
                y -= rowHeight;
            }

            BigDecimal iva = imponibileTotale.multiply(fatturaCompleta.getIva()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal totale = imponibileTotale.add(iva);

            // Totali
            y -= 20;
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(350, y);
            content.showText("Imponibile");
            content.newLineAtOffset(100, 0);
            content.showText(String.format("%.2f €", imponibileTotale));
            content.endText();

            y -= 15;
            content.beginText();
            content.newLineAtOffset(350, y);
            content.showText("IVA");
            content.newLineAtOffset(100, 0);
            content.showText(String.format("%.2f €", iva));
            content.endText();

            y -= 15;
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 14);
            content.newLineAtOffset(350, y);
            content.showText("Totale");
            content.newLineAtOffset(100, 0);
            content.showText(String.format("%.2f €", totale));
            content.endText();

            // Footer
            y = 100;
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 10);
            content.newLineAtOffset(50, y);
            content.showText("Coordinate bancarie: Daniele Aceti, Banca di Roma, Conto 0123 4567 8901");
            content.newLineAtOffset(0, -12);
            content.showText("Contatti: Tel. 123-456-7890 | Email: prova@artemgest.com");
            content.endText();

            content.close();
            doc.save(baos);
        }

        byte[] pdfBytes = baos.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "fattura_" + idCliente + ".pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }
}
