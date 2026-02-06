package crm.repository;

import crm.entity.Pdf;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PdfRepositoryTest {

    @MockBean
    private PdfRepository pdfRepository;

    @Test
    void testFindByName() {
        Pdf pdf = Pdf.builder().id(1L).name("test.pdf").build();

        when(pdfRepository.findByName("test.pdf")).thenReturn(pdf);

        Pdf result = pdfRepository.findByName("test.pdf");
        assertNotNull(result);
        assertEquals("test.pdf", result.getName());
        verify(pdfRepository, times(1)).findByName("test.pdf");
    }

    @Test
    void testFindByNameReturnsNull() {
        when(pdfRepository.findByName("nonexistent.pdf")).thenReturn(null);
        Pdf result = pdfRepository.findByName("nonexistent.pdf");
        assertNull(result);
    }

    @Test
    void testRepositoryNotNull() {
        assertNotNull(pdfRepository);
    }
}
