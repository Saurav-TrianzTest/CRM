package crm.service;

import crm.entity.Pdf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfServiceTest {

    @Test
    void testPdfServiceInterface() {
        assertNotNull(PdfService.class);
    }

    @Test
    void testFindByNameMethodExists() throws NoSuchMethodException {
        PdfService.class.getMethod("findByName", String.class);
    }

    @Test
    void testSavePdfMethodExists() throws NoSuchMethodException {
        PdfService.class.getMethod("savePdf", Pdf.class);
    }
}
