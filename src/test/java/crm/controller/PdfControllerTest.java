package crm.controller;

import crm.entity.Pdf;
import crm.service.PdfService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PdfControllerTest {

    @Mock
    private PdfService pdfService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private PdfController pdfController;

    private Pdf pdf;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        pdf = Pdf.builder()
                .id(1L)
                .name("test.pdf")
                .content("Test content")
                .build();
    }

    @Test
    public void testPdfGenerator() {
        String result = pdfController.pdfGenerator(model);
        assertEquals("pdf/generator", result);
        verify(model, times(1)).addAttribute(eq("pdf"), any(Pdf.class));
    }

    @Test
    public void testGeneratePdfWithErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);
        String result = pdfController.generatePdf(pdf, bindingResult);
        assertEquals("redirect:/pdf-generator", result);
    }

    @Test
    public void testGeneratePdfSuccess() {
        when(bindingResult.hasErrors()).thenReturn(false);
        String result = pdfController.generatePdf(pdf, bindingResult);
        assertEquals("pdf/success", result);
    }

    @Test
    public void testPdfControllerConstructor() {
        assertNotNull(pdfController);
    }

    @Test
    public void testPdfGeneratorAddsModel() {
        pdfController.pdfGenerator(model);
        verify(model, times(1)).addAttribute(eq("pdf"), any());
    }

    @Test
    public void testGeneratePdfWithValidPdf() {
        when(bindingResult.hasErrors()).thenReturn(false);
        pdf.setName("document");
        String result = pdfController.generatePdf(pdf, bindingResult);
        assertEquals("pdf/success", result);
    }
}
