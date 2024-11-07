package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class newGroupActivity extends AppCompatActivity {
    FloatingActionButton addMemberFunction;
    Button enterButton;
    Course currCourse;
    String courseName;
    String groupName;
    ArrayList<String> selectedUsernames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student_to_group_page);

        addMemberFunction = findViewById(R.id.addMemberButton);
        enterButton = findViewById(R.id.enterButton);

        Intent intent = getIntent();
        courseName = intent.getStringExtra("COURSE_NAME");
        groupName = intent.getStringExtra("GROUP_ID");
        Log.i("DATA","group name(pt.2) is " + groupName);
//        currCourse = new Course();
//        currCourse.setCourseName(courseName);
        addMemberFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("DATA","is group addMemberFunction clicked " + groupName);
                showUserMenu(view);
            }
        });

        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSelectedUsersToGroup();
            }
        });
    }


    private void showUserMenu(View view) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance()
                .getReference("users");

        // Fetch users and filter by course code
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                PopupMenu popupMenu = new PopupMenu(newGroupActivity.this, view);
                boolean hasItems = false;

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String username = userSnapshot.child("username").getValue(String.class);
                    String userCourseCode = userSnapshot.child("courses").child("courseCode").getValue(String.class);

                    // Check if the user's course code matches the specified course name
                    if (userCourseCode != null && userCourseCode.equals(courseName)) {
                        popupMenu.getMenu().add(username);  // Add matching usernames to menu
                        hasItems = true;
                    }
                }

                if (hasItems) {
                    // Handle user selection from popup menu
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            String selectedUser = menuItem.getTitle().toString();
                            if (!selectedUsernames.contains(selectedUser)) {
                                selectedUsernames.add(selectedUser);
                                Toast.makeText(newGroupActivity.this, selectedUser + " added", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(newGroupActivity.this, selectedUser + " is already selected", Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        }
                    });

                    popupMenu.show();
                } else {
                    // Notify if no users matched the course name
                    Toast.makeText(newGroupActivity.this, "No users found for this course", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(newGroupActivity.this, "Failed to load users: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addSelectedUsersToGroup() {
        DatabaseReference groupRef = FirebaseDatabase.getInstance().getReference("Courses").child(courseName).child("Groups").child(groupName).child("members");

        // Add selected users to the group in Firebase
        for (String username : selectedUsernames) {
            groupRef.push().setValue(username);
        }

        Toast.makeText(this, "Members added to group", Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(newGroupActivity.this,GroupPageActivity.class);
//        startActivity(intent);
        finish();  // Close the activity after adding members
    }
}