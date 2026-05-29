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

@Controller
@Slf4j
public class PdfController {

    private final PdfService pdfService;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket.name:crm-pdf-bucket}")
    private String s3BucketName;

    public PdfController(PdfService pdfService, S3Client s3Client) {
        this.pdfService = pdfService;
        this.s3Client = s3Client;
    }

    /**
     * Generates a PDF and stores it in Amazon S3 instead of local file system.
     * This ensures data durability and availability in cloud environments.
     */
    private void generateSamplePdfToS3(String fileName, String text) throws DocumentException, IOException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        // Generate PDF in memory
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);
            document.close();

            // Upload to S3
            byte[] pdfBytes = outputStream.toByteArray();
            String s3Key = "pdfs/" + fileName;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3BucketName)
                    .key(s3Key)
                    .contentType("application/pdf")
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(pdfBytes));
            log.info("Successfully uploaded PDF to S3: s3://{}/{}", s3BucketName, s3Key);

        } catch (S3Exception e) {
            log.error("Failed to upload PDF to S3: {}", e.getMessage());
            throw new IOException("Failed to upload PDF to S3", e);
        } finally {
            outputStream.close();
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
                generateSamplePdfToS3(pdf.getName(), pdf.getContent());
                pdfService.savePdf(pdf);
            } catch (DocumentException e) {
                log.error("Document generation error: {}", e.getMessage());
            } catch (IOException e) {
                log.error("S3 upload error: {}", e.getMessage());
            }
            return "pdf/success";
        }
    }

}
