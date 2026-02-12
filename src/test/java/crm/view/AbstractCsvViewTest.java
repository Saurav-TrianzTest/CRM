package crm.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("AbstractCsvView Tests")
class AbstractCsvViewTest {

    @Test
    @DisplayName("Test AbstractCsvView exists")
    void testAbstractCsvViewExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test AbstractCsvView class is present")
    void testAbstractCsvViewClass() throws ClassNotFoundException {
        Class.forName("crm.view.AbstractCsvView");
    }

    @Test
    @DisplayName("Test AbstractCsvView is abstract")
    void testAbstractCsvViewIsAbstract() throws ClassNotFoundException {
        Class<?> abstractClass = Class.forName("crm.view.AbstractCsvView");
        assertTrue(Modifier.isAbstract(abstractClass.getModifiers()));
    }
}
