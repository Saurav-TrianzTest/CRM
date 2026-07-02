package crm.controller;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
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
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;

    @Value("${gcs.bucket.name:default-crm-bucket}")
    private String bucketName;

    @Value("${gcs.project.id:#{null}}")
    private String projectId;

    @Value("${gcs.pdf.folder:pdfs/}")
    private String pdfFolder;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * Generates PDF and uploads to Google Cloud Storage instead of local filesystem.
     * This ensures data persistence across container restarts and scaling events.
     */
    private void generateSamplePdf(String fileName, String text) throws FileNotFoundException, DocumentException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }
        
        try {
            // Create PDF in memory
            Document document = new Document();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter.getInstance(document, baos);
            document.open();
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);
            document.close();

            // Upload to Google Cloud Storage
            uploadToGCS(fileName, baos.toByteArray());
            
            log.info("PDF successfully uploaded to GCS: {}/{}{}", bucketName, pdfFolder, fileName);
        } catch (Exception e) {
            log.error("Error generating and uploading PDF to GCS", e);
            throw new RuntimeException("Failed to generate PDF to cloud storage", e);
        }
    }

    /**
     * Uploads byte array content to Google Cloud Storage.
     */
    private void uploadToGCS(String fileName, byte[] content) {
        try {
            Storage storage = getStorageClient();
            
            String blobName = pdfFolder + fileName;
            BlobId blobId = BlobId.of(bucketName, blobName);
            BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                    .setContentType("application/pdf")
                    .build();
            
            storage.create(blobInfo, content);
        } catch (Exception e) {
            log.error("Error uploading to GCS: " + fileName, e);
            throw new RuntimeException("Failed to upload to GCS", e);
        }
    }

    private Storage getStorageClient() {
        if (projectId != null && !projectId.isEmpty()) {
            return StorageOptions.newBuilder()
                    .setProjectId(projectId)
                    .build()
                    .getService();
        } else {
            return StorageOptions.getDefaultInstance().getService();
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
            } catch (FileNotFoundException e) {
                log.info("File Not Found");
            } catch (DocumentException e) {
                log.info("Document");
            }
            return "pdf/success";
        }
    }

}
