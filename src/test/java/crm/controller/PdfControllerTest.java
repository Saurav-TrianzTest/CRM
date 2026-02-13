package crm.controller;

import com.itextpdf.text.DocumentException;
import crm.entity.Pdf;
import crm.service.PdfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.io.FileNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PdfControllerTest {

    @Mock
    private PdfService pdfService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private PdfController pdfController;

    private MockMvc mockMvc;

    private Pdf testPdf;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(pdfController).build();

        testPdf = new Pdf();
        testPdf.setId(1L);
        testPdf.setName("test-document");
        testPdf.setContent("This is a test PDF content.");
    }

    @Test
    void testConstructor() {
        PdfController controller = new PdfController(pdfService);
        assert controller != null;
    }

    @Test
    void testPdfGenerator_ShowForm() throws Exception {
        mockMvc.perform(get("/pdf-generator"))
                .andExpect(status().isOk())
                .andExpect(view().name("pdf/generator"))
                .andExpect(model().attributeExists("pdf"));
    }

    @Test
    void testPdfGenerator_ShowFormMultipleTimes() throws Exception {
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get("/pdf-generator"))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pdf/generator"))
                    .andExpect(model().attributeExists("pdf"));
        }
    }

    @Test
    void testGeneratePdf_Success() {
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_ValidationErrors() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("redirect:/pdf-generator");
        verify(pdfService, never()).savePdf(any(Pdf.class));
    }

    @Test
    void testGeneratePdf_WithPdfExtension() {
        testPdf.setName("document.pdf");

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_WithoutPdfExtension() {
        testPdf.setName("document");

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_EmptyContent() {
        testPdf.setContent("");

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_LongContent() {
        testPdf.setContent("Lorem ipsum ".repeat(1000));

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_SpecialCharactersInContent() {
        testPdf.setContent("Special characters: @#$%^&*()_+-={}[]|\\:\";<>?,./");

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_UnicodeContent() {
        testPdf.setContent("Unicode content: 你好世界 مرحبا العالم Привет мир");

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_MultilineContent() {
        testPdf.setContent("Line 1\nLine 2\nLine 3\nLine 4");

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_FileNameWithSpaces() {
        testPdf.setName("test document with spaces");

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_FileNameWithSpecialCharacters() {
        testPdf.setName("test@document#2024");

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_MultiplePdfExtensions() {
        testPdf.setName("document.pdf.pdf");

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_EmptyFileName() {
        testPdf.setName("");

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_NullFileName() {
        testPdf.setName(null);

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_NullContent() {
        testPdf.setContent(null);

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_VeryLongFileName() {
        testPdf.setName("a".repeat(255));

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_FileNameWithPathSeparators() {
        testPdf.setName("folder/subfolder/document");

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_MultipleGenerations() {
        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        for (int i = 1; i <= 5; i++) {
            Pdf pdf = new Pdf();
            pdf.setName("document" + i);
            pdf.setContent("Content " + i);

            String result = pdfController.generatePdf(pdf, bindingResult);

            assert result.equals("pdf/success");
        }

        verify(pdfService, times(5)).savePdf(any(Pdf.class));
    }

    @Test
    void testGeneratePdf_WithValidationErrorOnName() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("redirect:/pdf-generator");
        verify(pdfService, never()).savePdf(any(Pdf.class));
    }

    @Test
    void testGeneratePdf_WithValidationErrorOnContent() {
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("redirect:/pdf-generator");
        verify(pdfService, never()).savePdf(any(Pdf.class));
    }

    @Test
    void testGeneratePdf_ContentWithHtmlTags() {
        testPdf.setContent("<html><body><h1>Title</h1><p>Paragraph</p></body></html>");

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_ContentWithTabs() {
        testPdf.setContent("Column1\tColumn2\tColumn3\nValue1\tValue2\tValue3");

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }

    @Test
    void testGeneratePdf_UppercasePdfExtension() {
        testPdf.setName("document.PDF");

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(pdfService).savePdf(any(Pdf.class));

        String result = pdfController.generatePdf(testPdf, bindingResult);

        assert result.equals("pdf/success");
        verify(pdfService, times(1)).savePdf(testPdf);
    }
}
