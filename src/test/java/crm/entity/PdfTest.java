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
    public void testPdfCreation() {
        assertNotNull(pdf);
    }

    @Test
    public void testBuilderPattern() {
        Pdf pdf = Pdf.builder()
                .id(1L)
                .name("test.pdf")
                .content("Sample PDF content")
                .build();

        assertNotNull(pdf);
        assertEquals("test.pdf", pdf.getName());
        assertEquals("Sample PDF content", pdf.getContent());
    }

    @Test
    public void testSetAndGetId() {
        pdf.setId(10L);
        assertEquals(10L, pdf.getId());
    }

    @Test
    public void testSetAndGetName() {
        pdf.setName("document.pdf");
        assertEquals("document.pdf", pdf.getName());
    }

    @Test
    public void testSetAndGetContent() {
        pdf.setContent("PDF content text");
        assertEquals("PDF content text", pdf.getContent());
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
    public void testPdfEquality() {
        Pdf p1 = Pdf.builder()
                .id(1L)
                .name("test.pdf")
                .content("content")
                .build();

        Pdf p2 = Pdf.builder()
                .id(1L)
                .name("test.pdf")
                .content("content")
                .build();

        assertEquals(p1, p2);
    }

    @Test
    public void testPdfHashCode() {
        pdf.setId(1L);
        pdf.setName("test.pdf");
        int hashCode = pdf.hashCode();
        assertTrue(hashCode != 0);
    }

    @Test
    public void testPdfToString() {
        pdf.setId(1L);
        pdf.setName("sample.pdf");
        pdf.setContent("Sample content");
        String str = pdf.toString();
        assertNotNull(str);
        assertTrue(str.contains("sample.pdf"));
    }
}
