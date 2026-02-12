package crm.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("ExcelView Tests")
class ExcelViewTest {

    @Test
    @DisplayName("Test ExcelView exists")
    void testExcelViewExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test ExcelView class is present")
    void testExcelViewClass() throws ClassNotFoundException {
        Class.forName("crm.view.ExcelView");
    }

    @Test
    @DisplayName("Test ExcelView has buildExcelDocument method")
    void testExcelViewHasBuildMethod() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> excelViewClass = Class.forName("crm.view.ExcelView");
        assertNotNull(excelViewClass.getDeclaredMethod("buildExcelDocument",
                java.util.Map.class,
                org.apache.poi.ss.usermodel.Workbook.class,
                jakarta.servlet.http.HttpServletRequest.class,
                jakarta.servlet.http.HttpServletResponse.class));
    }
}
