package crm.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CsvViewTest {

    private CsvView csvView;

    @BeforeEach
    void setUp() {
        csvView = new CsvView();
    }

    @Test
    void testConstructor() {
        assertNotNull(csvView);
    }

    @Test
    void testCsvViewInstantiation() {
        CsvView view = new CsvView();
        assertNotNull(view);
    }

    @Test
    void testCsvViewExtendsAbstractCsvView() {
        assertTrue(csvView instanceof AbstractCsvView);
    }
}
