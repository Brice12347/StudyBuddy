package com.example.studybuddy;

import java.util.ArrayList;
import java.util.List;

public class GroupChat {

    public String chatID;
    List<Message> messages;

    public GroupChat(String chatID) {
        this.chatID = chatID;
        this.messages = new ArrayList<>();
    }

    // Method to send messages to the group chat
    public void sendMessage(User sender, String content) {
        if (sender != null && content != null && !content.isEmpty()) {
            String messageID = generateMessageID();  // Generate messageID here
            Message newMessage = new Message(messageID, sender, content);
            messages.add(newMessage);
        }
    }

    private String generateMessageID() {
        return "MSG" + System.currentTimeMillis();  // Generates a unique ID using current time
    }

    public List<Message> getMessages() {
        return new ArrayList<>(messages);
    }

    public String getChatID() {
        return chatID;
    }
}
