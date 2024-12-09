package com.example.studybuddy;

public class Message {
    private String content;
    private String senderID;  // Use senderID instead of a full User object to simplify
    private long timestamp;
    private String messageID;

    // Constructor
    public Message(String messageID, String senderID, String content) {
        this.senderID = senderID;
        this.content = content;
        this.messageID = messageID;
        this.timestamp = System.currentTimeMillis();
    }

    public String getSenderID() {
        return senderID;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getMessageID() {
        return messageID;
    }


}
