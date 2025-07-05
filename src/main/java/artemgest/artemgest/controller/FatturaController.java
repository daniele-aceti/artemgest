package artemgest.artemgest.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.model.Fattura;
import artemgest.artemgest.repository.ClienteRepository;

@Controller
public class FatturaController {

    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping("/genera-fattura/{idCliente}")
    public ResponseEntity<byte[]> generaPdfFattura(
            @PathVariable Long idCliente,
            @ModelAttribute("nuovaFattura") Fattura formFattura,
            Model model) throws IOException {

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new IllegalArgumentException("Cliente non trovato con ID: " + idCliente));

        formFattura.setCliente(cliente);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (PDDocument doc = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            PDPageContentStream content = new PDPageContentStream(doc, page);

            // Logo
            PDImageXObject logo = PDImageXObject.createFromFile("src/main/resources/static/img/logo.PNG", doc);
            content.drawImage(logo, 50, 750, 50, 50);

            // Nome e titolo
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
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(400, 740);
            content.showText("N. " + formFattura.getNumeroFattura());
            content.newLineAtOffset(0, -20);
            content.showText("Del " + formFattura.getDataInizioFattura().format(dtf));
            content.endText();

            // Dati cliente sotto logo
            content.beginText();
            content.setFont(PDType1Font.HELVETICA, 12);
            content.newLineAtOffset(50, 700);
            content.showText(cliente.getRagioneSociale());
            content.newLine();
            content.showText(cliente.getIndirizzo());
            content.newLine();
            content.showText("P.I. " + cliente.getpIvaCFiscale());
            content.endText();

            // Totali con spaziatura maggiore
            float yPosition = 650;

            content.beginText();
            content.setFont(PDType1Font.HELVETICA_BOLD, 12);
            content.newLineAtOffset(50, yPosition);
            content.showText("Importo: ");
            content.newLineAtOffset(80, 0);
            content.showText(String.format("€ %.2f", formFattura.getImporto()));
            content.endText();

            yPosition -= 20;
            content.beginText();
            content.newLineAtOffset(50, yPosition);
            content.showText("IVA: ");
            content.newLineAtOffset(80, 0);
            content.showText(String.format("€ %.2f", formFattura.getIva()));
            content.endText();

            yPosition -= 20;
            content.beginText();
            content.newLineAtOffset(50, yPosition);
            content.showText("Totale: ");
            content.newLineAtOffset(80, 0);
            content.showText(String.format("€ %.2f", formFattura.getImporto().add(formFattura.getIva())));
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

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fattura.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(baos.toByteArray());
    }
}
