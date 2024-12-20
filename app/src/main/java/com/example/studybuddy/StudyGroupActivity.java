package com.example.studybuddy;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.CalendarContract;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudyGroupActivity extends AppCompatActivity {

    private DatabaseReference groupRef;
    public LinearLayout memberListLayout;
    private String groupId;
    private String courseName;
    private String username;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studygrouppage);
        // Initialize Back Button
        backButton = findViewById(R.id.backButton);


        ScrollView memberListContainer = findViewById(R.id.memberListContainer);
        memberListLayout = memberListContainer.findViewById(R.id.innerMemberListLayout);
        // Get the groupId from the Intent
        //Intent intent = getIntent();
        //groupId = intent.getStringExtra("group_id");



        Intent intent = getIntent();
        courseName = intent.getStringExtra("COURSE_NAME");
        groupId = intent.getStringExtra("GROUP_ID");
        username = intent.getStringExtra("username");

        Log.i("DATA", "[StudyGroupActivity] Course Name is: " + courseName);
        Log.i("DATA", "[StudyGroupActivity] Group_ID is: " + groupId);
        Log.i("DATA", "[StudyGroupActivity] username: " + username);

        // Ensure groupId is not null
        if (groupId == null) {
            Toast.makeText(this, "Error: Group ID is missing.", Toast.LENGTH_SHORT).show();
            Log.i("DATA ERROR", "Group_ID is Missing: " + groupId);
            return;
        }

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        // Initialize Firebase reference for the group
        groupRef = db.getReference("Courses").child(courseName).child("Groups").child(groupId);        Log.i("DATA", "groupRef is: " + groupRef);
        //test reference
        //groupRef = db.getReference("Courses/CSCI103/Groups/103biggroup/");

        // Set up buttons
        Button chatBoxButton = findViewById(R.id.chatBoxButton);
        Button groupCalendarButton = findViewById(R.id.groupCalendarButton);
        Button addNewSessionsButton = findViewById(R.id.addNewSessionsButton);
        Button resourcesButton = findViewById(R.id.resourcesButton);

        chatBoxButton.setOnClickListener(v -> {
            Intent chatIntent = new Intent(StudyGroupActivity.this, MessagesActivity.class);
            chatIntent.putExtra("COURSE_NAME", courseName);
            chatIntent.putExtra("GROUP_ID", groupId);
            chatIntent.putExtra("username", username);
            startActivity(chatIntent);
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(StudyGroupActivity.this, GroupPageActivity.class);
                intent.putExtra("username", getIntent().getStringExtra("username"));
                intent.putExtra("COURSE_NAME", courseName);
                intent.putExtra("GROUP_ID",groupId);
                startActivity(intent);
                finish();
            }
        });

        //groupCalendarButton.setOnClickListener(v -> startActivity(new Intent(StudyGroupActivity.this, ResourcesActivity.class)));
        groupCalendarButton.setOnClickListener(v -> openGoogleCalendar());
        addNewSessionsButton.setOnClickListener(v -> {
            Intent scheduleIntent = new Intent(StudyGroupActivity.this, ScheduleActivity.class);
            scheduleIntent.putExtra("COURSE_NAME", courseName);
            scheduleIntent.putExtra("GROUP_ID", groupId);
            scheduleIntent.putExtra("username", username);
            startActivity(scheduleIntent);
        });
        resourcesButton.setOnClickListener(v -> {
            Intent resourcesIntent = new Intent(StudyGroupActivity.this, ResourcesActivity.class);
            resourcesIntent.putExtra("COURSE_NAME", courseName);
            resourcesIntent.putExtra("GROUP_ID", groupId);
            resourcesIntent.putExtra("username", username);
            startActivity(resourcesIntent);
        });

        // Load and display group members
        loadGroupMembers();
    }

    public void loadGroupMembers() {
        groupRef.child("members").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot memberSnapshot : dataSnapshot.getChildren()) {
                    String memberName = memberSnapshot.getKey();  // Assuming member names are keys
                    addMemberToList(memberName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(StudyGroupActivity.this, "Failed to load members: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("StudyGroupActivity", "Error loading members", databaseError.toException());
            }
        });
    }

    private void openGoogleCalendar() {
        try {
            Intent calendarIntent = new Intent(Intent.ACTION_VIEW);
            calendarIntent.setData(Uri.parse("content://com.android.calendar/time/"));
            startActivity(calendarIntent);
        } catch (Exception e) {
            Toast.makeText(this, "Unable to open Google Calendar", Toast.LENGTH_SHORT).show();
            Log.e("StudyGroupActivity", "Error opening Google Calendar", e);
        }
    }

    public void addMemberToList(String memberName) {
        // Create a button for each member
        Button memberButton = new Button(this);
        memberButton.setText(memberName);
        memberButton.setPadding(16, 16, 16, 16);
        memberButton.setTextSize(16);

        // When clicked, start MessagesActivity for a direct message chat
        memberButton.setOnClickListener(v -> {
            Intent dmIntent = new Intent(StudyGroupActivity.this, MessagesActivity.class);
            dmIntent.putExtra("IS_DIRECT_MESSAGE", true); // Flag to differentiate between group and direct messages
            dmIntent.putExtra("OTHER_USER", memberName); // The user to chat with
            dmIntent.putExtra("username", username); // The current user's username
            dmIntent.putExtra("COURSE_NAME", courseName);
            dmIntent.putExtra("GROUP_ID", groupId);
            //dmIntent.putExtra("username", username);
            startActivity(dmIntent);
        });

        memberListLayout.addView(memberButton);
    }

}