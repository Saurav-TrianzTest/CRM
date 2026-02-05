package crm.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CsvViewTest {

    private CsvView csvView;

    @BeforeEach
    public void setUp() {
        csvView = new CsvView();
    }

    @Test
    public void testCsvViewCreation() {
        assertNotNull(csvView);
    }

    @Test
    public void testCsvViewExtendsAbstractCsvView() {
        assertTrue(csvView instanceof AbstractCsvView);
    }

    @Test
    public void testContentTypeIsCsv() {
        assertEquals("text/csv", csvView.getContentType());
    }
}
