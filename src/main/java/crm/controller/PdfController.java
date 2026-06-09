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

/**
 * Controller for PDF generation.
 * PDF content is written directly to Amazon S3 instead of the local file system,
 * ensuring durability and availability in containerised / serverless cloud environments.
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
     * Generates a PDF document and uploads it to Amazon S3.
     * The target bucket is resolved from the AWS_S3_BUCKET_NAME environment variable.
     *
     * @param fileName desired S3 object key / file name
     * @param text     paragraph content to embed in the PDF
     */
    private void generateSamplePdf(String fileName, String text) throws DocumentException, IOException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        // Build the PDF in memory – no local file system dependency
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();
        Paragraph paragraph = new Paragraph(text);
        document.add(paragraph);
        document.close();

        // Upload the in-memory PDF bytes to Amazon S3
        String bucketName = System.getenv("AWS_S3_BUCKET_NAME");
        if (bucketName == null || bucketName.isEmpty()) {
            throw new IllegalStateException(
                    "Environment variable AWS_S3_BUCKET_NAME is not set. "
                    + "Please configure it before uploading PDFs to S3.");
        }

        byte[] pdfBytes = baos.toByteArray();
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key("pdfs/" + fileName)
                .contentType("application/pdf")
                .contentLength((long) pdfBytes.length)
                .build();

        try {
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(pdfBytes));
            log.info("PDF '{}' successfully uploaded to S3 bucket '{}'", fileName, bucketName);
        } catch (S3Exception e) {
            log.error("Failed to upload PDF '{}' to S3: {}", fileName, e.awsErrorDetails().errorMessage());
            throw e;
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
                log.error("IO error while generating PDF: {}", e.getMessage());
            } catch (DocumentException e) {
                log.error("Document error while generating PDF: {}", e.getMessage());
            }
            return "pdf/success";
        }
    }
}
