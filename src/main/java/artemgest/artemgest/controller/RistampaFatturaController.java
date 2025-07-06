package artemgest.artemgest.controller;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.model.Fattura;
import artemgest.artemgest.service.ClienteService;
import artemgest.artemgest.service.FatturaService;

@Controller
public class RistampaFatturaController {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private FatturaService fatturaService;

    @PostMapping("/genera-fattura/{idCliente}/{idFattura}")
    public ResponseEntity<byte[]> ristampaPdfFattura(
            @PathVariable Long idCliente,
            @PathVariable Long idFattura,
            Model model) throws IOException {
        Fattura formFattura = fatturaService.fattura(idFattura);
        Optional<Cliente> clienteOpt = clienteService.cliente(idCliente);
        Cliente cliente = clienteOpt.orElseThrow(() -> new IllegalArgumentException("Cliente non trovato con ID: " + idCliente));

        formFattura.setCliente(cliente);
        Fattura fatturaCompleta = fatturaService.creaNuovaFattura(formFattura, idCliente);

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
            content.showText("N. 00" + fatturaCompleta.getId() +"-"+ LocalDate.now().getYear());
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

            // Riga intestazione tabella
            y = 620;
            float x = 50;
            float rowHeight = 20;

            String[] headers = {"Quantità", "Descrizione", "Prezzo unitario", "IVA", "Importo"};
            float[] colWidths = {60, 200, 100, 50, 80};

            content.setStrokingColor(Color.BLACK);
            content.setLineWidth(0.5f);
            content.addRect(x, y, 490, rowHeight);
            content.stroke();

            float nextX = x;
            for (int i = 0; i < headers.length; i++) {
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 12);
                content.newLineAtOffset(nextX + 2, y + 5);
                content.showText(headers[i]);
                content.endText();
                nextX += colWidths[i];
            }

            // Riga dati
            y -= rowHeight;
            content.setFont(PDType1Font.HELVETICA, 12);
            content.addRect(x, y, 490, rowHeight);
            content.stroke();

            nextX = x;
            content.beginText();
            content.newLineAtOffset(nextX + 2, y + 5);
            content.showText("1");
            //content.showText(String.format("%.2f", fatturaCompleta.getQuantita()));
            content.endText();

            nextX += colWidths[0];
            content.beginText();
            content.newLineAtOffset(nextX + 2, y + 5);
            content.showText("descrizone prova");
            //content.showText(fatturaCompleta.getDescrizione());
            content.endText();

            nextX += colWidths[1];
            content.beginText();
            content.newLineAtOffset(nextX + 2, y + 5);
            content.showText("importo prova");
            //content.showText(String.format("%.2f €", fatturaCompleta.getPrezzoUnitario()));
            content.endText();  //TODO IN ATTESA DELLA SEZIONE ORDINI

            nextX += colWidths[2];
            content.beginText();
            content.newLineAtOffset(nextX + 2, y + 5);
            if (fatturaCompleta.getIva() == 0.22) {
                content.showText("22%");
            } else if (fatturaCompleta.getIva() == 0.04) {
                content.showText("4%");
            } else {
                content.showText("ESENTE");
            }
            content.endText();

            nextX += colWidths[3];
            content.beginText();
            content.newLineAtOffset(nextX + 2, y + 5);
            content.showText(String.format("%.2f €", fatturaCompleta.getImporto()));
            content.endText();

            // Totali
            y -= 50;
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(350, y);
            content.showText("Imponibile");
            content.newLineAtOffset(100, 0);
            content.showText(String.format("%.2f €", fatturaCompleta.getImporto()));
            content.endText();

            y -= 15;
            content.beginText();
            content.newLineAtOffset(350, y);
            content.showText("IVA");
            content.newLineAtOffset(100, 0);
            content.showText(String.format("%.2f €", (fatturaCompleta.getImporto() * fatturaCompleta.getIva())));
            content.endText();

            y -= 15;
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 14);
            content.newLineAtOffset(350, y);
            content.showText("Totale");
            content.newLineAtOffset(100, 0);
            if (fatturaCompleta.getIva() != 0.00) {
                content.showText(String.format("%.2f €", (fatturaCompleta.getImporto() * fatturaCompleta.getIva()) + fatturaCompleta.getImporto()));
            } else {
                content.showText(String.valueOf(fatturaCompleta.getImporto()));
            }
            content.endText();

            // Footer bancario
            y = 120;
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 10);
            content.newLineAtOffset(50, y);
            content.showText("Coordinate bancarie");
            content.newLineAtOffset(0, -12);
            content.showText("Daniele Aceti");
            content.newLineAtOffset(0, -12);
            content.showText("Banca di Roma");
            content.newLineAtOffset(0, -12);
            content.showText("Conto 0123 4567 8901");
            content.endText();

            // Footer contatto
            y = 50;
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 9);
            content.newLineAtOffset(50, y);
            content.showText("Tel. 123-456-7890 | Email: prova@artemgest.com");
            content.newLineAtOffset(0, -10);
            content.showText("9-3/4 Platform, Any City");
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
