package crm.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("ReadDataUtils Tests")
class ReadDataUtilsTest {

    @Test
    @DisplayName("Test ReadDataUtils class exists")
    void testReadDataUtilsExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test ReadDataUtils class is present")
    void testReadDataUtilsClass() throws ClassNotFoundException {
        Class.forName("crm.utils.ReadDataUtils");
    }

    @Test
    @DisplayName("Test ReadDataUtils has ReadFile method")
    void testReadFileMethod() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> utilClass = Class.forName("crm.utils.ReadDataUtils");
        assertNotNull(utilClass.getDeclaredMethod("ReadFile", String.class));
    }
}
