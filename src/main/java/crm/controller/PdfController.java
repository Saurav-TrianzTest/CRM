package crm.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
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
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Cloud-ready PDF Controller that uses Amazon S3 for durable storage
 * instead of local file system writes.
 */
@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;
    
    @Value("${aws.s3.bucket.name:${S3_BUCKET_NAME:crm-pdf-storage}}")
    private String s3BucketName;
    
    @Value("${aws.region:${AWS_REGION:us-east-1}}")
    private String awsRegion;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * Generates PDF and stores it in Amazon S3 for durable, scalable storage.
     * 
     * @param fileName The name of the PDF file
     * @param text The content to include in the PDF
     * @return The S3 key (path) where the PDF was stored
     * @throws DocumentException if PDF generation fails
     * @throws IOException if S3 upload fails
     */
    private String generateAndStorePdfToS3(String fileName, String text) throws DocumentException, IOException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }
        
        // Generate PDF in memory
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, outputStream);
        document.open();
        Paragraph paragraph = new Paragraph(text);
        document.add(paragraph);
        document.close();
        
        // Upload to S3
        byte[] pdfBytes = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfBytes);
        
        // Generate unique S3 key to avoid collisions
        String s3Key = "pdfs/" + UUID.randomUUID().toString() + "/" + fileName;
        
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(awsRegion)
                .build();
        
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(pdfBytes.length);
        metadata.setContentType("application/pdf");
        
        PutObjectRequest putRequest = new PutObjectRequest(s3BucketName, s3Key, inputStream, metadata);
        s3Client.putObject(putRequest);
        
        log.info("PDF successfully uploaded to S3: bucket={}, key={}", s3BucketName, s3Key);
        
        return s3Key;
    }

    @GetMapping("/pdf-generator")
    public String pdfGenerator(Model model) {
        model.addAttribute("pdf", new Pdf());
        return "pdf/generator";
    }

    @PostMapping("/pdf-generator")
    public String generatePdf(@Valid Pdf pdf, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/pdf-generator";
        } else {
            try {
                String s3Key = generateAndStorePdfToS3(pdf.getName(), pdf.getContent());
                pdf.setS3Key(s3Key); // Store S3 key in entity for future retrieval
                pdfService.savePdf(pdf);
                model.addAttribute("s3Key", s3Key);
                log.info("PDF generated and stored successfully: {}", s3Key);
            } catch (DocumentException e) {
                log.error("Failed to generate PDF document", e);
                model.addAttribute("error", "Failed to generate PDF document");
                return "pdf/generator";
            } catch (IOException e) {
                log.error("Failed to upload PDF to S3", e);
                model.addAttribute("error", "Failed to upload PDF to cloud storage");
                return "pdf/generator";
            }
            return "pdf/success";
        }
    }

}
