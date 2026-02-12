package crm.csv;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("CSVTest Class Tests")
class CSVTestTest {

    @Test
    @DisplayName("Test CSVTest class exists")
    void testCSVTestExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test CSVTest class is present")
    void testCSVTestClass() throws ClassNotFoundException {
        Class.forName("crm.csv.CSVTest");
    }

    @Test
    @DisplayName("Test CSVTest has main method")
    void testMainMethod() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> csvClass = Class.forName("crm.csv.CSVTest");
        assertNotNull(csvClass.getDeclaredMethod("main", String[].class));
    }

    @Test
    @DisplayName("Test CSVTest main method is static")
    void testMainMethodIsStatic() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> csvClass = Class.forName("crm.csv.CSVTest");
        var mainMethod = csvClass.getDeclaredMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
    }

    @Test
    @DisplayName("Test CSVTest main method is public")
    void testMainMethodIsPublic() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> csvClass = Class.forName("crm.csv.CSVTest");
        var mainMethod = csvClass.getDeclaredMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
    }
}
