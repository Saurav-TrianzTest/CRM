package crm.viewResolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class ExcelViewResolverTest {

    private ExcelViewResolver excelViewResolver;

    @BeforeEach
    void setUp() {
        excelViewResolver = new ExcelViewResolver();
    }

    @Test
    void testResolveViewName() throws Exception {
        View view = excelViewResolver.resolveViewName("test", Locale.US);
        assertNotNull(view);
    }

    @Test
    void testResolveViewNameReturnsNonNull() throws Exception {
        View view = excelViewResolver.resolveViewName("any", Locale.getDefault());
        assertNotNull(view);
    }

    @Test
    void testExcelViewResolverNotNull() {
        assertNotNull(excelViewResolver);
    }
}
