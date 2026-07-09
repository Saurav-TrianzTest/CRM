package crm.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
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

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Controller for PDF generation.
 * PDF content is stored in Azure Blob Storage instead of the local file system
 * to ensure data durability across container restarts and scaling events.
 */
@Controller
@Slf4j
public class PdfController {

    private PdfService pdfService;

    public PdfController(PdfService pdfService) {
        this.pdfService = pdfService;
    }

    /**
     * Generates a PDF document and uploads it to Azure Blob Storage.
     * Replaces the previous FileOutputStream-based local file write (blocker-2).
     *
     * Environment variables required:
     *   AZURE_STORAGE_CONNECTION_STRING – Azure Storage account connection string
     *   AZURE_BLOB_CONTAINER_NAME       – target blob container name
     *
     * @param fileName name of the PDF blob (without extension if not already present)
     * @param text     content to write into the PDF
     */
    private void generateSamplePdf(String fileName, String text) throws DocumentException, IOException {
        if (!fileName.endsWith(".pdf")) {
            fileName += ".pdf";
        }

        // Generate PDF content in memory – no local file system dependency
        ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, pdfOutputStream);
        document.open();
        Paragraph paragraph = new Paragraph(text);
        document.add(paragraph);
        document.close();

        // Upload the in-memory PDF to Azure Blob Storage
        String connectionString = System.getenv("AZURE_STORAGE_CONNECTION_STRING");
        String containerName   = System.getenv("AZURE_BLOB_CONTAINER_NAME");

        if (connectionString == null || connectionString.isEmpty()) {
            log.error("Environment variable AZURE_STORAGE_CONNECTION_STRING is not set.");
            throw new IOException("Azure Storage connection string is not configured.");
        }
        if (containerName == null || containerName.isEmpty()) {
            log.error("Environment variable AZURE_BLOB_CONTAINER_NAME is not set.");
            throw new IOException("Azure Blob container name is not configured.");
        }

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        containerClient.createIfNotExists();

        BlobClient blobClient = containerClient.getBlobClient(fileName);
        byte[] pdfBytes = pdfOutputStream.toByteArray();

        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType("application/pdf");
        blobClient.upload(new ByteArrayInputStream(pdfBytes), pdfBytes.length, true);
        blobClient.setHttpHeaders(headers);

        log.info("PDF '{}' successfully uploaded to Azure Blob Storage container '{}'.", fileName, containerName);
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
                log.error("IO error while uploading PDF to Azure Blob Storage: {}", e.getMessage());
            } catch (DocumentException e) {
                log.error("Document error while generating PDF: {}", e.getMessage());
            }
            return "pdf/success";
        }
    }

}
