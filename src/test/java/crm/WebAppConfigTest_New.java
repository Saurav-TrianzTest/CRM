package crm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebAppConfigTest_New {

    private WebAppConfig webAppConfig;

    @BeforeEach
    void setUp() {
        webAppConfig = new WebAppConfig();
    }

    @Test
    void testAddViewControllers() {
        ViewControllerRegistry registry = mock(ViewControllerRegistry.class);
        webAppConfig.addViewControllers(registry);
        verify(registry, atLeastOnce()).addViewController(anyString());
    }

    @Test
    void testTemplateResolver() {
        assertNotNull(webAppConfig.templateResolver());
    }

    @Test
    void testTemplateEngine() {
        assertNotNull(webAppConfig.templateEngine());
    }

    @Test
    void testViewResolver() {
        assertNotNull(webAppConfig.viewResolver());
    }

    @Test
    void testExcelViewResolver() {
        assertNotNull(webAppConfig.excelViewResolver());
    }

    @Test
    void testCsvViewResolver() {
        assertNotNull(webAppConfig.csvViewResolver());
    }

    @Test
    void testPdfViewResolver() {
        assertNotNull(webAppConfig.pdfViewResolver());
    }

    @Test
    void testContentNegotiatingViewResolver() {
        ContentNegotiationManager manager = mock(ContentNegotiationManager.class);
        ViewResolver resolver = webAppConfig.contentNegotiatingViewResolver(manager);
        assertNotNull(resolver);
    }
}
