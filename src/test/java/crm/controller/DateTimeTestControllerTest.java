package crm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DateTimeTestControllerTest {

    private DateTimeTestController controller;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new DateTimeTestController();
    }

    @Test
    void testDateTimeTest() {
        String result = controller.dateTimeTest(model);

        assertEquals("date/test", result);
        verify(model, times(1)).addAttribute(eq("standardDate"), any());
        verify(model, times(1)).addAttribute(eq("localDateTime"), any());
        verify(model, times(1)).addAttribute(eq("localDate"), any());
        verify(model, times(1)).addAttribute(eq("timestamp"), any());
    }

    @Test
    void testDateTimeTestReturnsCorrectView() {
        String viewName = controller.dateTimeTest(model);
        assertNotNull(viewName);
        assertEquals("date/test", viewName);
    }

    @Test
    void testDateTimeTestAddsAttributes() {
        controller.dateTimeTest(model);
        verify(model, atLeast(4)).addAttribute(anyString(), any());
    }
}
