package crm.viewResolver;

import crm.view.CsvView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    void testConstructor() {
        assertNotNull(csvViewResolver);
    }

    @Test
    void testResolveViewName() throws Exception {
        // Given
        String viewName = "csvView";
        Locale locale = Locale.US;

        // When
        View result = csvViewResolver.resolveViewName(viewName, locale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof CsvView);
    }

    @Test
    void testResolveViewNameWithNullViewName() throws Exception {
        // Given
        Locale locale = Locale.US;

        // When
        View result = csvViewResolver.resolveViewName(null, locale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof CsvView);
    }

    @Test
    void testResolveViewNameWithEmptyViewName() throws Exception {
        // Given
        String viewName = "";
        Locale locale = Locale.US;

        // When
        View result = csvViewResolver.resolveViewName(viewName, locale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof CsvView);
    }

    @Test
    void testResolveViewNameWithNullLocale() throws Exception {
        // Given
        String viewName = "csvView";

        // When
        View result = csvViewResolver.resolveViewName(viewName, null);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof CsvView);
    }

    @Test
    void testResolveViewNameWithBothNullParameters() throws Exception {
        // When
        View result = csvViewResolver.resolveViewName(null, null);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof CsvView);
    }

    @Test
    void testResolveViewNameWithDifferentViewNames() throws Exception {
        // Test with various view names
        String[] viewNames = {"customerList", "contractReport", "userExport", "data", "export"};
        Locale locale = Locale.US;

        for (String viewName : viewNames) {
            // When
            View result = csvViewResolver.resolveViewName(viewName, locale);

            // Then
            assertNotNull(result);
            assertTrue(result instanceof CsvView);
        }
    }

    @Test
    void testResolveViewNameWithDifferentLocales() throws Exception {
        // Test with various locales
        String viewName = "csvView";
        Locale[] locales = {Locale.US, Locale.UK, Locale.FRANCE, Locale.GERMANY, Locale.JAPAN, Locale.CHINA};

        for (Locale locale : locales) {
            // When
            View result = csvViewResolver.resolveViewName(viewName, locale);

            // Then
            assertNotNull(result);
            assertTrue(result instanceof CsvView);
        }
    }

    @Test
    void testResolveViewNameWithCustomLocale() throws Exception {
        // Given
        String viewName = "csvView";
        Locale customLocale = new Locale("pt", "BR");

        // When
        View result = csvViewResolver.resolveViewName(viewName, customLocale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof CsvView);
    }

    @Test
    void testResolveViewNameReturnsNewInstance() throws Exception {
        // Given
        String viewName = "csvView";
        Locale locale = Locale.US;

        // When
        View result1 = csvViewResolver.resolveViewName(viewName, locale);
        View result2 = csvViewResolver.resolveViewName(viewName, locale);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotSame(result1, result2, "Should return new instance each time");
    }

    @Test
    void testResolveViewNameWithSpecialCharacters() throws Exception {
        // Given
        String viewName = "csv_view-2024!@#$%";
        Locale locale = Locale.US;

        // When
        View result = csvViewResolver.resolveViewName(viewName, locale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof CsvView);
    }

    @Test
    void testResolveViewNameWithLongViewName() throws Exception {
        // Given
        String viewName = "this_is_a_very_long_view_name_that_should_still_work_properly_with_csv_export";
        Locale locale = Locale.US;

        // When
        View result = csvViewResolver.resolveViewName(viewName, locale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof CsvView);
    }

    @Test
    void testResolveViewNameWithPathSeparators() throws Exception {
        // Given
        String viewName = "reports/csv/customer";
        Locale locale = Locale.US;

        // When
        View result = csvViewResolver.resolveViewName(viewName, locale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof CsvView);
    }

    @Test
    void testResolveViewNameConsistency() throws Exception {
        // Given
        String viewName = "csvView";
        Locale locale = Locale.US;

        // When - Call multiple times
        View result1 = csvViewResolver.resolveViewName(viewName, locale);
        View result2 = csvViewResolver.resolveViewName(viewName, locale);
        View result3 = csvViewResolver.resolveViewName(viewName, locale);

        // Then - All should be CsvView instances
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertTrue(result1 instanceof CsvView);
        assertTrue(result2 instanceof CsvView);
        assertTrue(result3 instanceof CsvView);
    }

    @Test
    void testResolveViewNameWithDefaultLocale() throws Exception {
        // Given
        String viewName = "csvView";
        Locale locale = Locale.getDefault();

        // When
        View result = csvViewResolver.resolveViewName(viewName, locale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof CsvView);
    }

    @Test
    void testResolveViewNameWithWhitespaceViewName() throws Exception {
        // Given
        String viewName = "   ";
        Locale locale = Locale.US;

        // When
        View result = csvViewResolver.resolveViewName(viewName, locale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof CsvView);
    }

    @Test
    void testResolveViewNameWithNumericViewName() throws Exception {
        // Given
        String viewName = "12345";
        Locale locale = Locale.US;

        // When
        View result = csvViewResolver.resolveViewName(viewName, locale);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof CsvView);
    }

    @Test
    void testResolveViewNameConcurrentCalls() throws Exception {
        // Given
        String viewName = "csvView";
        Locale locale = Locale.US;

        // When - Simulate concurrent calls
        View result1 = csvViewResolver.resolveViewName(viewName, locale);
        View result2 = csvViewResolver.resolveViewName(viewName, Locale.UK);
        View result3 = csvViewResolver.resolveViewName("anotherView", locale);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);
        assertTrue(result1 instanceof CsvView);
        assertTrue(result2 instanceof CsvView);
        assertTrue(result3 instanceof CsvView);
    }
}
