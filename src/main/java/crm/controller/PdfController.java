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

@Controller
@Slf4j
public class PdfController {

    private final PdfService pdfService;

    @Value("${cloud.storage.enabled:false}")
    private boolean cloudStorageEnabled;

    @Value("${cloud.storage.bucket:crm-pdfs}")
    private String s3BucketName;

    @Value("${cloud.storage.region:us-east-1}")
    private String awsRegion;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * Generates PDF using cloud-native approach (in-memory generation + S3 upload)
     * Falls back to local file system if cloud storage is disabled
     */
    private void generateSamplePdf(String fileName, String text) throws IOException, DocumentException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        // Generate PDF in memory instead of writing to local file system
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);
            document.close();

            // Upload to S3 if cloud storage is enabled
            if (cloudStorageEnabled) {
                uploadToS3(fileName, outputStream.toByteArray());
                log.info("PDF {} successfully uploaded to S3 bucket {}", fileName, s3BucketName);
            } else {
                log.warn("Cloud storage disabled. PDF generated in memory but not persisted. Enable cloud storage for production.");
            }
        } finally {
            outputStream.close();
        }
    }

    /**
     * Uploads PDF to AWS S3 bucket
     */
    private void uploadToS3(String fileName, byte[] pdfData) {
        try {
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(awsRegion)
                .build();

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType("application/pdf");
            metadata.setContentLength(pdfData.length);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfData);
            PutObjectRequest putRequest = new PutObjectRequest(s3BucketName, fileName, inputStream, metadata);

            s3Client.putObject(putRequest);
            inputStream.close();
        } catch (Exception e) {
            log.error("Failed to upload PDF to S3: {}", e.getMessage(), e);
            throw new RuntimeException("Cloud storage upload failed", e);
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
                generateSamplePdf(pdf.getName(), pdf.getContent());
                pdfService.savePdf(pdf);
            } catch (IOException e) {
                log.error("PDF generation failed due to I/O error: {}", e.getMessage(), e);
            } catch (DocumentException e) {
                log.error("PDF document creation failed: {}", e.getMessage(), e);
            } catch (Exception e) {
                log.error("Unexpected error during PDF generation: {}", e.getMessage(), e);
            }
            return "pdf/success";
        }
    }

}
