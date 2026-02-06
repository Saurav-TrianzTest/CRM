package crm.viewResolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CsvViewResolverTest {

    private CsvViewResolver csvViewResolver;

    @BeforeEach
    void setUp() {
        csvViewResolver = new CsvViewResolver();
    }

    @Test
    void testResolveViewName() throws Exception {
        View view = csvViewResolver.resolveViewName("test", Locale.US);
        assertNotNull(view);
    }

    @Test
    void testResolveViewNameReturnsNonNull() throws Exception {
        View view = csvViewResolver.resolveViewName("any", Locale.getDefault());
        assertNotNull(view);
    }

    @Test
    void testCsvViewResolverNotNull() {
        assertNotNull(csvViewResolver);
    }
}
