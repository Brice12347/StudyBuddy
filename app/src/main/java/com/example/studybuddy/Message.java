package com.example.studybuddy;

public class Message {
    public String sender;
    public long content;
    private long timestamp;
    private String messageID;
    // Correct: declared the timestamp field

    // Constructor
    public Message(String messageID, String sender, long content){
        this.sender = sender;
        this.content = content;
        this.messageID = messageID;
        this.timestamp = System.currentTimeMillis();  // Moved initialization into the constructor
    }

    // Get the sender
    public String getSender() {
        return sender;
    }

    // Get the content of the message
    public long getContent() {
        return content;
    }

    // Get the current timestamp
    public long getTimeStamp() {
        return timestamp;  // Ensure the field name matches
    }
}
