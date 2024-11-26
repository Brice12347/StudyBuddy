package com.example.studybuddy;

public class FileNameUtils {

    // Utility method for sanitizing file names
    public static String sanitizeFileName(String fileName) {
        return fileName.replace(".", "_")
                .replace("#", "_")
                .replace("$", "_")
                .replace("[", "_")
                .replace("]", "_");
    }
}
