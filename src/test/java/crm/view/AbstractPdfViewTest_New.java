package crm.view;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AbstractPdfViewTest_New {

    @Test
    void testAbstractPdfViewInstantiation() {
        AbstractPdfView abstractPdfView = new AbstractPdfView() {
            @Override
            protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
            }
        };
        assertNotNull(abstractPdfView);
    }

    @Test
    void testGeneratesDownloadContent() {
        AbstractPdfView abstractPdfView = new AbstractPdfView() {
            @Override
            protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
            }
        };
        assertTrue(abstractPdfView.generatesDownloadContent());
    }

    @Test
    void testGetViewerPreferences() {
        AbstractPdfView abstractPdfView = new AbstractPdfView() {
            @Override
            protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                                            HttpServletRequest request, HttpServletResponse response) throws Exception {
            }
        };
        int preferences = abstractPdfView.getViewerPreferences();
        assertTrue(preferences > 0);
    }
}
