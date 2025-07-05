package artemgest.artemgest.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.model.Fattura;
import artemgest.artemgest.service.ClienteService;
import artemgest.artemgest.service.FatturaService;



@Controller
public class FatturaController {

    @Autowired
    private FatturaService fatturaService;

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/genera-fattura/{idCliente}")
    public ResponseEntity<byte[]> generaPdfFattura(
            @PathVariable Long idCliente,
            @ModelAttribute("nuovaFattura") Fattura formFattura,
            Model model) throws IOException {

        // Prendo il cliente
        Optional<Cliente> clienteOpt = clienteService.cliente(idCliente);
        Cliente cliente = clienteOpt.orElseThrow(() -> new IllegalArgumentException("Cliente non trovato con ID: " + idCliente));

        // Associo cliente alla fattura
        formFattura.setCliente(cliente);

        // Creo la fattura completa (imposto date, iva, ecc.)
        Fattura fatturaCompleta = fatturaService.creaNuovaFattura(formFattura, idCliente);

        // Formatter per la data
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Creo PDF
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            PDPageContentStream content = new PDPageContentStream(doc, page);

            // Logo (modifica percorso se serve)
            PDImageXObject logo = PDImageXObject.createFromFile("src/main/resources/static/img/logo.PNG", doc);
            content.drawImage(logo, 50, 750, 50, 50);

            // Titolo
            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 24);
            content.newLineAtOffset(110, 770);
            content.showText("ArtemGest");
            content.endText();

            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 36);
            content.newLineAtOffset(400, 770);
            content.showText("FATTURA");
            content.endText();

            // Numero fattura e data
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(400, 740);
            content.showText("N. " + fatturaCompleta.getNumeroFattura());
            content.newLineAtOffset(0, -20);
            content.showText("Del " + fatturaCompleta.getDataInizioFattura().format(dtf));
            content.endText();

            // Dati cliente
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(50, 700);
            content.showText(cliente.getRagioneSociale());
            content.newLine();
            content.showText(cliente.getIndirizzo());
            content.newLine();
            content.showText("P.I. " + cliente.getpIvaCFiscale());
            content.endText();

            // Totali
            float yPosition = 650;

            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 12);
            content.newLineAtOffset(50, yPosition);
            content.showText("Importo: ");
            content.newLineAtOffset(80, 0);
            content.showText(String.format("€ %.2f", fatturaCompleta.getImporto()));
            content.endText();

            yPosition -= 20;
            content.beginText();
            content.newLineAtOffset(50, yPosition);
            content.showText("IVA: ");
            content.newLineAtOffset(80, 0);
            content.showText(String.format("€ %.2f", fatturaCompleta.getIva()));
            content.endText();

            yPosition -= 20;
            content.beginText();
            content.newLineAtOffset(50, yPosition);
            content.showText("Totale: ");
            content.newLineAtOffset(80, 0);
            content.showText(String.format("€ %.2f", fatturaCompleta.getImporto() + fatturaCompleta.getIva()));
            content.endText();

            // Coordinate bancarie
            content.beginText();
            content.newLineAtOffset(50, 100);
            content.showText("Coordinate bancarie");
            content.newLine();
            content.showText("Nome Banca");
            content.newLine();
            content.showText("IBAN 0000 1111 2222 3333");
            content.endText();

            content.close();
            doc.save(baos);
        }

        // Restituisco il PDF come download
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fattura.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(baos.toByteArray());
    }


    @GetMapping("/fatture")
    public String tutteFatture(Model model) {
        model.addAttribute("listaFatture", fatturaService.tutteFatture());
        return "fatture";
    }

    @GetMapping("/dettaglioFattura/{id}")
    public String dettaglioFattur(@PathVariable Long id, Model model) {
        model.addAttribute("fattura", fatturaService.fattura(id));
        return "dettaglioFattura";
    }
    
    
}
