package crm.utils;

import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class ReadDataUtilsTest {

    @Test
    void readFile_withValidParameters_shouldReturnFile() {
        // This test is limited because it requires GUI interaction
        // We can only test that the method doesn't throw exceptions
        assertDoesNotThrow(() -> {
            // Method requires user interaction, so we can't fully test it
            // But we can verify the method signature is correct
            assertNotNull(ReadDataUtils.class.getMethod("ReadFile", 
                String.class, JFrame.class, String.class, String[].class));
        });
    }

    @Test
    void readFile_methodExists_shouldBeAccessible() {
        // Verify the method exists and is public
        try {
            var method = ReadDataUtils.class.getMethod("ReadFile", 
                String.class, JFrame.class, String.class, String[].class);
            assertNotNull(method);
            assertTrue(java.lang.reflect.Modifier.isPublic(method.getModifiers()));
            assertTrue(java.lang.reflect.Modifier.isStatic(method.getModifiers()));
        } catch (NoSuchMethodException e) {
            fail("ReadFile method should exist");
        }
    }

    @Test
    void readFile_returnType_shouldBeFile() {
        try {
            var method = ReadDataUtils.class.getMethod("ReadFile", 
                String.class, JFrame.class, String.class, String[].class);
            assertEquals(File.class, method.getReturnType());
        } catch (NoSuchMethodException e) {
            fail("ReadFile method should exist");
        }
    }
}
