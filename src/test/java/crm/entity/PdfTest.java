package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PdfTest {

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = new Pdf();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(pdf);
    }

    @Test
    void testBuilderPattern() {
        Pdf builtPdf = Pdf.builder()
                .id(1L)
                .name("Test PDF")
                .content("PDF Content")
                .build();

        assertNotNull(builtPdf);
        assertEquals(1L, builtPdf.getId());
        assertEquals("Test PDF", builtPdf.getName());
        assertEquals("PDF Content", builtPdf.getContent());
    }

    @Test
    void testAllArgsConstructor() {
        Pdf pdf = new Pdf(1L, "Report", "Report Content");

        assertEquals(1L, pdf.getId());
        assertEquals("Report", pdf.getName());
        assertEquals("Report Content", pdf.getContent());
    }

    @Test
    void testGettersAndSetters() {
        pdf.setId(1L);
        pdf.setName("Invoice");
        pdf.setContent("Invoice details...");

        assertEquals(1L, pdf.getId());
        assertEquals("Invoice", pdf.getName());
        assertEquals("Invoice details...", pdf.getContent());
    }

    @Test
    void testNullValues() {
        pdf.setId(null);
        pdf.setName(null);
        pdf.setContent(null);

        assertNull(pdf.getId());
        assertNull(pdf.getName());
        assertNull(pdf.getContent());
    }

    @Test
    void testMinimumNameLength() {
        String minName = "AB";
        pdf.setName(minName);

        assertEquals(minName, pdf.getName());
        assertTrue(pdf.getName().length() >= 2);
    }

    @Test
    void testLongName() {
        String longName = "A".repeat(255);
        pdf.setName(longName);

        assertEquals(longName, pdf.getName());
    }

    @Test
    void testEmptyContent() {
        pdf.setContent("");

        assertEquals("", pdf.getContent());
    }

    @Test
    void testLargeContent() {
        String largeContent = "Lorem ipsum ".repeat(1000);
        pdf.setContent(largeContent);

        assertEquals(largeContent, pdf.getContent());
    }

    @Test
    void testEquality() {
        Pdf pdf1 = Pdf.builder()
                .id(1L)
                .name("Test")
                .content("Content")
                .build();

        Pdf pdf2 = Pdf.builder()
                .id(1L)
                .name("Test")
                .content("Content")
                .build();

        assertEquals(pdf1, pdf2);
    }

    @Test
    void testHashCode() {
        pdf.setId(1L);
        pdf.setName("Test");

        int hashCode1 = pdf.hashCode();
        int hashCode2 = pdf.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testToString() {
        pdf.setId(1L);
        pdf.setName("Report");

        String toString = pdf.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("Report"));
    }
}
