package com.example.studybuddy;

public class FileSearchUtils {

    public static boolean isSearchTermInFileName(String fileName, String searchTerm) {
        if (fileName == null || searchTerm == null) {
            return false;
        }
        return fileName.toLowerCase().contains(searchTerm.toLowerCase());
    }
}
