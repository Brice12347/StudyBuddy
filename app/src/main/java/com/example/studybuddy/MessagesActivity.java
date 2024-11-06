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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_messages);


        send = findViewById(R.id.Send);
        ed = findViewById(R.id.edmsg);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
//        TODO: use intent to send class name and group name.
//       // Retrieve the groupId from the intent
//        String groupId = getIntent().getStringExtra("groupId");
//
//        // Set up Firebase reference for the group's messages
//        FirebaseDatabase db = FirebaseDatabase.getInstance();
//        TODO:Something like this
//        messagesRef = db.getReference("Courses/CSCI103/Groups/" + groupId + "/messages");

        DatabaseReference messagesRef = db.getReference("Courses/CSCI103/Groups/103biggroup/messages");
        ArrayList al = new ArrayList();
        messagesList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, messagesList);
        lv = findViewById(R.id.lv);

        lv.setAdapter(adapter);


        messagesRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String text = snapshot.child("text").getValue(String.class);
                String senderId = snapshot.child("senderId").getValue(String.class);
                String time = snapshot.child("time").getValue(String.class);

                if (text != null && senderId != null && time != null) {
                    String message = senderId + " (" + time + "): " + text;
                    messagesList.add(message);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = ed.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    String senderId = "userId1"; // Replace with actual user ID
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


}
