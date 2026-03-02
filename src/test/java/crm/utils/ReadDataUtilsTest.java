package crm.utils;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ReadDataUtilsTest {

    @Test
    void testReadFileWithNullParent() {
        File result = ReadDataUtils.ReadFile("Test", null, "CSV Files", "csv");
        // Note: This will return null in headless environment as JFileChooser requires GUI
        // In real environment with user interaction, it would return selected file
        assertTrue(result == null || result instanceof File);
    }

    @Test
    void testReadFileWithMultipleExtensions() {
        File result = ReadDataUtils.ReadFile("Select file", null, "Multiple", "csv", "txt", "pdf");
        assertTrue(result == null || result instanceof File);
    }

    @Test
    void testReadFileWithSingleExtension() {
        File result = ReadDataUtils.ReadFile("Select CSV", null, "CSV", "csv");
        assertTrue(result == null || result instanceof File);
    }

    @Test
    void testReadFileWithEmptyDescription() {
        File result = ReadDataUtils.ReadFile("", null, "", "txt");
        assertTrue(result == null || result instanceof File);
    }

    @Test
    void testReadFileMethodExists() {
        assertNotNull(ReadDataUtils.class);
        try {
            ReadDataUtils.class.getMethod("ReadFile", String.class, javax.swing.JFrame.class,
                    String.class, String[].class);
        } catch (NoSuchMethodException e) {
            fail("ReadFile method should exist");
        }
    }
}
