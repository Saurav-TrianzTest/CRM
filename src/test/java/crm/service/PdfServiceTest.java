package crm.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PdfServiceTest {

    @Test
    public void testPdfServiceInterfaceExists() {
        assertDoesNotThrow(() -> {
            Class.forName("crm.service.PdfService");
        });
    }

    @Test
    public void testPdfServiceHasFindByNameMethod() throws NoSuchMethodException {
        assertNotNull(PdfService.class.getMethod("findByName", String.class));
    }

    @Test
    public void testPdfServiceHasSavePdfMethod() throws NoSuchMethodException {
        assertNotNull(PdfService.class.getMethod("savePdf", crm.entity.Pdf.class));
    }
}
