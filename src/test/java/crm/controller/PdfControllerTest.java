package crm.controller;

import crm.entity.Pdf;
import crm.service.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PdfControllerTest {

    private PdfController controller;

    @Mock
    private PdfService pdfService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new PdfController(pdfService);
    }

    @Test
    void testPdfGenerator() {
        String result = controller.pdfGenerator(model);

        assertEquals("pdf/generator", result);
        verify(model).addAttribute(eq("pdf"), any(Pdf.class));
    }

    @Test
    void testGeneratePdfSuccess() {
        Pdf pdf = new Pdf();
        pdf.setName("test");
        pdf.setContent("content");

        when(bindingResult.hasErrors()).thenReturn(false);

        String result = controller.generatePdf(pdf, bindingResult);

        assertEquals("pdf/success", result);
        verify(pdfService).savePdf(pdf);
    }

    @Test
    void testGeneratePdfWithErrors() {
        Pdf pdf = new Pdf();

        when(bindingResult.hasErrors()).thenReturn(true);

        String result = controller.generatePdf(pdf, bindingResult);

        assertEquals("redirect:/pdf-generator", result);
        verify(pdfService, never()).savePdf(pdf);
    }

    @Test
    void testConstructor() {
        assertNotNull(controller);
    }
}
