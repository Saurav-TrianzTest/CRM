package crm.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class PdfViewTest {

    private PdfView pdfView;

    @BeforeEach
    public void setUp() {
        pdfView = new PdfView();
    }

    @Test
    public void testPdfViewCreation() {
        assertNotNull(pdfView);
    }

    @Test
    public void testPdfViewExtendsAbstractPdfView() {
        assertTrue(pdfView instanceof AbstractPdfView);
    }

    @Test
    public void testContentTypeIsPdf() {
        assertEquals("application/pdf", pdfView.getContentType());
    }
}
