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
import java.io.IOException;

@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;

    @Value("${cloud.storage.enabled:true}")
    private boolean cloudStorageEnabled;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    private byte[] generateSamplePdf(String fileName, String text) throws DocumentException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        Paragraph paragraph = new Paragraph(text);
        document.add(paragraph);
        document.close();
        return outputStream.toByteArray();
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
                byte[] pdfData = generateSamplePdf(pdf.getName(), pdf.getContent());
                // Store PDF data in database or cloud storage instead of local file system
                // For now, we'll log the success and save metadata
                log.info("PDF generated successfully: {} bytes", pdfData.length);
                pdfService.savePdf(pdf);
            } catch (DocumentException e) {
                log.error("Failed to generate PDF document: {}", e.getMessage(), e);
            }
            return "pdf/success";
        }
    }

}
