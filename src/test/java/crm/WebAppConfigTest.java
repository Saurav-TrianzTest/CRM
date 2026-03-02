package crm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebAppConfigTest {

    private WebAppConfig config;

    @BeforeEach
    void setUp() {
        config = new WebAppConfig();
    }

    @Test
    void testAddViewControllers() {
        ViewControllerRegistry registry = mock(ViewControllerRegistry.class);
        config.addViewControllers(registry);
        verify(registry, atLeastOnce()).addViewController(anyString());
    }

    @Test
    void testTemplateResolver() {
        ClassLoaderTemplateResolver resolver = config.templateResolver();
        assertNotNull(resolver);
    }

    @Test
    void testTemplateEngine() {
        SpringTemplateEngine engine = config.templateEngine();
        assertNotNull(engine);
    }

    @Test
    void testViewResolver() {
        ViewResolver resolver = config.viewResolver();
        assertNotNull(resolver);
    }

    @Test
    void testExcelViewResolver() {
        ViewResolver resolver = config.excelViewResolver();
        assertNotNull(resolver);
    }

    @Test
    void testCsvViewResolver() {
        ViewResolver resolver = config.csvViewResolver();
        assertNotNull(resolver);
    }

    @Test
    void testPdfViewResolver() {
        ViewResolver resolver = config.pdfViewResolver();
        assertNotNull(resolver);
    }

    @Test
    void testConfigInstantiation() {
        assertNotNull(config);
    }
}
