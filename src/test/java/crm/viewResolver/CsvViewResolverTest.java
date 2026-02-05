package crm.viewResolver;

import crm.view.CsvView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class CsvViewResolverTest {

    private CsvViewResolver csvViewResolver;

    @BeforeEach
    public void setUp() {
        csvViewResolver = new CsvViewResolver();
    }

    @Test
    public void testCsvViewResolverCreation() {
        assertNotNull(csvViewResolver);
    }

    @Test
    public void testResolveViewName() throws Exception {
        View view = csvViewResolver.resolveViewName("testView", Locale.ENGLISH);
        assertNotNull(view);
        assertTrue(view instanceof CsvView);
    }

    @Test
    public void testResolveViewNameWithNullName() throws Exception {
        View view = csvViewResolver.resolveViewName(null, Locale.ENGLISH);
        assertNotNull(view);
    }

    @Test
    public void testResolveViewNameWithNullLocale() throws Exception {
        View view = csvViewResolver.resolveViewName("testView", null);
        assertNotNull(view);
    }

    @Test
    public void testResolveViewNameWithDifferentLocales() throws Exception {
        View view1 = csvViewResolver.resolveViewName("test", Locale.US);
        View view2 = csvViewResolver.resolveViewName("test", Locale.CANADA);

        assertNotNull(view1);
        assertNotNull(view2);
    }

    @Test
    public void testResolveViewNameWithEmptyString() throws Exception {
        View view = csvViewResolver.resolveViewName("", Locale.ENGLISH);
        assertNotNull(view);
    }
}
