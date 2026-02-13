package crm.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MyErrorControllerTest {

    @InjectMocks
    private MyErrorController myErrorController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(myErrorController).build();
    }

    @Test
    void testConstructor() {
        MyErrorController controller = new MyErrorController();
        assertNotNull(controller);
    }

    @Test
    void testError_GetRequest_Success() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().isOk())
                .andExpect(content().string("Error handling"));
    }

    @Test
    void testError_PostRequest_Success() throws Exception {
        mockMvc.perform(post("/error"))
                .andExpect(status().isOk())
                .andExpect(content().string("Error handling"));
    }

    @Test
    void testError_ReturnsCorrectMessage() {
        String result = myErrorController.error();
        assertEquals("Error handling", result);
    }

    @Test
    void testError_MessageNotNull() {
        String result = myErrorController.error();
        assertNotNull(result);
    }

    @Test
    void testError_MessageNotEmpty() {
        String result = myErrorController.error();
        assertFalse(result.isEmpty());
    }

    @Test
    void testError_ExactMessageContent() {
        String result = myErrorController.error();
        assertEquals("Error handling", result);
        assertTrue(result.contains("Error"));
        assertTrue(result.contains("handling"));
    }

    @Test
    void testError_MultipleRequests() throws Exception {
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(get("/error"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Error handling"));
        }
    }

    @Test
    void testError_ConsistentResponse() throws Exception {
        String response1 = mockMvc.perform(get("/error"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String response2 = mockMvc.perform(get("/error"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(response1, response2);
    }

    @Test
    void testError_ContentType() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"));
    }

    @Test
    void testError_HttpStatusIsOk() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().isOk());
    }

    @Test
    void testError_ResponseLength() {
        String result = myErrorController.error();
        assertEquals(14, result.length());
    }

    @Test
    void testError_WithHeaders() throws Exception {
        mockMvc.perform(get("/error")
                        .header("User-Agent", "Test Agent")
                        .header("Accept", "text/plain"))
                .andExpect(status().isOk())
                .andExpect(content().string("Error handling"));
    }

    @Test
    void testError_WithQueryParameters() throws Exception {
        mockMvc.perform(get("/error")
                        .param("code", "404")
                        .param("message", "Not Found"))
                .andExpect(status().isOk())
                .andExpect(content().string("Error handling"));
    }

    @Test
    void testError_WithAcceptHeader() throws Exception {
        mockMvc.perform(get("/error")
                        .header("Accept", "*/*"))
                .andExpect(status().isOk())
                .andExpect(content().string("Error handling"));
    }

    @Test
    void testError_MethodInvocationDirectly() {
        String result1 = myErrorController.error();
        String result2 = myErrorController.error();
        String result3 = myErrorController.error();

        assertEquals(result1, result2);
        assertEquals(result2, result3);
        assertEquals("Error handling", result1);
    }

    @Test
    void testError_MultipleDirectInvocations() {
        for (int i = 0; i < 10; i++) {
            String result = myErrorController.error();
            assertEquals("Error handling", result);
        }
    }

    @Test
    void testError_WithDifferentMethods() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().isOk())
                .andExpect(content().string("Error handling"));

        mockMvc.perform(post("/error"))
                .andExpect(status().isOk())
                .andExpect(content().string("Error handling"));
    }

    @Test
    void testError_ResponseNotHtml() {
        String result = myErrorController.error();
        assertFalse(result.contains("<html>"));
        assertFalse(result.contains("<body>"));
        assertFalse(result.contains("</html>"));
    }

    @Test
    void testError_ResponseIsPlainText() {
        String result = myErrorController.error();
        assertTrue(result.matches("[A-Za-z ]+"));
    }

    @Test
    void testError_RapidSuccessiveCalls() throws Exception {
        for (int i = 0; i < 20; i++) {
            mockMvc.perform(get("/error"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Error handling"));
        }
    }

    @Test
    void testError_WithContentType() throws Exception {
        mockMvc.perform(get("/error")
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("Error handling"));
    }

    @Test
    void testError_WithCharacterEncoding() throws Exception {
        mockMvc.perform(get("/error")
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string("Error handling"));
    }

    @Test
    void testError_CaseInsensitiveCheck() {
        String result = myErrorController.error();
        assertTrue(result.equalsIgnoreCase("error handling"));
    }

    @Test
    void testError_StartsWithError() {
        String result = myErrorController.error();
        assertTrue(result.startsWith("Error"));
    }

    @Test
    void testError_EndsWithHandling() {
        String result = myErrorController.error();
        assertTrue(result.endsWith("handling"));
    }

    @Test
    void testError_ContainsSpace() {
        String result = myErrorController.error();
        assertTrue(result.contains(" "));
    }

    @Test
    void testError_WordCount() {
        String result = myErrorController.error();
        String[] words = result.split(" ");
        assertEquals(2, words.length);
    }

    @Test
    void testError_NoSpecialCharacters() {
        String result = myErrorController.error();
        assertFalse(result.matches(".*[!@#$%^&*()_+={}\\[\\]:;\"'<>,.?/\\\\|-].*"));
    }

    @Test
    void testError_NoNumbers() {
        String result = myErrorController.error();
        assertFalse(result.matches(".*\\d.*"));
    }

    @Test
    void testError_AlphabeticalCharacters() {
        String result = myErrorController.error();
        assertTrue(result.replaceAll(" ", "").matches("[A-Za-z]+"));
    }

    @Test
    void testError_ImplementsErrorController() {
        assertTrue(myErrorController instanceof org.springframework.boot.web.servlet.error.ErrorController);
    }
}
