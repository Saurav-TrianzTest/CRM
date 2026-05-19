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
 * Cloud-ready PDF Controller that uses Azure Blob Storage for persistent storage.
 * Replaces local file system writes with Azure Blob Storage operations.
 */
@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;

    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Value("${azure.storage.container-name}")
    private String containerName;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * Generates a PDF and stores it in Azure Blob Storage instead of local file system.
     * 
     * @param fileName The name of the PDF file
     * @param text The content to write to the PDF
     * @return The blob name where the PDF was stored
     * @throws DocumentException if PDF generation fails
     */
    private String generateSamplePdfToAzureBlob(String fileName, String text) throws DocumentException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        try {
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
            uploadToAzureBlobStorage(fileName, pdfBytes);

            log.info("PDF successfully generated and uploaded to Azure Blob Storage: {}", fileName);
            return fileName;
        } catch (Exception e) {
            log.error("Failed to generate and upload PDF to Azure Blob Storage", e);
            throw new RuntimeException("Failed to generate PDF to Azure Blob Storage", e);
        }
    }

    /**
     * Uploads a byte array to Azure Blob Storage.
     * 
     * @param blobName The name of the blob
     * @param data The data to upload
     */
    private void uploadToAzureBlobStorage(String blobName, byte[] data) {
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
            
            // Upload the data
            try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data)) {
                blobClient.upload(inputStream, data.length, true);
            }

            log.info("Successfully uploaded blob: {}", blobName);
        } catch (Exception e) {
            log.error("Failed to upload to Azure Blob Storage: {}", blobName, e);
            throw new RuntimeException("Failed to upload to Azure Blob Storage", e);
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
                String blobName = generateSamplePdfToAzureBlob(pdf.getName(), pdf.getContent());
                pdf.setName(blobName); // Store the blob name for reference
                pdfService.savePdf(pdf);
                log.info("PDF generated and saved successfully: {}", blobName);
            } catch (DocumentException e) {
                log.error("Document generation error", e);
                return "redirect:/pdf-generator?error=document";
            } catch (Exception e) {
                log.error("Failed to generate PDF", e);
                return "redirect:/pdf-generator?error=general";
            }
            return "pdf/success";
        }
    }

}
