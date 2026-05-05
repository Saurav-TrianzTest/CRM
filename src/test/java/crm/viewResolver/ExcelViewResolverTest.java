package crm.viewResolver;

import crm.view.ExcelView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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
    void constructor_shouldCreateExcelViewResolver() {
        // Assert
        assertNotNull(excelViewResolver);
    }

    @Test
    void resolveViewName_shouldReturnExcelView() throws Exception {
        // Act
        View view = excelViewResolver.resolveViewName("test", Locale.getDefault());
        
        // Assert
        assertNotNull(view);
        assertTrue(view instanceof ExcelView);
    }

    @Test
    void resolveViewName_withNullViewName_shouldReturnExcelView() throws Exception {
        // Act
        View view = excelViewResolver.resolveViewName(null, Locale.getDefault());
        
        // Assert
        assertNotNull(view);
        assertTrue(view instanceof ExcelView);
    }

    @Test
    void resolveViewName_withDifferentLocale_shouldReturnExcelView() throws Exception {
        // Act
        View view = excelViewResolver.resolveViewName("test", Locale.GERMAN);
        
        // Assert
        assertNotNull(view);
        assertTrue(view instanceof ExcelView);
    }
}
