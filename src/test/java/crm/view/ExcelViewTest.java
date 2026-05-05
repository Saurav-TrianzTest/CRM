package crm.view;

import crm.entity.Role;
import crm.entity.User;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExcelViewTest {

    private ExcelView excelView;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Map<String, Object> model;
    private Workbook workbook;

    @BeforeEach
    void setUp() {
        excelView = new ExcelView();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        model = new HashMap<>();
        workbook = new XSSFWorkbook();
    }

    @Test
    void constructor_shouldCreateExcelView() {
        // Assert
        assertNotNull(excelView);
    }

    @Test
    void buildExcelDocument_shouldCreateExcelSheet() throws Exception {
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
        
        // Act
        excelView.buildExcelDocument(model, workbook, request, response);
        
        // Assert
        verify(response).setHeader("Content-Disposition", "attachment; filename=\"my-xls-file.xls\"");
        assertNotNull(workbook.getSheet("User Detail"));
        assertEquals(2, workbook.getSheet("User Detail").getPhysicalNumberOfRows()); // Header + 1 user
    }

    @Test
    void buildExcelDocument_withMultipleUsers_shouldCreateMultipleRows() throws Exception {
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
        
        // Act
        excelView.buildExcelDocument(model, workbook, request, response);
        
        // Assert
        assertEquals(4, workbook.getSheet("User Detail").getPhysicalNumberOfRows()); // Header + 3 users
    }

    @Test
    void excelView_shouldExtendAbstractXlsView() {
        // Assert
        assertTrue(org.springframework.web.servlet.view.document.AbstractXlsView.class
            .isAssignableFrom(ExcelView.class));
    }
}
