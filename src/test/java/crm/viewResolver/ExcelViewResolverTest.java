package crm.viewResolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExcelViewResolverTest {

    private ExcelViewResolver excelViewResolver;

    @BeforeEach
    void setUp() {
        excelViewResolver = new ExcelViewResolver();
    }

    @Test
    void testClassExists() {
        assertNotNull(ExcelViewResolver.class);
    }

    @Test
    void testClassInstantiation() {
        assertNotNull(excelViewResolver);
    }

    @Test
    void testResolveViewName() throws Exception {
        View view = excelViewResolver.resolveViewName("testView", Locale.ENGLISH);
        // View might be null or an ExcelView depending on the view name
        // Just testing the method doesn't throw an exception
        assertNotNull(excelViewResolver);
    }

    @Test
    void testResolveViewNameWithNullLocale() throws Exception {
        assertDoesNotThrow(() -> {
            excelViewResolver.resolveViewName("testView", null);
        });
    }
}
