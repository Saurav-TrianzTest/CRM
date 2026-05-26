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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * PDF Controller with cloud-ready file storage using Amazon EFS.
 * Files are written to a mounted EFS volume for persistent, shared storage across container instances.
 */
@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;
    
    /**
     * EFS mount point configured via environment variable.
     * Default: /mnt/efs for Amazon EFS mounted volume.
     * This path should be configured to point to the EFS mount in the container/EC2 instance.
     */
    @Value("${pdf.storage.path:/mnt/efs/pdfs}")
    private String pdfStoragePath;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * Generates a PDF file and stores it in Amazon EFS for persistent storage.
     * The EFS volume provides POSIX-compliant file system semantics with data persistence
     * across container restarts and shared access from multiple instances.
     * 
     * @param fileName the name of the PDF file
     * @param text the content to write to the PDF
     * @throws IOException if file operations fail
     * @throws DocumentException if PDF generation fails
     */
    private void generateSamplePdf(String fileName, String text) throws IOException, DocumentException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }
        
        // Ensure the EFS storage directory exists
        Path storagePath = Paths.get(pdfStoragePath);
        if (!Files.exists(storagePath)) {
            try {
                Files.createDirectories(storagePath);
                log.info("Created PDF storage directory at: {}", pdfStoragePath);
            } catch (IOException e) {
                log.error("Failed to create PDF storage directory: {}", pdfStoragePath, e);
                throw new IOException("Unable to create storage directory", e);
            }
        }
        
        // Generate PDF to EFS-mounted persistent storage
        Path filePath = storagePath.resolve(fileName);
        Document document = new Document();
        
        try (FileOutputStream fos = new FileOutputStream(filePath.toFile())) {
            PdfWriter.getInstance(document, fos);
            document.open();
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);
            document.close();
            log.info("PDF generated successfully at: {}", filePath);
        } catch (Exception e) {
            log.error("Failed to generate PDF at: {}", filePath, e);
            throw e;
        }
    }
    
    /**
     * Alternative method: Generate PDF in memory and return as byte array.
     * This approach avoids file system dependencies entirely for stateless operations.
     * 
     * @param fileName the name of the PDF file
     * @param text the content to write to the PDF
     * @return byte array containing the PDF content
     * @throws DocumentException if PDF generation fails
     */
    private byte[] generatePdfInMemory(String fileName, String text) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);
            document.close();
            log.info("PDF generated in memory: {}", fileName);
            return baos.toByteArray();
        } finally {
            document.close();
        }
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
                // Generate PDF to EFS persistent storage
                generateSamplePdf(pdf.getName(), pdf.getContent());
                pdfService.savePdf(pdf);
                log.info("PDF saved successfully: {}", pdf.getName());
            } catch (IOException e) {
                log.error("File I/O error during PDF generation", e);
                return "pdf/error";
            } catch (DocumentException e) {
                log.error("Document error during PDF generation", e);
                return "pdf/error";
            }
            return "pdf/success";
        }
    }

}
