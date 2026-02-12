package crm.viewResolver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("ExcelViewResolver Tests")
class ExcelViewResolverTest {

    @Test
    @DisplayName("Test ExcelViewResolver exists")
    void testExcelViewResolverExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test ExcelViewResolver class is present")
    void testExcelViewResolverClass() throws ClassNotFoundException {
        Class.forName("crm.viewResolver.ExcelViewResolver");
    }

    @Test
    @DisplayName("Test ExcelViewResolver has resolveViewName method")
    void testExcelViewResolverHasResolveMethod() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> resolverClass = Class.forName("crm.viewResolver.ExcelViewResolver");
        assertNotNull(resolverClass.getDeclaredMethod("resolveViewName", String.class, java.util.Locale.class));
    }
}
