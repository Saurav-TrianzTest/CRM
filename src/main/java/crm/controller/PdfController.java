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

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;

    @Value("${aws.s3.bucket:default-pdf-bucket}")
    private String s3BucketName;

    @Value("${aws.s3.enabled:false}")
    private boolean s3Enabled;

    private S3Client s3Client;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    @PostConstruct
    public void init() {
        if (s3Enabled) {
            s3Client = S3Client.builder().build();
        }
    }

    private void generateSamplePdf(String fileName, String text) throws IOException, DocumentException {
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

        if (s3Enabled && s3Client != null) {
            uploadToS3(fileName, outputStream.toByteArray());
            log.info("PDF uploaded to S3: {}", fileName);
        } else {
            log.warn("S3 not enabled. PDF generated in-memory only: {}", fileName);
        }
    }

    private void uploadToS3(String fileName, byte[] content) {
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(s3BucketName)
                .key("pdfs/" + fileName)
                .build();
            s3Client.putObject(putObjectRequest, RequestBody.fromBytes(content));
        } catch (Exception e) {
            log.error("Failed to upload PDF to S3: {}", fileName, e);
            throw new RuntimeException("S3 upload failed", e);
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
                log.error("IO Error generating PDF", e);
            } catch (DocumentException e) {
                log.error("Document Error generating PDF", e);
            }
            return "pdf/success";
        }
    }

}
