package crm.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PdfViewTest {

    @Test
    void testClassExists() {
        assertNotNull(PdfView.class);
    }

    @Test
    void testClassInstantiation() {
        PdfView pdfView = new PdfView();
        assertNotNull(pdfView);
    }

    @Test
    void testExtendsAbstractPdfView() {
        assertTrue(AbstractPdfView.class.isAssignableFrom(PdfView.class));
    }
}
