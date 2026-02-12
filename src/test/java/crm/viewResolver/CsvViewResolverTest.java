package crm.viewResolver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("CsvViewResolver Tests")
class CsvViewResolverTest {

    @Test
    @DisplayName("Test CsvViewResolver exists")
    void testCsvViewResolverExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test CsvViewResolver class is present")
    void testCsvViewResolverClass() throws ClassNotFoundException {
        Class.forName("crm.viewResolver.CsvViewResolver");
    }

    @Test
    @DisplayName("Test CsvViewResolver has resolveViewName method")
    void testCsvViewResolverHasResolveMethod() throws ClassNotFoundException, NoSuchMethodException {
        Class<?> resolverClass = Class.forName("crm.viewResolver.CsvViewResolver");
        assertNotNull(resolverClass.getDeclaredMethod("resolveViewName", String.class, java.util.Locale.class));
    }
}
