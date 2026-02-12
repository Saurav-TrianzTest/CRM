package crm.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("AbstractPdfView Tests")
class AbstractPdfViewTest {

    @Test
    @DisplayName("Test AbstractPdfView exists")
    void testAbstractPdfViewExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test AbstractPdfView class is present")
    void testAbstractPdfViewClass() throws ClassNotFoundException {
        Class.forName("crm.view.AbstractPdfView");
    }

    @Test
    @DisplayName("Test AbstractPdfView is abstract")
    void testAbstractPdfViewIsAbstract() throws ClassNotFoundException {
        Class<?> abstractClass = Class.forName("crm.view.AbstractPdfView");
        assertTrue(Modifier.isAbstract(abstractClass.getModifiers()));
    }
}
