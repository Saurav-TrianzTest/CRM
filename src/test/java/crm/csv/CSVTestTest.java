package crm.csv;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVTestTest {

    @Test
    void testCSVTestClassExists() {
        assertNotNull(CSVTest.class);
    }

    @Test
    void testCSVTestClassNotNull() {
        assertTrue(CSVTest.class != null);
    }

    @Test
    void testCSVTestIsPublicClass() {
        int modifiers = CSVTest.class.getModifiers();
        assertTrue(java.lang.reflect.Modifier.isPublic(modifiers));
    }
}
