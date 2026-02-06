package crm;

import crm.viewResolver.CsvViewResolver;
import crm.viewResolver.ExcelViewResolver;
import crm.viewResolver.PdfViewResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.ViewResolver;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WebAppConfigTest {

    private WebAppConfig webAppConfig;

    @BeforeEach
    void setUp() {
        webAppConfig = new WebAppConfig();
    }

    @Test
    void testCsvViewResolver() {
        ViewResolver resolver = webAppConfig.csvViewResolver();

        assertNotNull(resolver);
        assertInstanceOf(CsvViewResolver.class, resolver);
    }

    @Test
    void testExcelViewResolver() {
        ViewResolver resolver = webAppConfig.excelViewResolver();

        assertNotNull(resolver);
        assertInstanceOf(ExcelViewResolver.class, resolver);
    }

    @Test
    void testPdfViewResolver() {
        ViewResolver resolver = webAppConfig.pdfViewResolver();

        assertNotNull(resolver);
        assertInstanceOf(PdfViewResolver.class, resolver);
    }

    @Test
    void testWebAppConfigInstantiation() {
        assertNotNull(webAppConfig);
    }

    @Test
    void testMultipleCsvViewResolverInstances() {
        ViewResolver resolver1 = webAppConfig.csvViewResolver();
        ViewResolver resolver2 = webAppConfig.csvViewResolver();

        assertNotNull(resolver1);
        assertNotNull(resolver2);
        // Each call should create a new instance
        assertNotSame(resolver1, resolver2);
    }

    @Test
    void testMultipleExcelViewResolverInstances() {
        ViewResolver resolver1 = webAppConfig.excelViewResolver();
        ViewResolver resolver2 = webAppConfig.excelViewResolver();

        assertNotNull(resolver1);
        assertNotNull(resolver2);
        assertNotSame(resolver1, resolver2);
    }

    @Test
    void testMultiplePdfViewResolverInstances() {
        ViewResolver resolver1 = webAppConfig.pdfViewResolver();
        ViewResolver resolver2 = webAppConfig.pdfViewResolver();

        assertNotNull(resolver1);
        assertNotNull(resolver2);
        assertNotSame(resolver1, resolver2);
    }
}
