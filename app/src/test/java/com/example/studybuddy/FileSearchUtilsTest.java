package com.example.studybuddy;

import org.junit.Test;
import static org.junit.Assert.*;

public class FileSearchUtilsTest {

    @Test
    public void testIsSearchTermInFileName_MatchingTerm() {
        assertTrue(FileSearchUtils.isSearchTermInFileName("lecture_notes.pdf", "notes"));
    }

    @Test
    public void testIsSearchTermInFileName_NonMatchingTerm() {
        assertFalse(FileSearchUtils.isSearchTermInFileName("lecture_notes.pdf", "exam"));
    }

    @Test
    public void testIsSearchTermInFileName_NullInputs_1() {
        assertFalse(FileSearchUtils.isSearchTermInFileName(null, "exam"));
    }

    @Test
    public void testIsSearchTermInFileName_NullInputs_2() {
        assertFalse(FileSearchUtils.isSearchTermInFileName("lecture_notes.pdf", null));
    }
}
