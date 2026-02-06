package crm.service;

import crm.entity.Pdf;
import crm.repository.PdfRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PdfServiceImplTest {

    @Mock
    private PdfRepository pdfRepository;

    @InjectMocks
    private PdfServiceImpl pdfServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByName() {
        Pdf pdf = Pdf.builder().id(1L).name("test.pdf").build();

        when(pdfRepository.findByName("test.pdf")).thenReturn(pdf);

        Pdf result = pdfServiceImpl.findByName("test.pdf");
        assertNotNull(result);
        assertEquals("test.pdf", result.getName());
        verify(pdfRepository, times(1)).findByName("test.pdf");
    }

    @Test
    void testSavePdf() {
        Pdf pdf = Pdf.builder().id(1L).name("test.pdf").content("Test content").build();

        when(pdfRepository.save(pdf)).thenReturn(pdf);

        pdfServiceImpl.savePdf(pdf);
        verify(pdfRepository, times(1)).save(pdf);
    }

    @Test
    void testFindByNameReturnsNull() {
        when(pdfRepository.findByName("nonexistent.pdf")).thenReturn(null);
        Pdf result = pdfServiceImpl.findByName("nonexistent.pdf");
        assertNull(result);
    }

    @Test
    void testConstructor() {
        assertNotNull(pdfServiceImpl);
    }
}
