package crm.service;

import crm.entity.Pdf;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfServiceTest {

    @Test
    void pdfService_shouldBeInterface() {
        // Assert
        assertTrue(PdfService.class.isInterface());
    }

    @Test
    void findByName_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            PdfService.class.getMethod("findByName", String.class);
        });
    }

    @Test
    void savePdf_methodShouldExist() {
        // Verify the method exists
        assertDoesNotThrow(() -> {
            PdfService.class.getMethod("savePdf", Pdf.class);
        });
    }

    @Test
    void findByName_shouldReturnPdf() throws NoSuchMethodException {
        // Arrange
        var method = PdfService.class.getMethod("findByName", String.class);
        
        // Assert
        assertEquals(Pdf.class, method.getReturnType());
    }
}
