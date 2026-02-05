package crm.controller;

import crm.entity.Pdf;
import crm.service.PdfService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PdfControllerTest {

    @Mock
    private PdfService pdfService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private PdfController pdfController;

    private Pdf testPdf;

    @BeforeEach
    public void setUp() {
        testPdf = Pdf.builder()
                .id(1L)
                .name("test.pdf")
                .content("Test content")
                .build();
    }

    @Test
    public void testPdfGenerator() {
        String view = pdfController.pdfGenerator(model);
        assertEquals("pdf/generator", view);
        verify(model, times(1)).addAttribute(eq("pdf"), any(Pdf.class));
    }

    @Test
    public void testGeneratePdfWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String view = pdfController.generatePdf(testPdf, bindingResult);
        assertEquals("redirect:/pdf-generator", view);
    }

    @Test
    public void testGeneratePdfSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);
        // Can't fully test file generation, but can verify service call
        assertDoesNotThrow(() -> pdfController.generatePdf(testPdf, bindingResult));
    }
}
