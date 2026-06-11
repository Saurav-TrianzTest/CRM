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
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Controller for PDF generation.
 * PDF files are stored in Amazon S3 instead of the local file system
 * to ensure durability and availability in cloud/containerized environments.
 */
@Controller
@Slf4j
public class PdfController {

    private final PdfService pdfService;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name:#{environment.AWS_S3_BUCKET_NAME}}")
    private String s3BucketName;

    @Value("${aws.s3.pdf-prefix:pdfs/}")
    private String s3PdfPrefix;

    public PdfController(PdfService pdfService, S3Client s3Client) {
        this.pdfService = pdfService;
        this.s3Client = s3Client;
    }

    /**
     * Generates a PDF document and uploads it directly to Amazon S3.
     * Replaces local FileOutputStream write with S3 PutObject to avoid
     * ephemeral file system dependency in cloud environments.
     *
     * @param fileName the name of the PDF file (S3 object key)
     * @param text     the text content to include in the PDF
     * @throws DocumentException if PDF generation fails
     * @throws IOException       if stream operations fail
     */
    private void generateSamplePdf(String fileName, String text) throws DocumentException, IOException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }
        // Write PDF content to an in-memory byte array instead of local file system
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();
        Paragraph paragraph = new Paragraph(text);
        document.add(paragraph);
        document.close();

        // Upload the generated PDF bytes directly to Amazon S3
        String s3Key = s3PdfPrefix + fileName;
        byte[] pdfBytes = baos.toByteArray();
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3BucketName)
                    .key(s3Key)
                    .contentType("application/pdf")
                    .contentLength((long) pdfBytes.length)
                    .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(pdfBytes));
            log.info("PDF uploaded to S3: s3://{}/{}", s3BucketName, s3Key);
        } catch (S3Exception e) {
            log.error("Failed to upload PDF to S3: {}", e.awsErrorDetails().errorMessage());
            throw new IOException("Failed to upload PDF to S3: " + e.awsErrorDetails().errorMessage(), e);
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
                log.error("IO error during PDF generation or S3 upload: {}", e.getMessage());
            } catch (DocumentException e) {
                log.error("Document error during PDF generation: {}", e.getMessage());
            }
            return "pdf/success";
        }
    }

}
