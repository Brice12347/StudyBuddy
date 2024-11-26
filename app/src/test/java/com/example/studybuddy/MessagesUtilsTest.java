package com.example.studybuddy;

import org.junit.Test;
import static org.junit.Assert.*;

public class MessagesUtilsTest {

    @Test
    public void testGenerateChatId_OrderIndependence() {
        String chatId = MessagesUtils.generateChatId("userA", "userB");
        String reverseChatId = MessagesUtils.generateChatId("userB", "userA");

        assertEquals(chatId, reverseChatId);
        assertEquals("userA_userB", chatId);
    }

    @Test
    public void testGenerateChatId_SameUsernames() {
        String chatId = MessagesUtils.generateChatId("userA", "userA");
        assertEquals("userA_userA", chatId);
    }
}
