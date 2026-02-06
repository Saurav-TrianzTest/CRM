package crm.service;

import crm.entity.Pdf;
import crm.repository.PdfRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
                .content("Content")
                .build();
    }

    @Test
    void testFindByName() {
        when(pdfRepository.findByName("Test PDF")).thenReturn(pdf);

        Pdf result = pdfService.findByName("Test PDF");

        assertNotNull(result);
        assertEquals("Test PDF", result.getName());
        verify(pdfRepository).findByName("Test PDF");
    }

    @Test
    void testFindByNameNotFound() {
        when(pdfRepository.findByName("Nonexistent")).thenReturn(null);

        Pdf result = pdfService.findByName("Nonexistent");

        assertNull(result);
        verify(pdfRepository).findByName("Nonexistent");
    }

    @Test
    void testSavePdf() {
        when(pdfRepository.save(pdf)).thenReturn(pdf);

        pdfService.savePdf(pdf);

        verify(pdfRepository).save(pdf);
    }

    @Test
    void testSavePdfWithNullName() {
        Pdf nullNamePdf = Pdf.builder().id(2L).build();
        when(pdfRepository.save(nullNamePdf)).thenReturn(nullNamePdf);

        pdfService.savePdf(nullNamePdf);

        verify(pdfRepository).save(nullNamePdf);
    }
}
