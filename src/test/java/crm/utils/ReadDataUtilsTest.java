package crm.utils;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class ReadDataUtilsTest {

    @Test
    public void testReadFileWithNullParent() {
        assertDoesNotThrow(() -> {
            File result = ReadDataUtils.ReadFile("Test Message", null, "Test Files", "txt");
        });
    }

    @Test
    public void testReadFileWithValidParameters() {
        assertDoesNotThrow(() -> {
            JFrame parent = new JFrame();
            File result = ReadDataUtils.ReadFile("Select file", parent, "CSV Files", "csv");
        });
    }

    @Test
    public void testReadFileWithMultipleExtensions() {
        assertDoesNotThrow(() -> {
            File result = ReadDataUtils.ReadFile("Select file", null, "Multiple Files", "txt", "csv", "pdf");
        });
    }

    @Test
    public void testReadFileWithEmptyMessage() {
        assertDoesNotThrow(() -> {
            File result = ReadDataUtils.ReadFile("", null, "Test Files", "txt");
        });
    }

    @Test
    public void testReadFileWithNullMessage() {
        assertDoesNotThrow(() -> {
            File result = ReadDataUtils.ReadFile(null, null, "Test Files", "txt");
        });
    }

    @Test
    public void testReadFileWithEmptyExtension() {
        assertDoesNotThrow(() -> {
            File result = ReadDataUtils.ReadFile("Test", null, "All Files", "");
        });
    }
}
