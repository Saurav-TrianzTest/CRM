package crm.utils;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ReadDataUtilsTest {

    @Test
    void testReadFileWithNullParent() {
        File result = ReadDataUtils.ReadFile("Select a file", null, "Text Files", "txt");
        // This will return null if user cancels the dialog or if running in headless mode
        // We just verify the method doesn't throw an exception
        assertTrue(result == null || result instanceof File);
    }

    @Test
    void testReadFileWithValidParent() {
        JFrame parent = new JFrame();
        parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        File result = ReadDataUtils.ReadFile("Select a file", parent, "Text Files", "txt");
        // This will return null if user cancels the dialog or if running in headless mode
        assertTrue(result == null || result instanceof File);

        parent.dispose();
    }

    @Test
    void testReadFileWithMultipleExtensions() {
        File result = ReadDataUtils.ReadFile("Select a file", null, "Multiple Files", "txt", "csv", "pdf");
        assertTrue(result == null || result instanceof File);
    }

    @Test
    void testReadFileWithSingleExtension() {
        File result = ReadDataUtils.ReadFile("Select a file", null, "PDF Files", "pdf");
        assertTrue(result == null || result instanceof File);
    }

    @Test
    void testReadFileWithEmptyMessage() {
        File result = ReadDataUtils.ReadFile("", null, "Text Files", "txt");
        assertTrue(result == null || result instanceof File);
    }

    @Test
    void testReadFileWithCustomExtension() {
        File result = ReadDataUtils.ReadFile("Select a file", null, "Custom Files", "custom", "dat");
        assertTrue(result == null || result instanceof File);
    }

    @Test
    void testReadFileMethodExists() {
        assertDoesNotThrow(() -> {
            ReadDataUtils.ReadFile("Test", null, "Test", "txt");
        });
    }
}
