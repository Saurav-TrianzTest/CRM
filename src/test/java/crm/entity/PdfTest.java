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
    void testDefaultConstructor() {
        assertNotNull(pdf);
        assertNull(pdf.getId());
        assertNull(pdf.getName());
        assertNull(pdf.getContent());
    }

    @Test
    void testBuilderConstructor() {
        Pdf pdfWithBuilder = Pdf.builder()
                .id(1L)
                .name("test.pdf")
                .content("Test content")
                .build();

        assertEquals(1L, pdfWithBuilder.getId());
        assertEquals("test.pdf", pdfWithBuilder.getName());
        assertEquals("Test content", pdfWithBuilder.getContent());
    }

    @Test
    void testAllArgsConstructor() {
        Pdf pdfWithArgs = new Pdf(1L, "document.pdf", "Document content");

        assertEquals(1L, pdfWithArgs.getId());
        assertEquals("document.pdf", pdfWithArgs.getName());
        assertEquals("Document content", pdfWithArgs.getContent());
    }

    @Test
    void testSetAndGetId() {
        pdf.setId(10L);
        assertEquals(10L, pdf.getId());
    }

    @Test
    void testSetAndGetName() {
        pdf.setName("report.pdf");
        assertEquals("report.pdf", pdf.getName());
    }

    @Test
    void testSetAndGetContent() {
        pdf.setContent("PDF content here");
        assertEquals("PDF content here", pdf.getContent());
    }

    @Test
    void testSetNameWithNull() {
        pdf.setName(null);
        assertNull(pdf.getName());
    }

    @Test
    void testSetNameWithEmptyString() {
        pdf.setName("");
        assertEquals("", pdf.getName());
    }

    @Test
    void testSetContentWithNull() {
        pdf.setContent(null);
        assertNull(pdf.getContent());
    }

    @Test
    void testSetIdWithNull() {
        pdf.setId(null);
        assertNull(pdf.getId());
    }

    @Test
    void testTransientContent() {
        Pdf pdfObj = Pdf.builder()
                .id(1L)
                .name("test.pdf")
                .content("This is transient")
                .build();

        assertNotNull(pdfObj.getContent());
        assertEquals("This is transient", pdfObj.getContent());
    }
}
