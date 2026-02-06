package crm.controller;

import crm.service.PdfService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PdfControllerTest {

    @Mock
    private PdfService pdfService;

    @InjectMocks
    private PdfController controller;

    @Test
    void testControllerClassExists() {
        assertNotNull(PdfController.class);
    }

    @Test
    void testControllerInstantiation() {
        assertNotNull(controller);
    }
}
