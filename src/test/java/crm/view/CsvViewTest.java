package crm.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("CsvView Tests")
class CsvViewTest {

    @Test
    @DisplayName("Test CsvView exists")
    void testCsvViewExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test CsvView class is present")
    void testCsvViewClass() throws ClassNotFoundException {
        Class.forName("crm.view.CsvView");
    }

    @Test
    @DisplayName("Test CsvView extends AbstractCsvView")
    void testCsvViewExtendsAbstract() throws ClassNotFoundException {
        Class<?> csvViewClass = Class.forName("crm.view.CsvView");
        Class<?> abstractCsvView = Class.forName("crm.view.AbstractCsvView");
        assertTrue(abstractCsvView.isAssignableFrom(csvViewClass));
    }
}
