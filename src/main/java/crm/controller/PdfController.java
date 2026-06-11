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

/**
 * Controller for PDF generation.
 * Local FileOutputStream writes have been replaced with Amazon S3 uploads
 * using AWS SDK for Java v2 to ensure durable, cloud-native storage.
 * The target S3 bucket is configured via the S3_BUCKET_NAME environment variable.
 */
@Controller
@Slf4j
public class PdfController {

    private final PdfService pdfService;
    private final S3Client s3Client;

    public PdfController(PdfService pdfService, S3Client s3Client) {
        this.pdfService = pdfService;
        this.s3Client = s3Client;
    }

    /**
     * Generates a PDF in memory and uploads it directly to Amazon S3.
     * No local file system writes are performed; the PDF bytes are streamed
     * into a ByteArrayOutputStream and then sent to S3 via PutObjectRequest.
     *
     * @param fileName the S3 object key (file name) for the PDF
     * @param text     the text content to embed in the PDF
     * @throws DocumentException if iText PDF generation fails
     */
    private void generateSamplePdf(String fileName, String text) throws DocumentException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        String bucketName = System.getenv("S3_BUCKET_NAME");
        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalStateException(
                    "Environment variable S3_BUCKET_NAME is not set. " +
                    "Please configure it before running the application.");
        }

        // Generate PDF into an in-memory byte array — no local file system dependency
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, pdfOutputStream);
        document.open();
        Paragraph paragraph = new Paragraph(text);
        document.add(paragraph);
        document.close();

        byte[] pdfBytes = pdfOutputStream.toByteArray();

        // Upload the generated PDF bytes directly to Amazon S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key("pdfs/" + fileName)
                .contentType("application/pdf")
                .contentLength((long) pdfBytes.length)
                .build();

        s3Client.putObject(putObjectRequest, RequestBody.fromBytes(pdfBytes));
        log.info("PDF uploaded to S3: s3://{}/pdfs/{}", bucketName, fileName);
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
            } catch (DocumentException e) {
                log.error("PDF document generation failed: {}", e.getMessage());
            } catch (S3Exception e) {
                log.error("Failed to upload PDF to S3: {}", e.awsErrorDetails().errorMessage());
            }
            return "pdf/success";
        }
    }

}
