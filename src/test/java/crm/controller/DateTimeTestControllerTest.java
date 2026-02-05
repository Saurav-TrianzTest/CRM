package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DateTimeTestControllerTest {

    private DateTimeTestController controller;
    private Model model;

    @BeforeEach
    public void setUp() {
        controller = new DateTimeTestController();
        model = mock(Model.class);
    }

    @Test
    public void testDateTimeTestControllerCreation() {
        assertNotNull(controller);
    }

    @Test
    public void testDateTimeTest() {
        String result = controller.dateTimeTest(model);
        assertEquals("date/test", result);
    }

    @Test
    public void testDateTimeTestAddsAttributes() {
        controller.dateTimeTest(model);
        verify(model, times(1)).addAttribute(eq("standardDate"), any());
        verify(model, times(1)).addAttribute(eq("localDateTime"), any());
        verify(model, times(1)).addAttribute(eq("localDate"), any());
        verify(model, times(1)).addAttribute(eq("timestamp"), any());
    }

    @Test
    public void testDateTimeTestReturnsCorrectView() {
        String view = controller.dateTimeTest(model);
        assertNotNull(view);
        assertEquals("date/test", view);
    }

    @Test
    public void testDateTimeTestWithNullModel() {
        assertThrows(NullPointerException.class, () -> controller.dateTimeTest(null));
    }
}
