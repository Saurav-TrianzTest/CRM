package crm.viewResolver;

import crm.view.CsvView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
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
    void constructor_shouldCreateCsvViewResolver() {
        // Assert
        assertNotNull(csvViewResolver);
    }

    @Test
    void resolveViewName_shouldReturnCsvView() throws Exception {
        // Act
        View view = csvViewResolver.resolveViewName("test", Locale.getDefault());
        
        // Assert
        assertNotNull(view);
        assertTrue(view instanceof CsvView);
    }

    @Test
    void resolveViewName_withNullViewName_shouldReturnCsvView() throws Exception {
        // Act
        View view = csvViewResolver.resolveViewName(null, Locale.getDefault());
        
        // Assert
        assertNotNull(view);
        assertTrue(view instanceof CsvView);
    }

    @Test
    void resolveViewName_withDifferentLocale_shouldReturnCsvView() throws Exception {
        // Act
        View view = csvViewResolver.resolveViewName("test", Locale.ITALIAN);
        
        // Assert
        assertNotNull(view);
        assertTrue(view instanceof CsvView);
    }
}
