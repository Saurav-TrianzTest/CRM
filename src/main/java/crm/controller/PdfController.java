package crm.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Pdf;
import crm.service.PdfService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.core.sync.RequestBody;

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;
    private S3Client s3Client;

    @Value("${aws.s3.bucket.name:${PDF_STORAGE_BUCKET:pdf-storage-bucket}}")
    private String bucketName;

    @Value("${pdf.storage.enabled:true}")
    private boolean s3StorageEnabled;

    public PdfController(PdfService pdfService, S3Client s3Client) {
        this.pdfService = pdfService;
        this.s3Client = s3Client;
    }

    private byte[] generateSamplePdf(String fileName, String text) throws DocumentException, IOException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();
        Paragraph paragraph = new Paragraph(text);
        document.add(paragraph);
        document.close();

        byte[] pdfBytes = baos.toByteArray();

        // Upload to S3 if enabled
        if (s3StorageEnabled) {
            try {
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key("pdfs/" + fileName)
                    .contentType("application/pdf")
                    .build();
                s3Client.putObject(putObjectRequest, RequestBody.fromBytes(pdfBytes));
                log.info("PDF uploaded to S3: s3://{}/pdfs/{}", bucketName, fileName);
            } catch (Exception e) {
                log.error("Failed to upload PDF to S3: {}", e.getMessage(), e);
            }
        }

        return pdfBytes;
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
                log.error("IO Error generating PDF: {}", e.getMessage(), e);
            } catch (DocumentException e) {
                log.error("Document Error generating PDF: {}", e.getMessage(), e);
            }
            return "pdf/success";
        }
    }

}
