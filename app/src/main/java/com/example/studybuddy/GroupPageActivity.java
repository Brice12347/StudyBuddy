package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_page);

        TextView group1 = findViewById(R.id.pretendGroup1);
        TextView group2 = findViewById(R.id.pretendGroup2);
        TextView group3 = findViewById(R.id.pretendGroup3);

        // Call openGroupChat with the respective group name on click
        group1.setOnClickListener(view -> openGroupChat("Group 1"));
        group2.setOnClickListener(view -> openGroupChat("Group 2"));
        group3.setOnClickListener(view -> openGroupChat("Group 3"));
    }

    // Method to start MessagesActivity with the selected group name
    private void openGroupChat(String groupName) {
        Intent intent = new Intent(GroupPageActivity.this, MessagesActivity.class);
        intent.putExtra("groupName", groupName);
        startActivity(intent);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.group_page);
//
//        linearLayout = findViewById(R.id.linearLayout);
//
//        // Reference to the groups in Firebase
//        DatabaseReference groupsRef = FirebaseDatabase.getInstance()
//                .getReference("Courses/CSCI103/Groups");
//
//        // Listen for groups data
//        groupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
//                    String groupName = groupSnapshot.child("groupName").getValue(String.class);
//                    String groupId = groupSnapshot.child("groupId").getValue(String.class);
//
//                    if (groupName != null && groupId != null) {
//                        addGroupTextView(groupName, groupId);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle possible errors.
//            }
//        });
//    }
//
//    private void addGroupTextView(String groupName, String groupId) {
//        TextView groupTextView = new TextView(this);
//        groupTextView.setText(groupName);
//        groupTextView.setTextSize(18);
//        groupTextView.setPadding(16, 16, 16, 16);
//
//        // Set click listener for each group
//        groupTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openGroupChat(groupId);
//            }
//        });
//
//        // Add the TextView to the LinearLayout
//        linearLayout.addView(groupTextView);
//    }
//
//    private void openGroupChat(String groupId) {
//        Intent intent = new Intent(GroupPageActivity.this, MessagesActivity.class);
//        intent.putExtra("groupId", groupId);
//        startActivity(intent);
//    }
}