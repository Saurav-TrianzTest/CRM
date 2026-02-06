package crm.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CsvViewTest {

    @Test
    void testClassExists() {
        assertNotNull(CsvView.class);
    }

    @Test
    void testClassInstantiation() {
        CsvView csvView = new CsvView();
        assertNotNull(csvView);
    }
}
