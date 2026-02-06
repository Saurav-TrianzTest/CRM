package crm.view;

import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private StringWriter stringWriter;
    private PrintWriter printWriter;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        csvView = new CsvView();
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(printWriter);
    }

    @Test
    void testCsvViewCreation() {
        assertNotNull(csvView);
    }

    @Test
    void testBuildCsvDocumentWithValidUsers() throws Exception {
        Role role = new Role();
        role.setId(1);
        role.setName("ADMIN");

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .enabled(1)
                .role(role)
                .build();

        List<User> users = new ArrayList<>();
        users.add(user);

        Map<String, Object> model = new HashMap<>();
        model.put("users", users);

        csvView.buildCsvDocument(model, request, response);

        verify(response).setHeader("Content-Disposition", "attachment; filename=\"my-csv-file.csv\"");
    }

    @Test
    void testBuildCsvDocumentWithEmptyUsers() throws Exception {
        List<User> users = new ArrayList<>();

        Map<String, Object> model = new HashMap<>();
        model.put("users", users);

        assertDoesNotThrow(() -> csvView.buildCsvDocument(model, request, response));
    }

    @Test
    void testBuildCsvDocumentWithMultipleUsers() throws Exception {
        Role role = new Role();
        role.setId(1);
        role.setName("ADMIN");

        User user1 = User.builder()
                .id(1L)
                .username("user1")
                .email("user1@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password1")
                .enabled(1)
                .role(role)
                .build();

        User user2 = User.builder()
                .id(2L)
                .username("user2")
                .email("user2@example.com")
                .firstName("Jane")
                .lastName("Smith")
                .password("password2")
                .enabled(1)
                .role(role)
                .build();

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);

        Map<String, Object> model = new HashMap<>();
        model.put("users", users);

        csvView.buildCsvDocument(model, request, response);

        verify(response).setHeader("Content-Disposition", "attachment; filename=\"my-csv-file.csv\"");
    }
}
