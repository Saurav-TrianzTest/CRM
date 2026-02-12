package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pdf Entity Tests")
class PdfTest {

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = Pdf.builder()
                .id(1L)
                .name("Test PDF")
                .content("PDF content data")
                .build();
    }

    @Test
    @DisplayName("Test Pdf builder creates valid instance")
    void testPdfBuilder() {
        assertNotNull(pdf);
        assertEquals(1L, pdf.getId());
        assertEquals("Test PDF", pdf.getName());
        assertEquals("PDF content data", pdf.getContent());
    }

    @Test
    @DisplayName("Test Pdf no-args constructor")
    void testNoArgsConstructor() {
        Pdf emptyPdf = new Pdf();
        assertNotNull(emptyPdf);
        assertNull(emptyPdf.getId());
        assertNull(emptyPdf.getName());
        assertNull(emptyPdf.getContent());
    }

    @Test
    @DisplayName("Test Pdf all-args constructor")
    void testAllArgsConstructor() {
        Pdf newPdf = new Pdf(2L, "New PDF", "New content");
        assertNotNull(newPdf);
        assertEquals(2L, newPdf.getId());
        assertEquals("New PDF", newPdf.getName());
        assertEquals("New content", newPdf.getContent());
    }

    @Test
    @DisplayName("Test Pdf getters and setters")
    void testGettersAndSetters() {
        Pdf testPdf = new Pdf();
        testPdf.setId(3L);
        testPdf.setName("Updated PDF");
        testPdf.setContent("Updated content");

        assertEquals(3L, testPdf.getId());
        assertEquals("Updated PDF", testPdf.getName());
        assertEquals("Updated content", testPdf.getContent());
    }

    @Test
    @DisplayName("Test Pdf with null values")
    void testPdfWithNullValues() {
        Pdf nullPdf = Pdf.builder()
                .name(null)
                .content(null)
                .build();

        assertNotNull(nullPdf);
        assertNull(nullPdf.getName());
        assertNull(nullPdf.getContent());
    }

    @Test
    @DisplayName("Test Pdf name minimum size")
    void testPdfNameMinimumSize() {
        pdf.setName("AB");
        assertEquals("AB", pdf.getName());
        assertEquals(2, pdf.getName().length());
    }

    @Test
    @DisplayName("Test Pdf name with single character")
    void testPdfNameWithSingleCharacter() {
        pdf.setName("A");
        assertEquals("A", pdf.getName());
    }

    @Test
    @DisplayName("Test Pdf with empty name")
    void testPdfWithEmptyName() {
        pdf.setName("");
        assertEquals("", pdf.getName());
    }

    @Test
    @DisplayName("Test Pdf with long name")
    void testPdfWithLongName() {
        String longName = "A".repeat(255);
        pdf.setName(longName);
        assertEquals(longName, pdf.getName());
        assertEquals(255, pdf.getName().length());
    }

    @Test
    @DisplayName("Test Pdf content with special characters")
    void testPdfContentWithSpecialCharacters() {
        String specialContent = "Content with special chars: !@#$%^&*()_+-=[]{}|;:',.<>?";
        pdf.setContent(specialContent);
        assertEquals(specialContent, pdf.getContent());
    }

    @Test
    @DisplayName("Test Pdf content with empty string")
    void testPdfContentWithEmptyString() {
        pdf.setContent("");
        assertEquals("", pdf.getContent());
    }

    @Test
    @DisplayName("Test Pdf content with large text")
    void testPdfContentWithLargeText() {
        String largeContent = "X".repeat(10000);
        pdf.setContent(largeContent);
        assertEquals(largeContent, pdf.getContent());
        assertEquals(10000, pdf.getContent().length());
    }

    @Test
    @DisplayName("Test Pdf content transient nature")
    void testPdfContentTransient() {
        // Content field is marked as @Transient, so it won't be persisted
        pdf.setContent("Transient content");
        assertEquals("Transient content", pdf.getContent());
    }

    @Test
    @DisplayName("Test Pdf equals and hashCode")
    void testEqualsAndHashCode() {
        Pdf pdf1 = Pdf.builder()
                .id(1L)
                .name("PDF1")
                .content("Content1")
                .build();

        Pdf pdf2 = Pdf.builder()
                .id(1L)
                .name("PDF1")
                .content("Content1")
                .build();

        assertEquals(pdf1, pdf2);
        assertEquals(pdf1.hashCode(), pdf2.hashCode());
    }

    @Test
    @DisplayName("Test Pdf not equals with different id")
    void testNotEqualsWithDifferentId() {
        Pdf pdf1 = Pdf.builder()
                .id(1L)
                .name("PDF1")
                .build();

        Pdf pdf2 = Pdf.builder()
                .id(2L)
                .name("PDF1")
                .build();

        assertNotEquals(pdf1, pdf2);
    }

    @Test
    @DisplayName("Test Pdf toString")
    void testToString() {
        String pdfString = pdf.toString();
        assertNotNull(pdfString);
        assertTrue(pdfString.contains("Test PDF"));
    }

    @Test
    @DisplayName("Test Pdf with numeric characters in name")
    void testPdfNameWithNumbers() {
        pdf.setName("PDF123");
        assertEquals("PDF123", pdf.getName());
    }

    @Test
    @DisplayName("Test Pdf with whitespace in name")
    void testPdfNameWithWhitespace() {
        pdf.setName("Test PDF Document");
        assertEquals("Test PDF Document", pdf.getName());
    }

    @Test
    @DisplayName("Test Pdf content with newlines")
    void testPdfContentWithNewlines() {
        String contentWithNewlines = "Line 1\nLine 2\nLine 3";
        pdf.setContent(contentWithNewlines);
        assertEquals(contentWithNewlines, pdf.getContent());
        assertTrue(pdf.getContent().contains("\n"));
    }

    @Test
    @DisplayName("Test Pdf content with tabs")
    void testPdfContentWithTabs() {
        String contentWithTabs = "Column1\tColumn2\tColumn3";
        pdf.setContent(contentWithTabs);
        assertEquals(contentWithTabs, pdf.getContent());
        assertTrue(pdf.getContent().contains("\t"));
    }
}
