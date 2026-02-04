package crm.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Pdf;
import crm.service.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;

    @Value("${pdf.storage.path:/tmp/pdfs}")
    private String pdfStoragePath;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    private byte[] generateSamplePdf(String fileName, String text) throws DocumentException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();
        Paragraph paragraph = new Paragraph(text);
        document.add(paragraph);
        document.close();

        byte[] pdfBytes = baos.toByteArray();

        // Write to cloud-compatible storage path with proper error handling
        try {
            Path storagePath = Paths.get(pdfStoragePath);
            if (!Files.exists(storagePath)) {
                Files.createDirectories(storagePath);
            }
            Path filePath = storagePath.resolve(fileName);
            Files.write(filePath, pdfBytes);
            log.info("PDF generated successfully at: {}", filePath);
        } catch (Exception e) {
            log.error("Failed to write PDF to storage: {}", e.getMessage());
        }

        return pdfBytes;
    }

    @GetMapping("/pdf-generator")
    public String pdfGenerator(Model model) {
        model.addAttribute("pdf", new Pdf());
        return "pdf/generator";
    }

    @PostMapping("/pdf-generator")
    public String generatePdf(@Valid Pdf pdf, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/pdf-generator";
        } else {
            try {
                generateSamplePdf(pdf.getName(), pdf.getContent());
                pdfService.savePdf(pdf);
                log.info("PDF generated and saved successfully: {}", pdf.getName());
            } catch (DocumentException e) {
                log.error("Error generating PDF document: {}", e.getMessage(), e);
            } catch (Exception e) {
                log.error("Unexpected error generating PDF: {}", e.getMessage(), e);
            }
            return "pdf/success";
        }
    }

}
