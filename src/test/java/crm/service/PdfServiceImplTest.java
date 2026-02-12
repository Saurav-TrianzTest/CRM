package crm.service;

import crm.entity.Pdf;
import crm.repository.PdfRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PdfServiceImpl Tests")
class PdfServiceImplTest {

    @Mock
    private PdfRepository pdfRepository;

    @InjectMocks
    private PdfServiceImpl pdfService;

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = Pdf.builder()
                .id(1L)
                .name("Test PDF")
                .content("Test content")
                .build();
    }

    @Test
    @DisplayName("Test findByName returns correct PDF")
    void testFindByName() {
        when(pdfRepository.findByName("Test PDF")).thenReturn(pdf);

        Pdf found = pdfService.findByName("Test PDF");

        assertNotNull(found);
        assertEquals("Test PDF", found.getName());
        verify(pdfRepository, times(1)).findByName("Test PDF");
    }

    @Test
    @DisplayName("Test findByName returns null when PDF not found")
    void testFindByNameNotFound() {
        when(pdfRepository.findByName("Nonexistent")).thenReturn(null);

        Pdf found = pdfService.findByName("Nonexistent");

        assertNull(found);
        verify(pdfRepository, times(1)).findByName("Nonexistent");
    }

    @Test
    @DisplayName("Test savePdf saves PDF successfully")
    void testSavePdf() {
        when(pdfRepository.save(pdf)).thenReturn(pdf);

        pdfService.savePdf(pdf);

        verify(pdfRepository, times(1)).save(pdf);
    }

    @Test
    @DisplayName("Test savePdf with null PDF")
    void testSavePdfWithNull() {
        when(pdfRepository.save(null)).thenReturn(null);

        pdfService.savePdf(null);

        verify(pdfRepository, times(1)).save(null);
    }

    @Test
    @DisplayName("Test findByName with empty string")
    void testFindByNameEmptyString() {
        when(pdfRepository.findByName("")).thenReturn(null);

        Pdf found = pdfService.findByName("");

        assertNull(found);
        verify(pdfRepository, times(1)).findByName("");
    }

    @Test
    @DisplayName("Test savePdf with PDF without id")
    void testSavePdfWithoutId() {
        Pdf newPdf = Pdf.builder()
                .name("New PDF")
                .build();

        when(pdfRepository.save(newPdf)).thenReturn(newPdf);

        pdfService.savePdf(newPdf);

        verify(pdfRepository, times(1)).save(newPdf);
    }

    @Test
    @DisplayName("Test savePdf updates existing PDF")
    void testSavePdfUpdate() {
        pdf.setName("Updated PDF");
        when(pdfRepository.save(pdf)).thenReturn(pdf);

        pdfService.savePdf(pdf);

        verify(pdfRepository, times(1)).save(pdf);
        assertEquals("Updated PDF", pdf.getName());
    }

    @Test
    @DisplayName("Test findByName with special characters")
    void testFindByNameWithSpecialCharacters() {
        String specialName = "PDF-2024_v1.0";
        Pdf specialPdf = Pdf.builder().name(specialName).build();
        when(pdfRepository.findByName(specialName)).thenReturn(specialPdf);

        Pdf found = pdfService.findByName(specialName);

        assertNotNull(found);
        assertEquals(specialName, found.getName());
    }

    @Test
    @DisplayName("Test savePdf with PDF containing content")
    void testSavePdfWithContent() {
        Pdf pdfWithContent = Pdf.builder()
                .name("Content PDF")
                .content("Large PDF content data")
                .build();

        when(pdfRepository.save(pdfWithContent)).thenReturn(pdfWithContent);

        pdfService.savePdf(pdfWithContent);

        verify(pdfRepository, times(1)).save(pdfWithContent);
    }

    @Test
    @DisplayName("Test findByName case sensitivity")
    void testFindByNameCaseSensitivity() {
        when(pdfRepository.findByName("TEST PDF")).thenReturn(null);
        when(pdfRepository.findByName("Test PDF")).thenReturn(pdf);

        Pdf foundUpperCase = pdfService.findByName("TEST PDF");
        Pdf foundMixedCase = pdfService.findByName("Test PDF");

        assertNull(foundUpperCase);
        assertNotNull(foundMixedCase);
    }
}
