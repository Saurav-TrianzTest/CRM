package crm.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfViewTest {

    private PdfView pdfView;

    @BeforeEach
    void setUp() {
        pdfView = new PdfView();
    }

    @Test
    void testConstructor() {
        assertNotNull(pdfView);
    }

    @Test
    void testPdfViewInstantiation() {
        PdfView view = new PdfView();
        assertNotNull(view);
    }

    @Test
    void testPdfViewExtendsAbstractPdfView() {
        assertTrue(pdfView instanceof AbstractPdfView);
    }
}
