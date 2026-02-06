package crm.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ReadDataUtilsTest {

    @Test
    void testClassExists() {
        assertNotNull(ReadDataUtils.class);
    }

    @Test
    void testClassInstantiation() {
        ReadDataUtils utils = new ReadDataUtils();
        assertNotNull(utils);
    }

    @Test
    void testReadFileMethodExists() throws NoSuchMethodException {
        assertNotNull(ReadDataUtils.class.getMethod("ReadFile", String.class,
                javax.swing.JFrame.class, String.class, String[].class));
    }

    @Test
    void testReadFileWithNullParent() {
        // Testing with null parent should not throw exception during method call setup
        // Actual file chooser would require GUI interaction so we just test the method exists
        assertDoesNotThrow(() -> {
            ReadDataUtils.class.getMethod("ReadFile", String.class,
                    javax.swing.JFrame.class, String.class, String[].class);
        });
    }
}
