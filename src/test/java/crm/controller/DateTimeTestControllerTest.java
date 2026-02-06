package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DateTimeTestControllerTest {

    @Mock
    private Model model;

    @InjectMocks
    private DateTimeTestController dateTimeTestController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDateTimeTestControllerConstructor() {
        assertNotNull(dateTimeTestController);
    }

    @Test
    public void testDateTimeTestMethod() {
        String result = dateTimeTestController.dateTimeTest(model);
        assertNotNull(result);
        assertEquals("date/test", result);
    }

    @Test
    public void testDateTimeTestAddsAttributes() {
        dateTimeTestController.dateTimeTest(model);
        verify(model, times(1)).addAttribute(eq("standardDate"), any());
        verify(model, times(1)).addAttribute(eq("localDateTime"), any());
        verify(model, times(1)).addAttribute(eq("localDate"), any());
        verify(model, times(1)).addAttribute(eq("timestamp"), any());
    }

    @Test
    public void testDateTimeTestReturnsString() {
        String result = dateTimeTestController.dateTimeTest(model);
        assertTrue(result instanceof String);
    }

    @Test
    public void testDateTimeTestReturnValue() {
        String result = dateTimeTestController.dateTimeTest(model);
        assertEquals("date/test", result);
    }
}
