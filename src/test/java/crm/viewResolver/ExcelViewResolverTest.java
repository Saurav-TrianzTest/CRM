package crm.viewResolver;

import crm.view.ExcelView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class ExcelViewResolverTest {

    private ExcelViewResolver excelViewResolver;

    @BeforeEach
    public void setUp() {
        excelViewResolver = new ExcelViewResolver();
    }

    @Test
    public void testExcelViewResolverCreation() {
        assertNotNull(excelViewResolver);
    }

    @Test
    public void testResolveViewName() throws Exception {
        View view = excelViewResolver.resolveViewName("testView", Locale.ENGLISH);
        assertNotNull(view);
        assertTrue(view instanceof ExcelView);
    }

    @Test
    public void testResolveViewNameWithNullName() throws Exception {
        View view = excelViewResolver.resolveViewName(null, Locale.ENGLISH);
        assertNotNull(view);
    }

    @Test
    public void testResolveViewNameWithNullLocale() throws Exception {
        View view = excelViewResolver.resolveViewName("testView", null);
        assertNotNull(view);
    }

    @Test
    public void testResolveViewNameWithDifferentLocales() throws Exception {
        View view1 = excelViewResolver.resolveViewName("test", Locale.US);
        View view2 = excelViewResolver.resolveViewName("test", Locale.GERMANY);

        assertNotNull(view1);
        assertNotNull(view2);
    }

    @Test
    public void testResolveViewNameWithEmptyString() throws Exception {
        View view = excelViewResolver.resolveViewName("", Locale.ENGLISH);
        assertNotNull(view);
    }
}
