package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PdfTest {

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = new Pdf();
    }

    @Test
    void testPdfCreation() {
        assertNotNull(pdf);
    }

    @Test
    void testPdfBuilder() {
        Pdf builtPdf = Pdf.builder()
                .id(1L)
                .name("TestPdf")
                .content("Sample PDF content")
                .build();

        assertNotNull(builtPdf);
        assertEquals(1L, builtPdf.getId());
        assertEquals("TestPdf", builtPdf.getName());
        assertEquals("Sample PDF content", builtPdf.getContent());
    }

    @Test
    void testSetAndGetId() {
        pdf.setId(1L);
        assertEquals(1L, pdf.getId());
    }

    @Test
    void testSetAndGetName() {
        pdf.setName("TestPdf");
        assertEquals("TestPdf", pdf.getName());
    }

    @Test
    void testSetAndGetContent() {
        pdf.setContent("Sample PDF content");
        assertEquals("Sample PDF content", pdf.getContent());
    }

    @Test
    void testPdfWithNullName() {
        pdf.setName(null);
        assertNull(pdf.getName());
    }

    @Test
    void testPdfWithNullContent() {
        pdf.setContent(null);
        assertNull(pdf.getContent());
    }

    @Test
    void testPdfWithEmptyContent() {
        pdf.setContent("");
        assertEquals("", pdf.getContent());
    }

    @Test
    void testPdfEquality() {
        Pdf pdf1 = Pdf.builder()
                .id(1L)
                .name("TestPdf")
                .content("Content")
                .build();

        Pdf pdf2 = Pdf.builder()
                .id(1L)
                .name("TestPdf")
                .content("Content")
                .build();

        assertEquals(pdf1, pdf2);
    }

    @Test
    void testAllArgsConstructor() {
        Pdf pdf = new Pdf(1L, "TestPdf", "Sample content");

        assertNotNull(pdf);
        assertEquals(1L, pdf.getId());
        assertEquals("TestPdf", pdf.getName());
        assertEquals("Sample content", pdf.getContent());
    }

    @Test
    void testNoArgsConstructor() {
        Pdf pdf = new Pdf();
        assertNotNull(pdf);
    }

    @Test
    void testPdfWithLongContent() {
        String longContent = "This is a very long PDF content for testing purposes. ".repeat(100);
        pdf.setContent(longContent);
        assertEquals(longContent, pdf.getContent());
    }
}
