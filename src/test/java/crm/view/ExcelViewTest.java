package crm.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class ExcelViewTest {

    private ExcelView excelView;

    @BeforeEach
    public void setUp() {
        excelView = new ExcelView();
    }

    @Test
    public void testExcelViewCreation() {
        assertNotNull(excelView);
    }

    @Test
    public void testExcelViewExtendsAbstractXlsView() {
        assertNotNull(excelView);
        assertTrue(excelView instanceof org.springframework.web.servlet.view.document.AbstractXlsView);
    }
}
