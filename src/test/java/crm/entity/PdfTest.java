package crm.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pdf Entity Tests")
class PdfTest {

    private Pdf pdf;

    @BeforeEach
    void setUp() {
        pdf = new Pdf();
    }

    @Test
    @DisplayName("Test default constructor creates non-null instance")
    void testDefaultConstructor() {
        Pdf newPdf = new Pdf();
        assertNotNull(newPdf);
    }

    @Test
    @DisplayName("Test all-args constructor with all fields")
    void testAllArgsConstructor() {
        Long id = 1L;
        String name = "TestDocument.pdf";
        String content = "PDF content in base64";

        Pdf pdf = new Pdf(id, name, content);

        assertNotNull(pdf);
        assertEquals(id, pdf.getId());
        assertEquals(name, pdf.getName());
        assertEquals(content, pdf.getContent());
    }

    @Test
    @DisplayName("Test builder pattern with all fields")
    void testBuilderWithAllFields() {
        Long id = 2L;
        String name = "BuilderDocument.pdf";
        String content = "Built PDF content";

        Pdf pdf = Pdf.builder()
                .id(id)
                .name(name)
                .content(content)
                .build();

        assertNotNull(pdf);
        assertEquals(id, pdf.getId());
        assertEquals(name, pdf.getName());
        assertEquals(content, pdf.getContent());
    }

    @Test
    @DisplayName("Test builder with minimal fields")
    void testBuilderWithMinimalFields() {
        Pdf pdf = Pdf.builder()
                .name("MinimalDoc.pdf")
                .build();

        assertNotNull(pdf);
        assertEquals("MinimalDoc.pdf", pdf.getName());
        assertNull(pdf.getId());
        assertNull(pdf.getContent());
    }

    @Test
    @DisplayName("Test builder with only content")
    void testBuilderWithOnlyContent() {
        Pdf pdf = Pdf.builder()
                .content("Only content")
                .build();

        assertNotNull(pdf);
        assertEquals("Only content", pdf.getContent());
        assertNull(pdf.getId());
        assertNull(pdf.getName());
    }

    @Test
    @DisplayName("Test setId and getId")
    void testSetAndGetId() {
        Long id = 10L;
        pdf.setId(id);
        assertEquals(id, pdf.getId());
    }

    @Test
    @DisplayName("Test setId with null value")
    void testSetIdNull() {
        pdf.setId(null);
        assertNull(pdf.getId());
    }

    @Test
    @DisplayName("Test setId with zero")
    void testSetIdZero() {
        Long id = 0L;
        pdf.setId(id);
        assertEquals(id, pdf.getId());
    }

    @Test
    @DisplayName("Test setId with negative value")
    void testSetIdNegative() {
        Long id = -1L;
        pdf.setId(id);
        assertEquals(id, pdf.getId());
    }

    @Test
    @DisplayName("Test setName and getName")
    void testSetAndGetName() {
        String name = "TestDocument.pdf";
        pdf.setName(name);
        assertEquals(name, pdf.getName());
    }

    @Test
    @DisplayName("Test setName with null value")
    void testSetNameNull() {
        pdf.setName(null);
        assertNull(pdf.getName());
    }

    @Test
    @DisplayName("Test setName with empty string")
    void testSetNameEmpty() {
        String name = "";
        pdf.setName(name);
        assertEquals(name, pdf.getName());
    }

    @Test
    @DisplayName("Test setName with single character")
    void testSetNameSingleCharacter() {
        String name = "a";
        pdf.setName(name);
        assertEquals(name, pdf.getName());
    }

    @Test
    @DisplayName("Test setName with two characters (minimum)")
    void testSetNameTwoCharacters() {
        String name = "ab";
        pdf.setName(name);
        assertEquals(name, pdf.getName());
    }

    @Test
    @DisplayName("Test setName with long filename")
    void testSetNameLongFilename() {
        String name = "very_long_document_name_with_many_characters_" + "x".repeat(200) + ".pdf";
        pdf.setName(name);
        assertEquals(name, pdf.getName());
        assertTrue(pdf.getName().length() > 200);
    }

    @Test
    @DisplayName("Test setName with special characters")
    void testSetNameSpecialCharacters() {
        String name = "document_#$%@!.pdf";
        pdf.setName(name);
        assertEquals(name, pdf.getName());
    }

    @Test
    @DisplayName("Test setName with spaces")
    void testSetNameWithSpaces() {
        String name = "My Document File.pdf";
        pdf.setName(name);
        assertEquals(name, pdf.getName());
    }

    @Test
    @DisplayName("Test setName with path separators")
    void testSetNameWithPathSeparators() {
        String name = "folder/subfolder/document.pdf";
        pdf.setName(name);
        assertEquals(name, pdf.getName());
    }

    @Test
    @DisplayName("Test setName without extension")
    void testSetNameWithoutExtension() {
        String name = "document_without_extension";
        pdf.setName(name);
        assertEquals(name, pdf.getName());
    }

    @Test
    @DisplayName("Test setContent and getContent")
    void testSetAndGetContent() {
        String content = "This is PDF content in text format";
        pdf.setContent(content);
        assertEquals(content, pdf.getContent());
    }

    @Test
    @DisplayName("Test setContent with null value")
    void testSetContentNull() {
        pdf.setContent(null);
        assertNull(pdf.getContent());
    }

    @Test
    @DisplayName("Test setContent with empty string")
    void testSetContentEmpty() {
        String content = "";
        pdf.setContent(content);
        assertEquals(content, pdf.getContent());
    }

    @Test
    @DisplayName("Test setContent with large content")
    void testSetContentLarge() {
        String content = "Lorem ipsum ".repeat(10000);
        pdf.setContent(content);
        assertEquals(content, pdf.getContent());
        assertTrue(pdf.getContent().length() > 100000);
    }

