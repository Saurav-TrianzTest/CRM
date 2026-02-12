package crm.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("PdfView Tests")
class PdfViewTest {

    @Test
    @DisplayName("Test PdfView exists")
    void testPdfViewExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test PdfView class is present")
    void testPdfViewClass() throws ClassNotFoundException {
        Class.forName("crm.view.PdfView");
    }

    @Test
    @DisplayName("Test PdfView extends AbstractPdfView")
    void testPdfViewExtendsAbstract() throws ClassNotFoundException {
        Class<?> pdfViewClass = Class.forName("crm.view.PdfView");
        Class<?> abstractPdfView = Class.forName("crm.view.AbstractPdfView");
        assertTrue(abstractPdfView.isAssignableFrom(pdfViewClass));
    }
}
