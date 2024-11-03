package com.example.studybuddy;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class GroupChatActivity extends AppCompatActivity {
    private TextView chatLog;  // TextView to display messages

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupchat);


        // Simulate retrieving and displaying messages
        displayChatLog();
    }

    private void displayChatLog() {
        // Example of how to set text on chatLog TextView
        chatLog.setText("User1: Hello\nUser2: Hi there!\nUser1: How’s it going?");
    }
}
