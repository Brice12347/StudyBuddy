package com.example.studybuddy;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MessagesActivity extends AppCompatActivity {

    ListView lv;
    Button send, selectFileButton;
    EditText ed;
    ArrayList<String> messagesList;
    ArrayAdapter<String> adapter;
    private DatabaseReference messagesRef;
    private String username;
    private boolean isDirectMessage;
    private String otherUser;
    private String courseName;
    private String groupId;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ArrayList<String> fileNames;
    private Map<String, String> fileUrls; // Maps file name to URL
    private Uri fileUri;

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_messages);


        send = findViewById(R.id.Send);
        ed = findViewById(R.id.edmsg);

        // Initialize Back Button
        backButton = findViewById(R.id.backButton);


        // Retrieve course name and group ID from Intent
        //String courseName = getIntent().getStringExtra("COURSE_NAME");
        //String groupId = getIntent().getStringExtra("GROUP_ID");
        username = getIntent().getStringExtra("username");
        isDirectMessage = getIntent().getBooleanExtra("IS_DIRECT_MESSAGE", false);
        otherUser = getIntent().getStringExtra("OTHER_USER");
        courseName = getIntent().getStringExtra("COURSE_NAME");
        groupId = getIntent().getStringExtra("GROUP_ID");

        isDirectMessage = getIntent().getBooleanExtra("IS_DIRECT_MESSAGE", false);
        otherUser = getIntent().getStringExtra("OTHER_USER");

        fileNames = new ArrayList<>();
        fileUrls = new HashMap<>();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        selectFileButton = findViewById(R.id.selectFile);

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MessagesActivity.this, StudyGroupActivity.class);
                intent.putExtra("username", getIntent().getStringExtra("username"));
                Log.i("DATA", "[MessagesActivity] username is: " + username);
                intent.putExtra("COURSE_NAME",courseName);
                Log.i("DATA", "[StudyGroupActivity] Course Name is: " + courseName);
                intent.putExtra("GROUP_ID", groupId);
                Log.i("DATA", "[StudyGroupActivity] Group Name is: " + groupId);
                startActivity(intent);
                finish();
            }
        });



        /*if (username == null || otherUser == null) {
            Toast.makeText(this, "Course or group information is missing", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }*/

        // Set up Firebase reference for the group's messages
        FirebaseDatabase db = FirebaseDatabase.getInstance();

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

        lv.setOnItemClickListener((parent, view, position, id) -> {
            String clickedMessage = messagesList.get(position);

            // Check if the message corresponds to a file
            if (clickedMessage.contains("File - ")) {
                String fileName = extractFileNameFromMessage(clickedMessage);

//                Toast.makeText(MessagesActivity.this,fileName, Toast.LENGTH_SHORT).show();


                String url = fileUrls.get(fileName);

                if (url != null) {

                    downloadFile(fileName, url);
                    openUrlInBrowser(url);
                } else {
                    Toast.makeText(this, "File URL not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Do nothing if the message is not a file
                Toast.makeText(this, "This message is not a file", Toast.LENGTH_SHORT).show();
            }
        });

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
//
    private String extractFileNameFromMessage(String message) {
        int startIndex = message.indexOf("File - ") + 7;
        int endIndex = message.indexOf(" [Click to download]");
        return message.substring(startIndex, endIndex).trim();
    }

    private void openUrlInBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
//
//
//
    private void downloadFile(String fileName, String url) {
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(this, "/Downloads", fileName);
        downloadManager.enqueue(request);
        Toast.makeText(this, "Download started...", Toast.LENGTH_SHORT).show();
    }

    //
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        fileChooserLauncher.launch(Intent.createChooser(intent, "Select File"));
    }
//
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
//
    private void uploadFile() {
        if (fileUri != null) {
            String fileName = System.currentTimeMillis() + "_" + getFileName(fileUri);
            StorageReference fileRef = storageReference.child("Courses/" + courseName + "/Groups/" + groupId +"/messages");

            fileRef.putFile(fileUri).addOnSuccessListener(taskSnapshot ->
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        sendMessageWithFile(uri.toString(), fileName);
                    })).addOnFailureListener(e ->
                    Toast.makeText(MessagesActivity.this, "File upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }
//
    private void sendMessageWithFile(String fileUrl, String fileName) {
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
        Map<String, Object> newMessage = new HashMap<>();
        newMessage.put("fileUrl", fileUrl);
        newMessage.put("fileName", fileName);
        newMessage.put("senderId", username);
        newMessage.put("time", time);


//        fileUrls.put(fileName, fileUrl); // Store file URL for downloading

        messagesRef.push().setValue(newMessage);
    }
//
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (nameIndex >= 0) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }
//
    private String generateChatId(String user1, String user2) {
        // Sort usernames to ensure a consistent chat ID regardless of who initiated the chat
        return (user1.compareTo(user2) < 0) ? user1 + "_" + user2 : user2 + "_" + user1;
    }
//
    private void loadMessages() {
        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Check if the message has file-specific fields
                String fileName = snapshot.child("fileName").getValue(String.class);
                String fileUrl = snapshot.child("fileUrl").getValue(String.class);
                String senderId = snapshot.child("senderId").getValue(String.class);
                String time = snapshot.child("time").getValue(String.class);
                String text = snapshot.child("text").getValue(String.class);

                if (fileName != null && fileUrl != null && senderId != null && time != null) {
                    // It's a file message
                    String message = "File - " + fileName + " [Click to download]";

                    messagesList.add(message);

                    fileUrls.put(fileName, fileUrl); // Store file name and URL for downloads
                } else if (text != null && senderId != null && time != null) {
                    // It's a regular text message
                    String message = senderId + " (" + time + "): " + text;
                    messagesList.add(message);
                }

                adapter.notifyDataSetChanged();
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
//



}
