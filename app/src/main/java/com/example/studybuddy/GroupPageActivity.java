package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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

public class GroupPageActivity extends AppCompatActivity
{

    private DatabaseReference coursesRef;
    private LinearLayout groupsLayout;
    private String username;
    private String courseName;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_page);

        Log.i("GroupPageActivity", "inside onCreate of GroupPageActivity");

        groupsLayout = findViewById(R.id.linearLayout);

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view) {
                Intent intent = new Intent(GroupPageActivity.this, HomeActivity.class);
                intent.putExtra("username", getIntent().getStringExtra("username")); // Pass username
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
//        Toast.makeText(this, "Username should be" + username, Toast.LENGTH_SHORT).show();

        // Reference to courses in the database
        coursesRef = FirebaseDatabase.getInstance("https://studybuddy-eeec8-default-rtdb.firebaseio.com/")
                .getReference("Courses");

        loadUserGroups();



        findViewById(R.id.button).setOnClickListener(v -> {
            Intent intent1 = new Intent(GroupPageActivity.this, GroupCreateActivity.class);
            intent1.putExtra("COURSE_NAME", courseName);
            intent1.putExtra("username", username);
            //intent1.putExtra("GROUP_ID", groupId);
            startActivity(intent1);
        });
    }

    private void loadUserGroups() {
        coursesRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                for (DataSnapshot courseSnapshot : snapshot.getChildren())
                {
                    String currentCourseName = courseSnapshot.getKey();
                    Log.i("DATA", "Course Name is: " + currentCourseName);
                    DataSnapshot groupsSnapshot = courseSnapshot.child("Groups");

                    for (DataSnapshot groupSnapshot : groupsSnapshot.getChildren())
                    {
                        String groupName = groupSnapshot.child("groupName").getValue(String.class);
                        DataSnapshot membersSnapshot = groupSnapshot.child("members");

                        if (groupName != null && membersSnapshot.hasChild(username))
                        {
                            // Pass currentCourseName to addGroupToLayout to ensure each group gets the correct course
                            addGroupToLayout(currentCourseName, groupSnapshot.getKey(), groupName);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Toast.makeText(GroupPageActivity.this, "Failed to load groups: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addGroupToLayout(String courseName, String groupId, String groupName)
    {
        // Create a new horizontal layout for each group entry
        LinearLayout groupLayout = new LinearLayout(GroupPageActivity.this);
        groupLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        groupLayout.setOrientation(LinearLayout.HORIZONTAL);
        groupLayout.setGravity(android.view.Gravity.CENTER);

        // Create and set up TextView for the group name
        TextView groupTextView = new TextView(GroupPageActivity.this);
        groupTextView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        groupTextView.setText(groupName);
        groupTextView.setGravity(android.view.Gravity.CENTER);

        groupTextView.setOnClickListener(v ->
        {
            Intent studyGroupIntent = new Intent(GroupPageActivity.this, StudyGroupActivity.class);
            studyGroupIntent.putExtra("COURSE_NAME", courseName);
            studyGroupIntent.putExtra("username", username);
            studyGroupIntent.putExtra("GROUP_ID", groupId);
            startActivity(studyGroupIntent);
        });

        groupLayout.addView(groupTextView);

        // Create and set up ImageButton to add members
        ImageButton addMemberButton = new ImageButton(GroupPageActivity.this);
        addMemberButton.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addMemberButton.setImageResource(R.drawable.baseline_add_24);
        addMemberButton.setOnClickListener(v -> {
            Intent addMemberIntent = new Intent(GroupPageActivity.this, newGroupActivity.class);
            Log.i("DATA","group name is " + groupName);
            addMemberIntent.putExtra("GROUP_ID", groupName);
            Log.i("DATA","Course name is " + courseName);
            addMemberIntent.putExtra("COURSE_NAME", courseName);
            Log.i("DATA","[GroupPageActivity] User name is " + username);
            addMemberIntent.putExtra("username", username);


            startActivity(addMemberIntent);
        });

        groupLayout.addView(addMemberButton);
        groupsLayout.addView(groupLayout);


    }




}