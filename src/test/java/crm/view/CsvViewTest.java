package crm.view;

import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CsvViewTest {

    private CsvView csvView;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Map<String, Object> model;

    @BeforeEach
    void setUp() {
        csvView = new CsvView();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        model = new HashMap<>();
    }

    @Test
    void constructor_shouldCreateCsvView() {
        // Assert
        assertNotNull(csvView);
    }

    @Test
    void buildCsvDocument_shouldWriteCsvData() throws Exception {
        // Arrange
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
        
        User user = User.builder()
                .id(1L)
                .username("testuser")
                .firstName("John")
                .lastName("Doe")
                .email("test@example.com")
                .password("password")
                .enabled(1)
                .role(role)
                .build();
        
        List<User> users = new ArrayList<>();
        users.add(user);
        model.put("users", users);
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        
        when(response.getWriter()).thenReturn(printWriter);
        
        // Act
        csvView.buildCsvDocument(model, request, response);
        
        // Assert
        verify(response).setHeader("Content-Disposition", "attachment; filename=\"my-csv-file.csv\"");
        verify(response).getWriter();
    }

    @Test
    void buildCsvDocument_withEmptyUserList_shouldHandleGracefully() throws Exception {
        // Arrange
        List<User> users = new ArrayList<>();
        model.put("users", users);
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        
        when(response.getWriter()).thenReturn(printWriter);
        
        // Act & Assert
        assertDoesNotThrow(() -> csvView.buildCsvDocument(model, request, response));
    }

    @Test
    void csvView_shouldExtendAbstractCsvView() {
        // Assert
        assertTrue(AbstractCsvView.class.isAssignableFrom(CsvView.class));
    }
}
