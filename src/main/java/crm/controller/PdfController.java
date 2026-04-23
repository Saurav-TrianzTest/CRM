package crm.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Pdf;
import crm.service.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@Slf4j
public class PdfController {

    private static final String S3_BUCKET_NAME = System.getenv().getOrDefault("S3_BUCKET_NAME", "crm-pdf-bucket");
    private static final String AWS_REGION = System.getenv().getOrDefault("AWS_REGION", "us-east-1");

    private PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * Generates a PDF and uploads it to Amazon S3 for durable storage.
     * Replaces local file system write operations with cloud-native S3 storage.
     *
     * @param fileName The name of the PDF file
     * @param text The content to include in the PDF
     * @return The S3 key (path) where the PDF was stored, or null if operation failed
     */
    private String generateAndUploadPdfToS3(String fileName, String text) {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        try {
            // Generate PDF in memory instead of writing to local file system
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);
            document.close();

            // Upload to S3
            byte[] pdfBytes = baos.toByteArray();
            String s3Key = "pdfs/" + fileName;

            S3Client s3Client = S3Client.builder()
                    .region(software.amazon.awssdk.regions.Region.of(AWS_REGION))
                    .build();

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(S3_BUCKET_NAME)
                    .key(s3Key)
                    .contentType("application/pdf")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(pdfBytes));

            log.info("Successfully uploaded PDF to S3: s3://{}/{}", S3_BUCKET_NAME, s3Key);
            return s3Key;

        } catch (DocumentException e) {
            log.error("Error generating PDF document: {}", e.getMessage(), e);
            return null;
        } catch (S3Exception e) {
            log.error("S3 error while uploading PDF: {}", e.awsErrorDetails().errorMessage(), e);
            return null;
        } catch (Exception e) {
            log.error("Unexpected error during PDF generation and upload: {}", e.getMessage(), e);
            return null;
        }
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
            String s3Key = generateAndUploadPdfToS3(pdf.getName(), pdf.getContent());
            if (s3Key != null) {
                // Store S3 key reference in the database instead of local file path
                pdf.setName(s3Key);
                pdfService.savePdf(pdf);
                model.addAttribute("s3Key", s3Key);
                model.addAttribute("message", "PDF successfully generated and stored in S3");
                log.info("PDF saved to database with S3 reference: {}", s3Key);
                return "pdf/success";
            } else {
                log.error("Failed to generate and upload PDF for: {}", pdf.getName());
                model.addAttribute("error", "Failed to generate PDF. Please try again.");
                return "pdf/generator";
            }
        }
    }

}
