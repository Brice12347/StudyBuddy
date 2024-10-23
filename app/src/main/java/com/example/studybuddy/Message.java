package com.example.studybuddy;
import java.util.Date;

public class Message {
    public User sender;
    public String content;
    private long timestamp;
    private String messageID;
    // Correct: declared the timestamp field

    // Constructor
    public Message(String messageID, User sender, String content){
        this.sender = sender;
        this.content = content;
        this.messageID = messageID;
        this.timestamp = System.currentTimeMillis();  // Moved initialization into the constructor
    }

    // Get the sender
    public User getSender() {
        return sender;
    }

    // Get the content of the message
    public String getContent() {
        return content;
    }

    // Get the current timestamp
    public long getTimeStamp() {
        return timestamp;  // Ensure the field name matches
    }
}
