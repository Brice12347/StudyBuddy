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
    private LinearLayout memberListLayout;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studygrouppage);

        ScrollView memberListContainer = findViewById(R.id.memberListContainer);
        memberListLayout = memberListContainer.findViewById(R.id.innerMemberListLayout);
        // Get the groupId from the Intent
        Intent intent = getIntent();
        groupId = intent.getStringExtra("group_id");

        FirebaseDatabase db = FirebaseDatabase.getInstance();




        // Initialize Firebase reference for the group
        //groupRef = FirebaseDatabase.getInstance("https://studybuddy-eeec8-default-rtdb.firebaseio.com/").getReference("Groups").child(groupId);

        //test reference
        groupRef = db.getReference("Courses/CSCI103/Groups/103biggroup/");

        // Set up buttons
        Button chatBoxButton = findViewById(R.id.chatBoxButton);
        Button groupCalendarButton = findViewById(R.id.groupCalendarButton);
        Button addNewSessionsButton = findViewById(R.id.addNewSessionsButton);
        Button resourcesButton = findViewById(R.id.resourcesButton);

        chatBoxButton.setOnClickListener(v -> startActivity(new Intent(StudyGroupActivity.this, MessagesActivity.class)));
        //groupCalendarButton.setOnClickListener(v -> startActivity(new Intent(StudyGroupActivity.this, ResourcesActivity.class)));
        groupCalendarButton.setOnClickListener(v -> openGoogleCalendar());
        addNewSessionsButton.setOnClickListener(v -> startActivity(new Intent(StudyGroupActivity.this, ScheduleActivity.class)));
        resourcesButton.setOnClickListener(v -> startActivity(new Intent(StudyGroupActivity.this, ResourcesActivity.class)));

        // Load and display group members
        loadGroupMembers();
    }

    private void loadGroupMembers() {
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

    private void addMemberToList(String memberName) {
        TextView memberTextView = new TextView(this);
        memberTextView.setText(memberName);
        memberTextView.setPadding(16, 16, 16, 16);
        memberTextView.setTextSize(16);
        memberListLayout.addView(memberTextView);
    }
}
