package crm.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class PdfTest {

    private Pdf pdf;

    @BeforeEach
    public void setUp() {
        pdf = new Pdf();
    }

    @Test
    public void testPdfConstructor() {
        assertNotNull(pdf);
    }

    @Test
    public void testPdfBuilder() {
        Pdf builtPdf = Pdf.builder()
                .id(1L)
                .name("test.pdf")
                .content("Test content")
                .build();
        assertNotNull(builtPdf);
        assertEquals(1L, builtPdf.getId());
        assertEquals("test.pdf", builtPdf.getName());
        assertEquals("Test content", builtPdf.getContent());
    }

    @Test
    public void testSetAndGetId() {
        pdf.setId(1L);
        assertEquals(1L, pdf.getId());
    }

    @Test
    public void testSetAndGetName() {
        pdf.setName("document.pdf");
        assertEquals("document.pdf", pdf.getName());
    }

    @Test
    public void testSetAndGetContent() {
        pdf.setContent("Sample PDF content");
        assertEquals("Sample PDF content", pdf.getContent());
    }

    @Test
    public void testPdfWithNullName() {
        pdf.setName(null);
        assertNull(pdf.getName());
    }

    @Test
    public void testPdfWithNullContent() {
        pdf.setContent(null);
        assertNull(pdf.getContent());
    }

    @Test
    public void testPdfWithEmptyStrings() {
        pdf.setName("");
        pdf.setContent("");
        assertEquals("", pdf.getName());
        assertEquals("", pdf.getContent());
    }

    @Test
    public void testPdfIdBoundary() {
        pdf.setId(0L);
        assertEquals(0L, pdf.getId());
        pdf.setId(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, pdf.getId());
    }

    @Test
    public void testPdfAllArgsConstructor() {
        Pdf allArgsPdf = new Pdf(1L, "test.pdf", "Content");
        assertNotNull(allArgsPdf);
        assertEquals("test.pdf", allArgsPdf.getName());
        assertEquals("Content", allArgsPdf.getContent());
    }

    @Test
    public void testPdfEquality() {
        Pdf pdf1 = Pdf.builder().id(1L).name("doc.pdf").content("text").build();
        Pdf pdf2 = Pdf.builder().id(1L).name("doc.pdf").content("text").build();
        assertEquals(pdf1.getId(), pdf2.getId());
        assertEquals(pdf1.getName(), pdf2.getName());
    }
}
