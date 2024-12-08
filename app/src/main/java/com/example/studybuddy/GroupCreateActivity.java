package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupCreateActivity extends AppCompatActivity {
    TextInputEditText groupNameInput;
    FloatingActionButton selectClassBtn, addMemberBtn;
    ArrayList<Course> availableCourses = new ArrayList<>();
    HashMap<String, String> courseMap = new HashMap<>(); // Maps course name to course code
    ArrayList<String> matchingUsers = new ArrayList<>();
    ArrayList<String> selectedUsers = new ArrayList<>();
    DatabaseReference databaseReference;
    Course selectedCourse = null;
    Button Enterbtn;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.group_creation);

        availableCourses.add(new Course("CSCI103", "Introduction to Computer Science", "OOP in C++"));
        availableCourses.add(new Course("CSCI170", "Discrete Math", "Literal Hell"));
        availableCourses.add(new Course("CSCI104", "Data Structures and Algorithms", "The most important class ever"));

        groupNameInput = findViewById(R.id.groupNameEditText);
        selectClassBtn = findViewById(R.id.chooseClassButton);
        addMemberBtn = findViewById(R.id.addMemberToNewGroupButton);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Enterbtn = findViewById(R.id.enterButton);


        Enterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String groupName = groupNameInput.getText().toString().trim();

                if (groupName.isEmpty() || selectedCourse == null || selectedUsers.isEmpty()) {
                    Toast.makeText(GroupCreateActivity.this, "Please enter a group name, select a class, and add members.", Toast.LENGTH_SHORT).show();
                    return;
                }
                DatabaseReference groupRef = databaseReference.child("Courses").child(selectedCourse.getCourseId()).child("Groups").child(groupName);
//                THIS IS HOW YOU WOULD GET A RANDOM ID
                String groupId = databaseReference.child("Courses").child(selectedCourse.getCourseId()).child("Groups").push().getKey();
                Group newGroup = new Group(groupId, groupName);

                Intent init_intent = getIntent();
                username = init_intent.getStringExtra("username");

                // Add the creator to selectedUsers
                if (username != null && !username.isEmpty()) {
                    selectedUsers.add(username);
                } else {
                    Toast.makeText(GroupCreateActivity.this, "Error: Username not provided", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }



                groupRef.setValue(newGroup).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Add selected users to the group
                        for (String username : selectedUsers) {
                            groupRef.child("members").child(username).setValue(true);
                        }
                        Toast.makeText(GroupCreateActivity.this, "Group created successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(GroupCreateActivity.this, GroupPageActivity.class);
                        intent.putExtra("GROUP_ID", groupId);
                        intent.putExtra("username", username);
                        startActivity(intent);
                    } else {

                        Toast.makeText(GroupCreateActivity.this, "Failed to create group.", Toast.LENGTH_SHORT).show();

                    }

                });
            }
        });

        selectClassBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                showCourseMenu(view);
            }
        });

        addMemberBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedCourse == null) {
                    // Warn the user if no class is selected
                    Toast.makeText(GroupCreateActivity.this, "Please select a class before adding members.", Toast.LENGTH_SHORT).show();
                } else {
                    // Show users in the selected course
                    showUserMenu(selectedCourse.getCourseId());
                }
            }
        });


    }

    private void showCourseMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        selectedUsers.clear();

        // Populate the menu with hard-coded courses
        for (Course course : availableCourses) {
            popupMenu.getMenu().add(course.getCourseName());
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                String selectedCourseName = item.getTitle().toString();

                // Find the selected course object based on course name
                for (Course course : availableCourses) {
                    if (course.getCourseName().equals(selectedCourseName)) {
                        selectedCourse = course;
                        break;
                    }
                }

                Toast.makeText(GroupCreateActivity.this, "Selected: " + selectedCourseName, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popupMenu.show();
    }

    private void showUserMenu(String courseCode) {
        PopupMenu userPopupMenu = new PopupMenu(this, addMemberBtn);

        // Fetch users with matching courseCode from Firebase
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                matchingUsers.clear();
                userPopupMenu.getMenu().clear();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String username = userSnapshot.getKey();
                    DataSnapshot coursesSnapshot = userSnapshot.child("courses");

                    for (DataSnapshot courseSnapshot : coursesSnapshot.getChildren()) {
                        String userCourseCode = courseSnapshot.child("courseCode").getValue(String.class);

                        if (courseCode.equals(userCourseCode)) {
                            matchingUsers.add(username);
                            userPopupMenu.getMenu().add(username);
                            break;
                        }
                    }
                }

                userPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        String selectedUser = item.getTitle().toString();
                        Toast.makeText(GroupCreateActivity.this, "Added: " + selectedUser, Toast.LENGTH_SHORT).show();
                        // Add selected user to your group logic here
                        selectedUsers.add(selectedUser);
                        return true;
                    }
                });

                userPopupMenu.show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GroupCreateActivity.this, "Failed to load users.", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
