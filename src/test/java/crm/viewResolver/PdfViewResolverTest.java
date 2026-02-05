package crm.viewResolver;

import crm.view.PdfView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.web.servlet.View;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class PdfViewResolverTest {

    private PdfViewResolver pdfViewResolver;

    @BeforeEach
    public void setUp() {
        pdfViewResolver = new PdfViewResolver();
    }

    @Test
    public void testPdfViewResolverCreation() {
        assertNotNull(pdfViewResolver);
    }

    @Test
    public void testResolveViewName() throws Exception {
        View view = pdfViewResolver.resolveViewName("testView", Locale.ENGLISH);
        assertNotNull(view);
        assertTrue(view instanceof PdfView);
    }

    @Test
    public void testResolveViewNameWithNullName() throws Exception {
        View view = pdfViewResolver.resolveViewName(null, Locale.ENGLISH);
        assertNotNull(view);
    }

    @Test
    public void testResolveViewNameWithNullLocale() throws Exception {
        View view = pdfViewResolver.resolveViewName("testView", null);
        assertNotNull(view);
    }

    @Test
    public void testResolveViewNameWithDifferentLocales() throws Exception {
        View view1 = pdfViewResolver.resolveViewName("test", Locale.US);
        View view2 = pdfViewResolver.resolveViewName("test", Locale.UK);
        View view3 = pdfViewResolver.resolveViewName("test", Locale.FRANCE);

        assertNotNull(view1);
        assertNotNull(view2);
        assertNotNull(view3);
    }

    @Test
    public void testResolveViewNameWithEmptyString() throws Exception {
        View view = pdfViewResolver.resolveViewName("", Locale.ENGLISH);
        assertNotNull(view);
    }
}
