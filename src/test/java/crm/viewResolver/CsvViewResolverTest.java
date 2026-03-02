package crm.viewResolver;

import crm.view.CsvView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class CsvViewResolverTest {

    private CsvViewResolver resolver;

    @BeforeEach
    void setUp() {
        resolver = new CsvViewResolver();
    }

    @Test
    void testResolveViewName() throws Exception {
        View view = resolver.resolveViewName("test", Locale.US);
        assertNotNull(view);
        assertTrue(view instanceof CsvView);
    }

    @Test
    void testResolveViewNameWithNullString() throws Exception {
        View view = resolver.resolveViewName(null, Locale.US);
        assertNotNull(view);
        assertTrue(view instanceof CsvView);
    }

    @Test
    void testResolveViewNameWithDifferentLocale() throws Exception {
        View view = resolver.resolveViewName("test", Locale.JAPAN);
        assertNotNull(view);
        assertTrue(view instanceof CsvView);
    }

    @Test
    void testResolveViewNameReturnsNewInstance() throws Exception {
        View view1 = resolver.resolveViewName("test1", Locale.US);
        View view2 = resolver.resolveViewName("test2", Locale.US);
        assertNotSame(view1, view2);
    }
}
