package crm.view;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PdfViewTest {

    private PdfView pdfView;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pdfView = new PdfView();
    }

    @Test
    void testPdfViewCreation() {
        assertNotNull(pdfView);
    }

    @Test
    void testBuildPdfDocumentWithValidUsers() throws Exception {
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

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        pdfView.buildPdfDocument(model, document, writer, request, response);

        verify(response).setHeader("Content-Disposition", "attachment; filename=\"my-pdf-file.pdf\"");

        document.close();
    }

    @Test
    void testBuildPdfDocumentWithMultipleUsers() throws Exception {
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

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();

        pdfView.buildPdfDocument(model, document, writer, request, response);

        verify(response).setHeader("Content-Disposition", "attachment; filename=\"my-pdf-file.pdf\"");

        document.close();
    }
}
