package crm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PdfServiceTest {

    @Test
    void testInterfaceExists() {
        assertNotNull(PdfService.class);
    }

    @Test
    void testInterfaceMethods() throws NoSuchMethodException {
        assertNotNull(PdfService.class.getMethod("findByName", String.class));
        assertNotNull(PdfService.class.getMethod("savePdf", crm.entity.Pdf.class));
    }
}
