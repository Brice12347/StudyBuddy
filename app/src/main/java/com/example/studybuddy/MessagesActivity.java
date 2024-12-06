package com.example.studybuddy;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MessagesActivity extends AppCompatActivity {

    ListView lv;
    Button send;
    EditText ed;
    ArrayList<String> messagesList;
    ArrayAdapter<String> adapter;
    private DatabaseReference messagesRef;
    private String username;
    private boolean isDirectMessage;
    private String otherUser;
    private String courseName;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_messages);

        selectFileButton = findViewById(R.id.selectFile);
        send = findViewById(R.id.Send);
        ed = findViewById(R.id.edmsg);

        // Retrieve course name and group ID from Intent
        //String courseName = getIntent().getStringExtra("COURSE_NAME");
        //String groupId = getIntent().getStringExtra("GROUP_ID");
        username = getIntent().getStringExtra("USERNAME");
        isDirectMessage = getIntent().getBooleanExtra("IS_DIRECT_MESSAGE", false);
        otherUser = getIntent().getStringExtra("OTHER_USER");
        courseName = getIntent().getStringExtra("COURSE_NAME");
        groupId = getIntent().getStringExtra("GROUP_ID");

        isDirectMessage = getIntent().getBooleanExtra("IS_DIRECT_MESSAGE", false);
        otherUser = getIntent().getStringExtra("OTHER_USER");



        /*if (username == null || otherUser == null) {
            Toast.makeText(this, "Course or group information is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }*/

        // Set up Firebase reference for the group's messages
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (isDirectMessage) {
            // Check if both usernames are available for direct messages
            if (username == null || otherUser == null) {
                Toast.makeText(this, "Direct message user information is missing", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            // Set up Firebase reference for direct messages
            String chatId = generateChatId(username, otherUser);
            messagesRef = db.getReference("DirectMessages").child(chatId);
        } else {
            // Check if both course and group information are available for group messages
            if (courseName == null || groupId == null) {
                Toast.makeText(this, "Group or course information is missing", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            // Set up Firebase reference for group messages
            messagesRef = db.getReference("Courses").child(courseName).child("Groups").child(groupId).child("messages");
        }

        //FirebaseDatabase db = FirebaseDatabase.getInstance();
//        TODO: use intent to send class name and group name.
//       // Retrieve the groupId from the intent
//        String groupId = getIntent().getStringExtra("groupId");
//
//        // Set up Firebase reference for the group's messages
//        FirebaseDatabase db = FirebaseDatabase.getInstance();
//        TODO:Something like this
//        messagesRef = db.getReference("Courses/CSCI103/Groups/" + groupId + "/messages");

        //DatabaseReference messagesRef = db.getReference("Courses/CSCI103/Groups/103biggroup/messages");
        ArrayList al = new ArrayList();
        messagesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messagesList);
        lv = findViewById(R.id.lv);

        lv.setAdapter(adapter);


        loadMessages();

        send.setOnClickListener(view -> sendMessage());
        selectFileButton.setOnClickListener(view -> openFileChooser());

        // Send button click listener to send a message
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = ed.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    String senderId = username; // Replace with actual user ID
                    String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());

                    // Create a new message map
                    Map<String, Object> newMessage = new HashMap<>();
                    newMessage.put("text", messageText);
                    newMessage.put("senderId", senderId);
                    newMessage.put("time", time);

                    // Push the new message to Firebase
                    messagesRef.push().setValue(newMessage);

                    // Clear the input field
                    ed.setText("");
                } else {
                    Toast.makeText(MessagesActivity.this, "Enter a message", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String generateChatId(String user1, String user2) {
        // Sort usernames to ensure a consistent chat ID regardless of who initiated the chat
        return (user1.compareTo(user2) < 0) ? user1 + "_" + user2 : user2 + "_" + user1;
    }

    private void loadMessages() {
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String text = snapshot.child("text").getValue(String.class);
                String fileUrl = snapshot.child("fileUrl").getValue(String.class);
                String fileName = snapshot.child("fileName").getValue(String.class);
                String senderId = snapshot.child("senderId").getValue(String.class);
                String time = snapshot.child("time").getValue(String.class);

                if (fileUrl != null) {
                    String message;
                    message = senderId + " (" + time + "): File - " + fileName + " [Click to download]";
                } else {
                    message = senderId + " (" + time + "): " + text;
                }
                else if (text != null && senderId != null && time != null) {
                    String message = senderId + " (" + time + "): " + text;
                    messagesList.add(message);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {}
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void sendMessage() {
        String messageText = ed.getText().toString().trim();
        if (!messageText.isEmpty()) {
            String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
            Map<String, Object> newMessage = new HashMap<>();
            newMessage.put("text", messageText);
            newMessage.put("senderId", username);
            newMessage.put("time", time);

            // Push the new message to Firebase
            messagesRef.push().setValue(newMessage);

            // Clear the input field
            ed.setText("");
        } else {
            Toast.makeText(MessagesActivity.this, "Enter a message", Toast.LENGTH_SHORT).show();
        }
    }

    private final ActivityResultLauncher<Intent> fileChooserLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    fileUri = result.getData().getData();
                    uploadFile();
                } else {
                    Toast.makeText(MessagesActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
                }
            });

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*"); // Allow all file types
        intent.setAction(Intent.ACTION_GET_CONTENT);
        fileChooserLauncher.launch(Intent.createChooser(intent, "Select File"));
    }

    private void uploadFile() {
        if (fileUri != null) {
            String fileName = System.currentTimeMillis() + "_" + getFileName(fileUri);
            StorageReference fileRef = storageReference.child("messages/" + groupId + "/" + fileName);

            fileRef.putFile(fileUri).addOnSuccessListener(taskSnapshot ->
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        sendMessageWithFile(uri.toString(), fileName);
                    })).addOnFailureListener(e ->
                    Toast.makeText(MessagesActivity.this, "File upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void sendMessageWithFile(String fileUrl, String fileName) {
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        Map<String, Object> newMessage = new HashMap<>();
        newMessage.put("fileUrl", fileUrl);
        newMessage.put("fileName", fileName);
        newMessage.put("senderId", username);
        newMessage.put("time", time);

        messagesRef.push().setValue(newMessage);
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }


}
