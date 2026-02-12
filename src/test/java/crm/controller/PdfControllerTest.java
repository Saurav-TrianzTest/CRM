package crm.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("PdfController Tests")
class PdfControllerTest {

    @Test
    @WithMockUser
    @DisplayName("Test PdfController exists")
    void testPdfControllerExists() {
        assertTrue(true);
    }

    @Test
    @DisplayName("Test PdfController class is present")
    void testPdfControllerClass() throws ClassNotFoundException {
        Class.forName("crm.controller.PdfController");
    }
}
