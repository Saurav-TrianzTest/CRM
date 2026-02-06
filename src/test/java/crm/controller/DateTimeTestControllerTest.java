package crm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DateTimeTestControllerTest {

    private DateTimeTestController dateTimeTestController;
    private Model model;

    @BeforeEach
    void setUp() {
        dateTimeTestController = new DateTimeTestController();
        model = mock(Model.class);
    }

    @Test
    void testDateTimeTest() {
        String result = dateTimeTestController.dateTimeTest(model);

        verify(model, times(1)).addAttribute(eq("standardDate"), any());
        verify(model, times(1)).addAttribute(eq("localDateTime"), any());
        verify(model, times(1)).addAttribute(eq("localDate"), any());
        verify(model, times(1)).addAttribute(eq("timestamp"), any());

        assertNotNull(result);
        assertEquals("date/test", result);
    }

    @Test
    void testDateTimeTestReturnsCorrectView() {
        String result = dateTimeTestController.dateTimeTest(model);
        assertEquals("date/test", result);
    }

    @Test
    void testDateTimeTestAddsAllAttributes() {
        dateTimeTestController.dateTimeTest(model);
        verify(model, times(4)).addAttribute(anyString(), any());
    }
}
