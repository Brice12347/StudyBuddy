package com.example.studybuddy;

import org.junit.Test;
import static org.junit.Assert.*;

public class FileNameUtilsTest {

    @Test
    public void testSanitizeFileName_RemovesInvalidCharacters() {
        String sanitized = FileNameUtils.sanitizeFileName("test.file#name[1]");
        assertEquals("test_file_name_1_", sanitized);
    }

    @Test
    public void testSanitizeFileName_AllowsValidCharacters() {
        String sanitized = FileNameUtils.sanitizeFileName("validFileName123");
        assertEquals("validFileName123", sanitized);
    }

    @Test
    public void testSanitizeFileName_EmptyString() {
        String sanitized = FileNameUtils.sanitizeFileName("");
        assertEquals("", sanitized);
    }
}
