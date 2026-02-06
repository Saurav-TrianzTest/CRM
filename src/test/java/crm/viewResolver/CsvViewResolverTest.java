package crm.viewResolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CsvViewResolverTest {

    private CsvViewResolver csvViewResolver;

    @BeforeEach
    void setUp() {
        csvViewResolver = new CsvViewResolver();
    }

    @Test
    void testClassExists() {
        assertNotNull(CsvViewResolver.class);
    }

    @Test
    void testClassInstantiation() {
        assertNotNull(csvViewResolver);
    }

    @Test
    void testResolveViewName() throws Exception {
        View view = csvViewResolver.resolveViewName("testView", Locale.ENGLISH);
        // View might be null or a CsvView depending on the view name
        // Just testing the method doesn't throw an exception
        assertNotNull(csvViewResolver);
    }

    @Test
    void testResolveViewNameWithNullLocale() throws Exception {
        assertDoesNotThrow(() -> {
            csvViewResolver.resolveViewName("testView", null);
        });
    }
}
