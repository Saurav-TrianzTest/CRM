package crm;

import crm.viewResolver.CsvViewResolver;
import crm.viewResolver.ExcelViewResolver;
import crm.viewResolver.PdfViewResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.thymeleaf.dialect.springdata.SpringDataDialect;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WebAppConfigTest {

    @InjectMocks
    private WebAppConfig webAppConfig;

    @Mock
    private ViewControllerRegistry viewControllerRegistry;

    @Mock
    private ContentNegotiationConfigurer contentNegotiationConfigurer;

    @Mock
    private ContentNegotiationManager contentNegotiationManager;

    @BeforeEach
    void setUp() {
        webAppConfig = new WebAppConfig();
    }

    @Test
    void testConstructor() {
        assertNotNull(webAppConfig);
    }

    @Test
    void testAddViewControllers() {
        // Given
        when(viewControllerRegistry.addViewController(anyString())).thenReturn(mock(org.springframework.web.servlet.config.annotation.ViewControllerRegistration.class));

        // When
        webAppConfig.addViewControllers(viewControllerRegistry);

        // Then
        verify(viewControllerRegistry).addViewController("/login");
        verify(viewControllerRegistry).addViewController("/");
        verify(viewControllerRegistry).addViewController("/user/menu");
        verify(viewControllerRegistry).addViewController("/customer/menu");
        verify(viewControllerRegistry).addViewController("/contract/menu");
        verify(viewControllerRegistry).addViewController("/contract/search");
        verify(viewControllerRegistry).addViewController("/admin");
        verify(viewControllerRegistry).addViewController("/search");
        verify(viewControllerRegistry).addViewController("/403");
        verify(viewControllerRegistry).addViewController("/logout");
        verify(viewControllerRegistry).setOrder(anyInt());
    }

    @Test
    void testAddViewControllersWithNullRegistry() {
        // When/Then - Should handle null gracefully or throw NPE as per Spring contract
        assertThrows(NullPointerException.class, () -> {
            webAppConfig.addViewControllers(null);
        });
    }

    @Test
    void testConfigureContentNegotiation() {
        // Given
        when(contentNegotiationConfigurer.ignoreAcceptHeader(false)).thenReturn(contentNegotiationConfigurer);
        when(contentNegotiationConfigurer.defaultContentType(any(MediaType.class))).thenReturn(contentNegotiationConfigurer);
        when(contentNegotiationConfigurer.mediaTypes(any(Map.class))).thenReturn(contentNegotiationConfigurer);

        // When
        webAppConfig.configureContentNegotiation(contentNegotiationConfigurer);

        // Then
        verify(contentNegotiationConfigurer).ignoreAcceptHeader(false);
        verify(contentNegotiationConfigurer).defaultContentType(MediaType.APPLICATION_JSON);
        verify(contentNegotiationConfigurer).mediaTypes(any(Map.class));
    }

    @Test
    void testConfigureContentNegotiationWithNullConfigurer() {
        // When/Then
        assertThrows(NullPointerException.class, () -> {
            webAppConfig.configureContentNegotiation(null);
        });
    }

    @Test
    void testContentNegotiatingViewResolver() {
        // When
        ViewResolver result = webAppConfig.contentNegotiatingViewResolver(contentNegotiationManager);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof ContentNegotiatingViewResolver);

        ContentNegotiatingViewResolver cnvr = (ContentNegotiatingViewResolver) result;
        assertNotNull(cnvr);
    }

    @Test
    void testContentNegotiatingViewResolverWithNullManager() {
        // When
        ViewResolver result = webAppConfig.contentNegotiatingViewResolver(null);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof ContentNegotiatingViewResolver);
    }

    @Test
    void testTemplateResolver() {
        // When
        ClassLoaderTemplateResolver result = webAppConfig.templateResolver();

        // Then
        assertNotNull(result);
        assertEquals("templates/", result.getPrefix());
        assertEquals(".html", result.getSuffix());
        assertEquals("HTML", result.getTemplateMode());
        assertEquals("UTF-8", result.getCharacterEncoding());
        assertFalse(result.isCacheable());
    }

    @Test
    void testTemplateEngine() {
        // When
        SpringTemplateEngine result = webAppConfig.templateEngine();

        // Then
        assertNotNull(result);
        assertNotNull(result.getDialects());
        assertTrue(result.getDialects().stream()
                .anyMatch(dialect -> dialect.getClass().getSimpleName().contains("SpringSecurity")));
    }

    @Test
    void testTemplateEngineWithResolver() {
        // Given
        ITemplateResolver mockResolver = mock(ITemplateResolver.class);

        // When
        org.thymeleaf.TemplateEngine result = webAppConfig.templateEngine(mockResolver);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof SpringTemplateEngine);
        SpringTemplateEngine engine = (SpringTemplateEngine) result;
        assertTrue(engine.getDialects().stream()
                .anyMatch(dialect -> dialect.getClass().getSimpleName().contains("Java8Time")));
    }

    @Test
    void testTemplateEngineWithNullResolver() {
        // When/Then
        assertThrows(Exception.class, () -> {
            org.thymeleaf.TemplateEngine result = webAppConfig.templateEngine(null);
            // Attempting to use the engine may throw an exception
        });
    }

    @Test
    void testViewResolver() {
        // When
        ViewResolver result = webAppConfig.viewResolver();

        // Then
        assertNotNull(result);
        assertTrue(result instanceof ThymeleafViewResolver);

        ThymeleafViewResolver tvr = (ThymeleafViewResolver) result;
        assertEquals("UTF-8", tvr.getCharacterEncoding());
    }

    @Test
    void testExcelViewResolver() {
        // When
        ViewResolver result = webAppConfig.excelViewResolver();

        // Then
        assertNotNull(result);
        assertTrue(result instanceof ExcelViewResolver);
    }

    @Test
    void testCsvViewResolver() {
        // When
        ViewResolver result = webAppConfig.csvViewResolver();

        // Then
        assertNotNull(result);
        assertTrue(result instanceof CsvViewResolver);
    }

    @Test
    void testPdfViewResolver() {
        // When
        ViewResolver result = webAppConfig.pdfViewResolver();

        // Then
        assertNotNull(result);
        assertTrue(result instanceof PdfViewResolver);
    }

    @Test
    void testSpringDataDialect() {
        // When
        SpringDataDialect result = webAppConfig.springDataDialect();

        // Then
        assertNotNull(result);
        assertTrue(result instanceof SpringDataDialect);
    }

    @Test
    void testContentNegotiationConfigurer_MediaTypes() {
        // This test verifies that the media types are set correctly
        ContentNegotiationConfigurer configurer = mock(ContentNegotiationConfigurer.class);
        when(configurer.ignoreAcceptHeader(false)).thenReturn(configurer);
        when(configurer.defaultContentType(any(MediaType.class))).thenReturn(configurer);

        // Capture the media types map
        when(configurer.mediaTypes(argThat(map -> {
            Map<String, MediaType> mediaTypes = (Map<String, MediaType>) map;
            return mediaTypes.containsKey("html") &&
                   mediaTypes.containsKey("json") &&
                   mediaTypes.containsKey("xls") &&
                   mediaTypes.containsKey("pdf") &&
                   mediaTypes.containsKey("csv");
        }))).thenReturn(configurer);

        // When
        webAppConfig.configureContentNegotiation(configurer);

        // Then
        verify(configurer).mediaTypes(any(Map.class));
    }

    @Test
    void testMultipleViewResolversRegistered() {
        // When
        ViewResolver result = webAppConfig.contentNegotiatingViewResolver(contentNegotiationManager);

        // Then
        assertNotNull(result);
        ContentNegotiatingViewResolver cnvr = (ContentNegotiatingViewResolver) result;

        // Verify that multiple view resolvers are registered
        // Note: We can't directly access the list but we can verify the bean creation methods exist
        assertNotNull(webAppConfig.csvViewResolver());
        assertNotNull(webAppConfig.excelViewResolver());
        assertNotNull(webAppConfig.pdfViewResolver());
        assertNotNull(webAppConfig.viewResolver());
    }

    @Test
    void testTemplateResolverConfiguration() {
        // When
        ClassLoaderTemplateResolver resolver = webAppConfig.templateResolver();

        // Then
        assertNotNull(resolver);
        assertEquals("templates/", resolver.getPrefix());
        assertEquals(".html", resolver.getSuffix());
        assertEquals("HTML", resolver.getTemplateMode());
        assertEquals("UTF-8", resolver.getCharacterEncoding());
        assertFalse(resolver.isCacheable());
    }

    @Test
    void testBeanCreationIsolation() {
        // Test that each bean creation method returns a new instance
        ViewResolver excel1 = webAppConfig.excelViewResolver();
        ViewResolver excel2 = webAppConfig.excelViewResolver();

        ViewResolver csv1 = webAppConfig.csvViewResolver();
        ViewResolver csv2 = webAppConfig.csvViewResolver();

        ViewResolver pdf1 = webAppConfig.pdfViewResolver();
        ViewResolver pdf2 = webAppConfig.pdfViewResolver();

        // Each call should create a new instance
        assertNotSame(excel1, excel2);
        assertNotSame(csv1, csv2);
        assertNotSame(pdf1, pdf2);
    }

    @Test
    void testSpringDataDialectCreation() {
        // When
        SpringDataDialect dialect1 = webAppConfig.springDataDialect();
        SpringDataDialect dialect2 = webAppConfig.springDataDialect();

        // Then
        assertNotNull(dialect1);
        assertNotNull(dialect2);
        assertNotSame(dialect1, dialect2);
    }
}
