package crm.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AbstractXlsViewTest {

    private AbstractXlsView xlsView = new AbstractXlsView() {
        @Override
        protected void buildExcelDocument(Map<String, Object> model, Workbook workbook,
                                          HttpServletRequest request, HttpServletResponse response) throws Exception {
            // Test implementation
        }
    };

    @Test
    void testContentType() {
        assertEquals("application/vnd.ms-excel", xlsView.getContentType());
    }

    @Test
    void testGeneratesDownloadContent() {
        assertTrue(xlsView.generatesDownloadContent());
    }

    @Test
    void testConstructor() {
        assertNotNull(xlsView);
    }
}
