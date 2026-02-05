package crm.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PdfRepositoryTest {

    @Test
    public void testPdfRepositoryInterfaceExists() {
        assertDoesNotThrow(() -> Class.forName("crm.repository.PdfRepository"));
    }

    @Test
    public void testPdfRepositoryHasFindByNameMethod() throws NoSuchMethodException {
        assertNotNull(PdfRepository.class.getMethod("findByName", String.class));
    }
}
