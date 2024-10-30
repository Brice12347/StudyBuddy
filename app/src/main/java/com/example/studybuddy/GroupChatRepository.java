package com.example.studybuddy;

import android.util.Log;
import com.example.studybuddy.StudyGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class GroupChatRepository {
    private FirebaseFirestore db;

    public GroupChatRepository() {
        db = FirebaseFirestore.getInstance();
    }

    // Method to send messages (as shown before)
    public void sendMessage(String groupID, String content, String senderID) {
        CollectionReference messagesRef = db.collection("StudyGroups").document(groupID).collection("groupChat");

        long timestamp = System.currentTimeMillis();
        Message message = new Message(senderID, content, timestamp);

        messagesRef.add(message)
                .addOnSuccessListener(documentReference -> Log.d("Firestore", "Message sent"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error sending message", e));
    }

    // Method to listen for messages
    public void listenForMessages(String groupID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference messagesRef = db.collection("StudyGroups").document(groupID).collection("groupChat");

        messagesRef.orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((querySnapshot, e) -> {
                    if (e != null) {
                        Log.e("Firestore", "Listen failed", e);
                        return;
                    }

                    if (querySnapshot != null) {
                        List<Message> messages = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : querySnapshot) {
                            Message message = doc.toObject(Message.class);
                            messages.add(message);
                        }
                        // Use the messages list to update your chat UI
                        updateChatUI(messages); // Implement this method to handle UI updates
                    }
                });
    }

    // Placeholder for UI update method
    private void updateChatUI(List<Message> messages) {
        // Update your chat interface with the new messages
    }
}
