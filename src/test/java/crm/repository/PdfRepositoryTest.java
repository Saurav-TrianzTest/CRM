package crm.repository;

import crm.entity.Pdf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PdfRepositoryTest {

    @Mock
    private PdfRepository pdfRepository;

    @Test
    void testFindAll() {
        Pdf pdf1 = Pdf.builder().id(1L).name("Report 1").build();
        Pdf pdf2 = Pdf.builder().id(2L).name("Report 2").build();

        List<Pdf> pdfs = Arrays.asList(pdf1, pdf2);
        when(pdfRepository.findAll()).thenReturn(pdfs);

        List<Pdf> result = pdfRepository.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(pdfRepository).findAll();
    }

    @Test
    void testSave() {
        Pdf pdf = Pdf.builder().id(1L).name("Invoice").build();

        when(pdfRepository.save(pdf)).thenReturn(pdf);

        Pdf result = pdfRepository.save(pdf);

        assertNotNull(result);
        assertEquals("Invoice", result.getName());
        verify(pdfRepository).save(pdf);
    }

    @Test
    void testFindById() {
        Pdf pdf = Pdf.builder().id(1L).name("Report").build();

        when(pdfRepository.findById(1L)).thenReturn(Optional.of(pdf));

        Optional<Pdf> result = pdfRepository.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Report", result.get().getName());
        verify(pdfRepository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(pdfRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Pdf> result = pdfRepository.findById(999L);

        assertFalse(result.isPresent());
        verify(pdfRepository).findById(999L);
    }

    @Test
    void testDeleteById() {
        doNothing().when(pdfRepository).deleteById(1L);

        pdfRepository.deleteById(1L);

        verify(pdfRepository).deleteById(1L);
    }

    @Test
    void testCount() {
        when(pdfRepository.count()).thenReturn(15L);

        long count = pdfRepository.count();

        assertEquals(15L, count);
        verify(pdfRepository).count();
    }

    @Test
    void testExistsById() {
        when(pdfRepository.existsById(1L)).thenReturn(true);

        boolean exists = pdfRepository.existsById(1L);

        assertTrue(exists);
        verify(pdfRepository).existsById(1L);
    }

    @Test
    void testDelete() {
        Pdf pdf = Pdf.builder().id(1L).name("Report").build();

        doNothing().when(pdfRepository).delete(pdf);

        pdfRepository.delete(pdf);

        verify(pdfRepository).delete(pdf);
    }
}
