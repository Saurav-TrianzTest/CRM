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

import javax.validation.Valid;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Controller for PDF generation.
 * PDF content is written to Amazon S3 instead of the local file system,
 * ensuring durability and availability in cloud/containerised environments.
 */
@Controller
@Slf4j
public class PdfController {

    private final PdfService pdfService;
    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name:${AWS_S3_BUCKET_NAME:crm-pdf-storage}}")
    private String bucketName;

    @Value("${aws.s3.pdf-prefix:pdfs/}")
    private String pdfPrefix;

    public PdfController(PdfService pdfService, S3Client s3Client) {
        this.pdfService = pdfService;
        this.s3Client = s3Client;
    }

    /**
     * Generates a PDF document and uploads it directly to Amazon S3.
     * Replaces the previous {@code new FileOutputStream(fileName)} local write
     * with an in-memory {@link ByteArrayOutputStream} that is then streamed to S3.
     *
     * @param fileName the desired object key suffix (without path prefix)
     * @param text     the paragraph text to embed in the PDF
     * @throws DocumentException if iText encounters a document-level error
     * @throws IOException       if the byte-array stream cannot be read
     */
    private void generateSamplePdf(String fileName, String text) throws DocumentException, IOException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        // Write PDF content to an in-memory buffer — no local file system dependency
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, baos);
        document.open();
        Paragraph paragraph = new Paragraph(text);
        document.add(paragraph);
        document.close();

        // Upload the generated PDF to Amazon S3
        String s3Key = pdfPrefix + fileName;
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(s3Key)
                .contentType("application/pdf")
                .build();
        s3Client.putObject(putRequest, RequestBody.fromBytes(baos.toByteArray()));
        log.info("PDF uploaded to S3: s3://{}/{}", bucketName, s3Key);
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
                log.error("PDF document generation error: {}", e.getMessage());
            } catch (IOException e) {
                log.error("PDF upload I/O error: {}", e.getMessage());
            }
            return "pdf/success";
        }
    }

}
