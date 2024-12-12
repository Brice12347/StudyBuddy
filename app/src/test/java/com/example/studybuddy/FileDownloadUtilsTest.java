package com.example.studybuddy;

import org.junit.Test;
import static org.junit.Assert.*;

public class FileDownloadUtilsTest {

    @Test
    public void testIsValidDownloadUrl_1() {
        assertTrue(FileDownloadUtils.isValidDownloadUrl("https://example.com/file.pdf"));

    }
    @Test
    public void testIsValidDownloadUrl_2() {

        assertTrue(FileDownloadUtils.isValidDownloadUrl("http://example.com/file.pdf"));

    }
    @Test
    public void testIsValidDownloadUrl_3() {

        assertFalse(FileDownloadUtils.isValidDownloadUrl("ftp://example.com/file.pdf"));

    }
    @Test
    public void testIsValidDownloadUrl_4() {

        assertFalse(FileDownloadUtils.isValidDownloadUrl(null));
        //assertFalse(FileDownloadUtils.isValidDownloadUrl(""));
    }
    @Test
    public void testIsValidDownloadUrl_5() {

        assertFalse(FileDownloadUtils.isValidDownloadUrl(""));
    }
}
