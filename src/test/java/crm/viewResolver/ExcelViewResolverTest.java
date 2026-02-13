package crm.viewResolver;

import crm.view.ExcelView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void testConstructor() {
        assertNotNull(excelViewResolver);
    }

    @Test
    void testResolveViewName() throws Exception {
        // Given
        String viewName = "excelView";
        Locale locale = Locale.US;

        // When
        View result = excelViewResolver.resolveViewName(viewName, locale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof ExcelView);
    }

    @Test
    void testResolveViewNameWithNullViewName() throws Exception {
        // Given
        Locale locale = Locale.US;

        // When
        View result = excelViewResolver.resolveViewName(null, locale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof ExcelView);
    }

    @Test
    void testResolveViewNameWithEmptyViewName() throws Exception {
        // Given
        String viewName = "";
        Locale locale = Locale.US;

        // When
        View result = excelViewResolver.resolveViewName(viewName, locale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof ExcelView);
    }

    @Test
    void testResolveViewNameWithNullLocale() throws Exception {
        // Given
        String viewName = "excelView";

        // When
        View result = excelViewResolver.resolveViewName(viewName, null);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof ExcelView);
    }

    @Test
    void testResolveViewNameWithBothNullParameters() throws Exception {
        // When
        View result = excelViewResolver.resolveViewName(null, null);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof ExcelView);
    }

    @Test
    void testResolveViewNameWithDifferentViewNames() throws Exception {
        // Test with various view names
        String[] viewNames = {"customerList", "contractReport", "userExport", "data"};
        Locale locale = Locale.US;

        for (String viewName : viewNames) {
            // When
            View result = excelViewResolver.resolveViewName(viewName, locale);

            // Then
            assertNotNull(result);
            assertTrue(result instanceof ExcelView);
        }
    }

    @Test
    void testResolveViewNameWithDifferentLocales() throws Exception {
        // Test with various locales
        String viewName = "excelView";
        Locale[] locales = {Locale.US, Locale.UK, Locale.FRANCE, Locale.GERMANY, Locale.JAPAN, Locale.CHINA};

        for (Locale locale : locales) {
            // When
            View result = excelViewResolver.resolveViewName(viewName, locale);

            // Then
            assertNotNull(result);
            assertTrue(result instanceof ExcelView);
        }
    }

    @Test
    void testResolveViewNameWithCustomLocale() throws Exception {
        // Given
        String viewName = "excelView";
        Locale customLocale = new Locale("es", "ES");

        // When
        View result = excelViewResolver.resolveViewName(viewName, customLocale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof ExcelView);
    }

    @Test
    void testResolveViewNameReturnsNewInstance() throws Exception {
        // Given
        String viewName = "excelView";
        Locale locale = Locale.US;

        // When
        View result1 = excelViewResolver.resolveViewName(viewName, locale);
        View result2 = excelViewResolver.resolveViewName(viewName, locale);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2, "Should return new instance each time");
    }

    @Test
    void testResolveViewNameWithSpecialCharacters() throws Exception {
        // Given
        String viewName = "excel_view-2024!@#$%";
        Locale locale = Locale.US;

        // When
        View result = excelViewResolver.resolveViewName(viewName, locale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof ExcelView);
    }

    @Test
    void testResolveViewNameWithLongViewName() throws Exception {
        // Given
        String viewName = "this_is_a_very_long_view_name_that_should_still_work_properly_123456789";
        Locale locale = Locale.US;

        // When
        View result = excelViewResolver.resolveViewName(viewName, locale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof ExcelView);
    }

    @Test
    void testResolveViewNameWithPathSeparators() throws Exception {
        // Given
        String viewName = "reports/excel/customer";
        Locale locale = Locale.US;

        // When
        View result = excelViewResolver.resolveViewName(viewName, locale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof ExcelView);
    }

    @Test
    void testResolveViewNameConsistency() throws Exception {
        // Given
        String viewName = "excelView";
        Locale locale = Locale.US;

        // When - Call multiple times
        View result1 = excelViewResolver.resolveViewName(viewName, locale);
        View result2 = excelViewResolver.resolveViewName(viewName, locale);
        View result3 = excelViewResolver.resolveViewName(viewName, locale);

        // Then - All should be ExcelView instances
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertTrue(result1 instanceof ExcelView);
        assertTrue(result2 instanceof ExcelView);
        assertTrue(result3 instanceof ExcelView);
    }

    @Test
    void testResolveViewNameWithDefaultLocale() throws Exception {
        // Given
        String viewName = "excelView";
        Locale locale = Locale.getDefault();

        // When
        View result = excelViewResolver.resolveViewName(viewName, locale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof ExcelView);
    }

    @Test
    void testResolveViewNameWithWhitespaceViewName() throws Exception {
        // Given
        String viewName = "   ";
        Locale locale = Locale.US;

        // When
        View result = excelViewResolver.resolveViewName(viewName, locale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof ExcelView);
    }
}
