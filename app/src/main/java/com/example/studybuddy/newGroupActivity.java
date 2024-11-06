package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class newGroupActivity extends AppCompatActivity {
    FloatingActionButton addMemberFunction;
    Course currCourse;
    String courseName;
    ArrayList<User> selectedUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student_to_group_page);

        Intent intent = getIntent();
        courseName = intent.getStringExtra("username");
        currCourse = new Course();
        currCourse.setCourseName(courseName);
        addMemberFunction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//              TODO:
//                I need an intent that gives me the current class so that I can access
//                the students enrolled in the class. This will be currCourse

                showCourseMenu(view);
            }
        });
    }


    private void showCourseMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);

        ArrayList<User> enrolledStudents = currCourse.getEnrolledStudents();
        // Add courses to the popup menu
        for (int i = 0; i < enrolledStudents.size(); i++) {
            popupMenu.getMenu().add(0, i, 0, enrolledStudents.get(i).getUsername());
        }

        // Handle course selection and enrollment
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int courseIndex = menuItem.getItemId();
            User selectedUser = enrolledStudents.get(courseIndex);



            if (!selectedUsers.contains(selectedUser)) {
                selectedUsers.add(selectedUser); // Add the selected course to the list if not already selected
                Toast.makeText(this, selectedUser.getUsername() + " added to selection.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, selectedUser.getUsername() + " is already selected.", Toast.LENGTH_SHORT).show();
            }

            return true;
        });

        popupMenu.show();
    }
}