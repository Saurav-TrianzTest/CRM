package crm.viewResolver;

import crm.view.PdfView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class PdfViewResolverTest {

    private PdfViewResolver pdfViewResolver;

    @BeforeEach
    void setUp() {
        pdfViewResolver = new PdfViewResolver();
    }

    @Test
    void constructor_shouldCreatePdfViewResolver() {
        // Assert
        assertNotNull(pdfViewResolver);
    }

    @Test
    void resolveViewName_shouldReturnPdfView() throws Exception {
        // Act
        View view = pdfViewResolver.resolveViewName("test", Locale.getDefault());
        
        // Assert
        assertNotNull(view);
        assertTrue(view instanceof PdfView);
    }

    @Test
    void resolveViewName_withNullViewName_shouldReturnPdfView() throws Exception {
        // Act
        View view = pdfViewResolver.resolveViewName(null, Locale.getDefault());
        
        // Assert
        assertNotNull(view);
        assertTrue(view instanceof PdfView);
    }

    @Test
    void resolveViewName_withDifferentLocale_shouldReturnPdfView() throws Exception {
        // Act
        View view = pdfViewResolver.resolveViewName("test", Locale.FRENCH);
        
        // Assert
        assertNotNull(view);
        assertTrue(view instanceof PdfView);
    }
}
