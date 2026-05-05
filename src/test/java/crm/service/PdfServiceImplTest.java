package crm.service;

import crm.entity.Pdf;
import crm.repository.PdfRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PdfServiceImplTest {

    private PdfServiceImpl pdfService;
    private PdfRepository pdfRepository;

    @BeforeEach
    void setUp() {
        pdfRepository = mock(PdfRepository.class);
        pdfService = new PdfServiceImpl(pdfRepository);
    }

    @Test
    void constructor_shouldCreatePdfServiceImpl() {
        // Assert
        assertNotNull(pdfService);
    }

    @Test
    void findByName_shouldReturnPdf() {
        // Arrange
        Pdf pdf = Pdf.builder()
                .id(1L)
                .name("test.pdf")
                .content("Test content")
                .build();
        
        when(pdfRepository.findByName("test.pdf")).thenReturn(pdf);
        
        // Act
        Pdf result = pdfService.findByName("test.pdf");
        
        // Assert
        assertNotNull(result);
        assertEquals("test.pdf", result.getName());
        verify(pdfRepository).findByName("test.pdf");
    }

    @Test
    void findByName_withNonExistentName_shouldReturnNull() {
        // Arrange
        when(pdfRepository.findByName("nonexistent.pdf")).thenReturn(null);
        
        // Act
        Pdf result = pdfService.findByName("nonexistent.pdf");
        
        // Assert
        assertNull(result);
        verify(pdfRepository).findByName("nonexistent.pdf");
    }

    @Test
    void savePdf_shouldSavePdf() {
        // Arrange
        Pdf pdf = Pdf.builder()
                .name("test.pdf")
                .content("Content")
                .build();
        
        when(pdfRepository.save(pdf)).thenReturn(pdf);
        
        // Act
        pdfService.savePdf(pdf);
        
        // Assert
        verify(pdfRepository).save(pdf);
    }

    @Test
    void service_shouldHaveServiceAnnotation() {
        // Assert
        assertTrue(PdfServiceImpl.class.isAnnotationPresent(
            org.springframework.stereotype.Service.class));
    }
}
