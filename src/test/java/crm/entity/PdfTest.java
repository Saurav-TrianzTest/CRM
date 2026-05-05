package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class PdfTest {

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = new Pdf();
    }

    @Test
    void constructor_shouldCreatePdf() {
        // Assert
        assertNotNull(pdf);
    }

    @Test
    void builder_shouldCreatePdfWithAllFields() {
        // Act
        Pdf built = Pdf.builder()
                .id(1L)
                .name("test.pdf")
                .content("Test content")
                .build();
        
        // Assert
        assertNotNull(built);
        assertEquals(1L, built.getId());
        assertEquals("test.pdf", built.getName());
        assertEquals("Test content", built.getContent());
    }

    @Test
    void setId_shouldSetId() {
        // Act
        pdf.setId(1L);
        
        // Assert
        assertEquals(1L, pdf.getId());
    }

    @Test
    void setName_shouldSetName() {
        // Act
        pdf.setName("document.pdf");
        
        // Assert
        assertEquals("document.pdf", pdf.getName());
    }

    @Test
    void setContent_shouldSetContent() {
        // Act
        pdf.setContent("PDF content here");
        
        // Assert
        assertEquals("PDF content here", pdf.getContent());
    }

    @Test
    void getId_shouldReturnId() {
        // Arrange
        pdf.setId(5L);
        
        // Act
        Long id = pdf.getId();
        
        // Assert
        assertEquals(5L, id);
    }

    @Test
    void getName_shouldReturnName() {
        // Arrange
        pdf.setName("report.pdf");
        
        // Act
        String name = pdf.getName();
        
        // Assert
        assertEquals("report.pdf", name);
    }

    @Test
    void getContent_shouldReturnContent() {
        // Arrange
        pdf.setContent("Content");
        
        // Act
        String content = pdf.getContent();
        
        // Assert
        assertEquals("Content", content);
    }

    @Test
    void allArgsConstructor_shouldCreatePdfWithAllFields() {
        // Act
        Pdf pdf = new Pdf(1L, "test.pdf", "Content");
        
        // Assert
        assertNotNull(pdf);
        assertEquals(1L, pdf.getId());
        assertEquals("test.pdf", pdf.getName());
        assertEquals("Content", pdf.getContent());
    }

    @Test
    void noArgsConstructor_shouldCreateEmptyPdf() {
        // Act
        Pdf pdf = new Pdf();
        
        // Assert
        assertNotNull(pdf);
    }
}
