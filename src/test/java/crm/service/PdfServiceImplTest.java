package crm.service;

import crm.entity.Pdf;
import crm.repository.PdfRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PdfServiceImplTest {

    @Mock
    private PdfRepository pdfRepository;

    @InjectMocks
    private PdfServiceImpl pdfService;

    private Pdf pdf;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        pdf = Pdf.builder()
                .id(1L)
                .name("test.pdf")
                .content("Test content")
                .build();
    }

    @Test
    public void testFindByName() {
        when(pdfRepository.findByName("test.pdf")).thenReturn(pdf);
        Pdf result = pdfService.findByName("test.pdf");
        assertNotNull(result);
        assertEquals("test.pdf", result.getName());
        verify(pdfRepository, times(1)).findByName("test.pdf");
    }

    @Test
    public void testFindByNameNotFound() {
        when(pdfRepository.findByName("nonexistent.pdf")).thenReturn(null);
        Pdf result = pdfService.findByName("nonexistent.pdf");
        assertNull(result);
    }

    @Test
    public void testSavePdf() {
        when(pdfRepository.save(pdf)).thenReturn(pdf);
        pdfService.savePdf(pdf);
        verify(pdfRepository, times(1)).save(pdf);
    }

    @Test
    public void testSavePdfWithNullName() {
        Pdf nullNamePdf = new Pdf();
        nullNamePdf.setName(null);
        when(pdfRepository.save(nullNamePdf)).thenReturn(nullNamePdf);
        pdfService.savePdf(nullNamePdf);
        verify(pdfRepository, times(1)).save(nullNamePdf);
    }

    @Test
    public void testSavePdfWithEmptyContent() {
        pdf.setContent("");
        when(pdfRepository.save(pdf)).thenReturn(pdf);
        pdfService.savePdf(pdf);
        verify(pdfRepository, times(1)).save(pdf);
    }
}
