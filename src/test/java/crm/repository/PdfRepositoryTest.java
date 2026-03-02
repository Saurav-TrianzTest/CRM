package crm.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfRepositoryTest {

    @Test
    void testPdfRepositoryInterface() {
        assertNotNull(PdfRepository.class);
    }

    @Test
    void testFindByNameMethodExists() throws NoSuchMethodException {
        PdfRepository.class.getMethod("findByName", String.class);
    }
}
