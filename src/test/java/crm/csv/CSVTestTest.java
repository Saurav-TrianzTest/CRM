package crm.csv;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CSVTestTest {

    @Test
    void testClassExists() {
        assertNotNull(CSVTest.class);
    }

    @Test
    void testClassInstantiation() {
        CSVTest csvTest = new CSVTest();
        assertNotNull(csvTest);
    }

    @Test
    void testMainMethodExists() throws NoSuchMethodException {
        assertNotNull(CSVTest.class.getMethod("main", String[].class));
    }

    @Test
    void testMainMethodAccessibility() throws NoSuchMethodException {
        java.lang.reflect.Method mainMethod = CSVTest.class.getMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
    }
}
