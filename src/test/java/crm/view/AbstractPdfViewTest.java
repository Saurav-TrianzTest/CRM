package crm.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AbstractPdfViewTest {

    @Test
    void testClassExists() {
        assertNotNull(AbstractPdfView.class);
    }

    @Test
    void testIsAbstractClass() {
        assertTrue(java.lang.reflect.Modifier.isAbstract(AbstractPdfView.class.getModifiers()));
    }
}