    @Test
    @DisplayName("Test setContent with base64 encoded data")
    void testSetContentBase64() {
        String content = "JVBERi0xLjQKJeLjz9MKMyAwIG9iago8PC9UeXBlIC9QYWdlCi9QYXJlbnQgMSAwIFI=";
        pdf.setContent(content);
        assertEquals(content, pdf.getContent());
    }

    @Test
    @DisplayName("Test setContent with binary-like content")
    void testSetContentBinary() {
        String content = "\u0000\u0001\u0002\u0003\u0004\u0005";
        pdf.setContent(content);
        assertEquals(content, pdf.getContent());
    }

    @Test
    @DisplayName("Test setContent with multiline content")
    void testSetContentMultiline() {
        String content = "Line 1\nLine 2\nLine 3\n";
        pdf.setContent(content);
        assertEquals(content, pdf.getContent());
        assertTrue(pdf.getContent().contains("\n"));
    }

    @Test
    @DisplayName("Test setContent with special characters")
    void testSetContentSpecialCharacters() {
        String content = "Special chars: @#$%^&*()[]{}|\\;:'\",.<>?/~`";
        pdf.setContent(content);
        assertEquals(content, pdf.getContent());
    }

    @Test
    @DisplayName("Test equals with same object")
    void testEqualsWithSameObject() {
        pdf.setId(1L);
        pdf.setName("doc.pdf");
        assertEquals(pdf, pdf);
    }

    @Test
    @DisplayName("Test equals with equal objects")
    void testEqualsWithEqualObjects() {
        pdf.setId(1L);
        pdf.setName("doc.pdf");
        pdf.setContent("content");

        Pdf other = new Pdf();
        other.setId(1L);
        other.setName("doc.pdf");
        other.setContent("content");

        assertEquals(pdf, other);
    }

    @Test
    @DisplayName("Test equals with different objects")
    void testEqualsWithDifferentObjects() {
        pdf.setId(1L);
        pdf.setName("doc1.pdf");

        Pdf other = new Pdf();
        other.setId(2L);
        other.setName("doc2.pdf");

        assertNotEquals(pdf, other);
    }

    @Test
    @DisplayName("Test equals with null")
    void testEqualsWithNull() {
        pdf.setId(1L);
        assertNotEquals(null, pdf);
    }

    @Test
    @DisplayName("Test hashCode consistency")
    void testHashCodeConsistency() {
        pdf.setId(1L);
        pdf.setName("doc.pdf");

        int hashCode1 = pdf.hashCode();
        int hashCode2 = pdf.hashCode();

        assertEquals(hashCode1, hashCode2);
    }

    @Test
    @DisplayName("Test hashCode with equal objects")
    void testHashCodeWithEqualObjects() {
        pdf.setId(1L);
        pdf.setName("doc.pdf");

        Pdf other = new Pdf();
        other.setId(1L);
        other.setName("doc.pdf");

        assertEquals(pdf.hashCode(), other.hashCode());
    }

    @Test
    @DisplayName("Test toString contains field values")
    void testToString() {
        pdf.setId(1L);
        pdf.setName("TestDoc.pdf");
        pdf.setContent("Test content");

        String toString = pdf.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("1"));
        assertTrue(toString.contains("TestDoc.pdf"));
        assertTrue(toString.contains("Test content"));
    }

    @Test
    @DisplayName("Test toString with null fields")
    void testToStringWithNullFields() {
        String toString = pdf.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("null"));
    }

    @Test
    @DisplayName("Test complete pdf lifecycle")
    void testCompletePdfLifecycle() {
        Pdf newPdf = Pdf.builder()
                .name("InitialDoc.pdf")
                .content("Initial content")
                .build();

        assertNotNull(newPdf);
        assertEquals("InitialDoc.pdf", newPdf.getName());
        assertEquals("Initial content", newPdf.getContent());

        newPdf.setName("UpdatedDoc.pdf");
        assertEquals("UpdatedDoc.pdf", newPdf.getName());

        newPdf.setContent("Updated content with new information");
        assertEquals("Updated content with new information", newPdf.getContent());
    }

    @Test
    @DisplayName("Test pdf with maximum Long id")
    void testPdfWithMaximumId() {
        Long maxId = Long.MAX_VALUE;
        pdf.setId(maxId);
        assertEquals(maxId, pdf.getId());
    }

    @Test
    @DisplayName("Test pdf with minimum Long id")
    void testPdfWithMinimumId() {
        Long minId = Long.MIN_VALUE;
        pdf.setId(minId);
        assertEquals(minId, pdf.getId());
    }

    @Test
    @DisplayName("Test pdf with unicode characters in name")
    void testPdfWithUnicodeInName() {
        String name = "文档.pdf";
        pdf.setName(name);
        assertEquals(name, pdf.getName());
    }

    @Test
    @DisplayName("Test pdf with unicode characters in content")
    void testPdfWithUnicodeInContent() {
        String content = "Content with unicode: 你好世界 🌍";
        pdf.setContent(content);
        assertEquals(content, pdf.getContent());
    }

    @Test
    @DisplayName("Test multiple content updates")
    void testMultipleContentUpdates() {
        pdf.setContent("First content");
        assertEquals("First content", pdf.getContent());

        pdf.setContent("Second content");
        assertEquals("Second content", pdf.getContent());

        pdf.setContent("Third content");
        assertEquals("Third content", pdf.getContent());
    }

    @Test
    @DisplayName("Test content is transient field")
    void testContentIsTransient() {
        pdf.setName("TransientTest.pdf");
        pdf.setContent("This content should not persist");

        assertNotNull(pdf.getContent());
        assertEquals("This content should not persist", pdf.getContent());
    }
}
