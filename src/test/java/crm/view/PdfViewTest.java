package crm.view;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import crm.entity.Role;
import crm.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PdfViewTest {

    private PdfView pdfView;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Map<String, Object> model;

    @BeforeEach
    void setUp() {
        pdfView = new PdfView();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        model = new HashMap<>();
    }

    @Test
    void constructor_shouldCreatePdfView() {
        // Assert
        assertNotNull(pdfView);
    }

    @Test
    void buildPdfDocument_shouldCreatePdfDocument() throws Exception {
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
        
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();
        
        // Act
        pdfView.buildPdfDocument(model, document, writer, request, response);
        
        // Assert
        verify(response).setHeader("Content-Disposition", "attachment; filename=\"my-pdf-file.pdf\"");
    }

    @Test
    void buildPdfDocument_withMultipleUsers_shouldAddAllToTable() throws Exception {
        // Arrange
        Role role = new Role();
        role.setId(1);
        role.setName("ROLE_USER");
        
        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 3; i++) {
            User user = User.builder()
                    .id((long) i)
                    .username("user" + i)
                    .firstName("First" + i)
                    .lastName("Last" + i)
                    .email("user" + i + "@example.com")
                    .password("password")
                    .enabled(1)
                    .role(role)
                    .build();
            users.add(user);
        }
        model.put("users", users);
        
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        document.open();
        
        // Act & Assert
        assertDoesNotThrow(() -> pdfView.buildPdfDocument(model, document, writer, request, response));
    }

    @Test
    void pdfView_shouldExtendAbstractPdfView() {
        // Assert
        assertTrue(AbstractPdfView.class.isAssignableFrom(PdfView.class));
    }
}
