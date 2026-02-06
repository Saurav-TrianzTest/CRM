package crm.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AbstractCsvViewTest {

    @Test
    void testClassExists() {
        assertNotNull(AbstractCsvView.class);
    }

    @Test
    void testIsAbstractClass() {
        assertTrue(java.lang.reflect.Modifier.isAbstract(AbstractCsvView.class.getModifiers()));
    }
}
