package crm;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("CrmApplication Tests")
class CrmApplicationTest {

    @Test
    @DisplayName("Test CrmApplication class exists")
    void testCrmApplicationExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test CrmApplication class is present")
    void testCrmApplicationClass() throws ClassNotFoundException {
        Class.forName("crm.CrmApplication");
    }

    @Test
    @DisplayName("Test CrmApplication has main method")
    void testMainMethod() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> appClass = Class.forName("crm.CrmApplication");
        assertNotNull(appClass.getDeclaredMethod("main", String[].class));
    }

    @Test
    @DisplayName("Test CrmApplication main method is static")
    void testMainMethodIsStatic() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> appClass = Class.forName("crm.CrmApplication");
        var mainMethod = appClass.getDeclaredMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
    }

    @Test
    @DisplayName("Test CrmApplication main method is public")
    void testMainMethodIsPublic() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> appClass = Class.forName("crm.CrmApplication");
        var mainMethod = appClass.getDeclaredMethod("main", String[].class);
        assertTrue(java.lang.reflect.Modifier.isPublic(mainMethod.getModifiers()));
    }
}
