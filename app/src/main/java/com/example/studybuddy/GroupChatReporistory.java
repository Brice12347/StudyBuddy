package com.example.studybuddy;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupChatReporistory {
    private DatabaseReference messagesRef;

    public GroupChatReporistory(String courseID, String groupID) {
        this.messagesRef = FirebaseDatabase.getInstance().getReference("Courses")
                .child(courseID)
                .child("Groups")
                .child(groupID)
                .child("Messages");
    }

    // Method to send a message to the Firebase database
    public void sendMessage(String senderID, String content) {
        if (content != null && !content.isEmpty()) {
            String messageID = generateMessageID();
            Message newMessage = new Message(messageID, senderID, content);
            messagesRef.child(messageID).setValue(newMessage);
        }
    }

    // Method to add a message listener for real-time updates
    public void addMessageListener(ValueEventListener messageListener) {
        messagesRef.addValueEventListener(messageListener);
    }

    // Method to remove a message listener
    public void removeMessageListener(ValueEventListener messageListener) {
        messagesRef.removeEventListener(messageListener);
    }

    // Helper method to generate a unique message ID
    private String generateMessageID() {
        return "MSG" + System.currentTimeMillis();  // Generates a unique ID using current time
    }
}
