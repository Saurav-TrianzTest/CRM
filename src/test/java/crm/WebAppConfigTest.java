package crm;

import crm.viewResolver.CsvViewResolver;
import crm.viewResolver.ExcelViewResolver;
import crm.viewResolver.PdfViewResolver;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebAppConfigTest {

    private WebAppConfig webAppConfig;

    @BeforeEach
    void setUp() {
        webAppConfig = new WebAppConfig();
    }

    @Test
    void addViewControllers_shouldConfigureViewControllers() {
        // Arrange
        ViewControllerRegistry registry = mock(ViewControllerRegistry.class);
        
        // Act
        webAppConfig.addViewControllers(registry);
        
        // Assert
        verify(registry, atLeastOnce()).addViewController(anyString());
    }

    @Test
    void configureContentNegotiation_shouldConfigureMediaTypes() {
        // Arrange
        ContentNegotiationConfigurer configurer = mock(ContentNegotiationConfigurer.class);
        
        // Act
        webAppConfig.configureContentNegotiation(configurer);
        
        // Assert
        verify(configurer).defaultContentType(MediaType.APPLICATION_JSON);
        verify(configurer).mediaTypes(anyMap());
    }

    @Test
    void templateResolver_shouldReturnConfiguredResolver() {
        // Act
        ClassLoaderTemplateResolver resolver = webAppConfig.templateResolver();
        
        // Assert
        assertNotNull(resolver);
    }

    @Test
    void templateEngine_shouldReturnConfiguredEngine() {
        // Act
        SpringTemplateEngine engine = webAppConfig.templateEngine();
        
        // Assert
        assertNotNull(engine);
    }

    @Test
    void viewResolver_shouldReturnThymeleafViewResolver() {
        // Act
        ViewResolver resolver = webAppConfig.viewResolver();
        
        // Assert
        assertNotNull(resolver);
        assertTrue(resolver instanceof ThymeleafViewResolver);
    }

    @Test
    void excelViewResolver_shouldReturnExcelViewResolver() {
        // Act
        ViewResolver resolver = webAppConfig.excelViewResolver();
        
        // Assert
        assertNotNull(resolver);
        assertTrue(resolver instanceof ExcelViewResolver);
    }

    @Test
    void csvViewResolver_shouldReturnCsvViewResolver() {
        // Act
        ViewResolver resolver = webAppConfig.csvViewResolver();
        
        // Assert
        assertNotNull(resolver);
        assertTrue(resolver instanceof CsvViewResolver);
    }

    @Test
    void pdfViewResolver_shouldReturnPdfViewResolver() {
        // Act
        ViewResolver resolver = webAppConfig.pdfViewResolver();
        
        // Assert
        assertNotNull(resolver);
        assertTrue(resolver instanceof PdfViewResolver);
    }

    @Test
    void contentNegotiatingViewResolver_shouldReturnConfiguredResolver() {
        // Arrange
        ContentNegotiationManager manager = mock(ContentNegotiationManager.class);
        
        // Act
        ViewResolver resolver = webAppConfig.contentNegotiatingViewResolver(manager);
        
        // Assert
        assertNotNull(resolver);
        assertTrue(resolver instanceof ContentNegotiatingViewResolver);
    }
}
