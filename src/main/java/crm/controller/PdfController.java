package crm.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
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

/**
 * Cloud-ready PDF Controller that uses Azure Blob Storage for PDF persistence.
 * Replaces local file system writes with Azure Blob Storage operations.
 */
@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;

    @Value("${azure.storage.connection-string:#{null}}")
    private String connectionString;

    @Value("${azure.storage.container-name:crm-files}")
    private String containerName;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * Generates a PDF and stores it in Azure Blob Storage instead of local file system.
     * 
     * @param fileName The name of the PDF file
     * @param text The content to write to the PDF
     * @return The blob URL where the PDF is stored
     */
    private String generateSamplePdfToAzureBlob(String fileName, String text) {
        try {
            if (!fileName.endsWith(".pdf")) {
                fileName += ".pdf";
            }

            // Generate PDF in memory
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);
            document.open();
            Paragraph paragraph = new Paragraph(text);
            document.add(paragraph);
            document.close();

            // Upload to Azure Blob Storage
            byte[] pdfBytes = outputStream.toByteArray();
            return uploadToAzureBlobStorage(fileName, pdfBytes);

        } catch (DocumentException e) {
            log.error("Error generating PDF document", e);
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    /**
     * Uploads a byte array to Azure Blob Storage.
     * 
     * @param blobName The name of the blob
     * @param data The data to upload
     * @return The blob URL
     */
    private String uploadToAzureBlobStorage(String blobName, byte[] data) {
        if (connectionString == null || connectionString.isEmpty()) {
            log.warn("Azure Storage connection string is not configured. PDF will not be persisted.");
            return "local://" + blobName;
        }

        try {
            BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();

            BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
            
            // Create container if it doesn't exist
            if (!containerClient.exists()) {
                containerClient.create();
                log.info("Created Azure Blob Storage container: {}", containerName);
            }

            BlobClient blobClient = containerClient.getBlobClient(blobName);
            
            // Upload the PDF
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data)) {
                blobClient.upload(inputStream, data.length, true);
                log.info("Successfully uploaded PDF to Azure Blob Storage: {}", blobName);
            }

            return blobClient.getBlobUrl();

        } catch (Exception e) {
            log.error("Failed to upload PDF to Azure Blob Storage", e);
            throw new RuntimeException("Failed to upload PDF to Azure Blob Storage", e);
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
            try {
                String blobUrl = generateSamplePdfToAzureBlob(pdf.getName(), pdf.getContent());
                pdf.setName(blobUrl); // Store the blob URL instead of local path
                pdfService.savePdf(pdf);
                model.addAttribute("blobUrl", blobUrl);
                log.info("PDF generated and stored successfully at: {}", blobUrl);
            } catch (Exception e) {
                log.error("Error generating or storing PDF", e);
                model.addAttribute("error", "Failed to generate PDF: " + e.getMessage());
                return "pdf/generator";
            }
            return "pdf/success";
        }
    }
}
