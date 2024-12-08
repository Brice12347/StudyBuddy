package com.example.studybuddy;

public class MessagesUtils {

    public static String generateChatId(String user1, String user2) {
        return (user1.compareTo(user2) < 0) ? user1 + "_" + user2 : user2 + "_" + user1;
    }
}
