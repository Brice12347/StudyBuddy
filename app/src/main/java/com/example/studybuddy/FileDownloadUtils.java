package com.example.studybuddy;

public class FileDownloadUtils {

    public static boolean isValidDownloadUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        return url.startsWith("http") || url.startsWith("https");
    }
}
