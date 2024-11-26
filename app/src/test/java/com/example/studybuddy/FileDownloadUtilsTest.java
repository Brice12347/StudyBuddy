package com.example.studybuddy;

import org.junit.Test;
import static org.junit.Assert.*;

public class FileDownloadUtilsTest {

    @Test
    public void testIsValidDownloadUrl() {
        assertTrue(FileDownloadUtils.isValidDownloadUrl("https://example.com/file.pdf"));
        assertTrue(FileDownloadUtils.isValidDownloadUrl("http://example.com/file.pdf"));
        assertFalse(FileDownloadUtils.isValidDownloadUrl("ftp://example.com/file.pdf"));
        assertFalse(FileDownloadUtils.isValidDownloadUrl(null));
        assertFalse(FileDownloadUtils.isValidDownloadUrl(""));
    }
}
