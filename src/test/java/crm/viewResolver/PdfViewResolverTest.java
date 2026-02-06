package crm.viewResolver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void testResolveViewName() throws Exception {
        View view = pdfViewResolver.resolveViewName("test", Locale.US);
        assertNotNull(view);
    }

    @Test
    void testResolveViewNameReturnsNonNull() throws Exception {
        View view = pdfViewResolver.resolveViewName("any", Locale.getDefault());
        assertNotNull(view);
    }

    @Test
    void testPdfViewResolverNotNull() {
        assertNotNull(pdfViewResolver);
    }
}
