package crm.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExcelViewTest {

    @Test
    void testClassExists() {
        assertNotNull(ExcelView.class);
    }

    @Test
    void testClassInstantiation() {
        ExcelView excelView = new ExcelView();
        assertNotNull(excelView);
    }
}
