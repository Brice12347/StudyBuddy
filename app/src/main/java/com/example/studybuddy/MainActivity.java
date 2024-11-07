package com.example.studybuddy;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.widget.PopupMenu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextInputLayout signupUsername, signupConfirmPassword, signupPassword, signupEmail;
    Button signupButton, loginButton;
    FloatingActionButton courseButton;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<Course> availableCourses = new ArrayList<>();
    ArrayList<Course> selectedCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        selectedCourses = new ArrayList<>();
//        Toast.makeText(MainActivity.this, selectedCourses.size(), Toast.LENGTH_SHORT).show();
        availableCourses.add(new Course("CSCI103", "Introduction to Computer Science", "OOP in C++"));
        availableCourses.add(new Course("CSCI170", "Discrete Math", "Literal Hell"));
        availableCourses.add(new Course("CSCI104", "Data Structures and Algorithms", "The most important class ever"));


//
        signupUsername = findViewById(R.id.registrationUsernameInput);
        signupPassword = findViewById(R.id.registrationPassInput);
        signupConfirmPassword = findViewById(R.id.registrationConfirmPassInput);
        signupButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.goToSignInPage);
        signupEmail = findViewById(R.id.registrationEmailInput);
        courseButton = findViewById(R.id.addCourseButton);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database = FirebaseDatabase.getInstance("https://studybuddy-eeec8-default-rtdb.firebaseio.com/");
                reference = database.getReference("users");


                String username = signupUsername.getEditText().getText().toString();
                String password = signupPassword.getEditText().getText().toString();
                String confirmPassword = signupConfirmPassword.getEditText().getText().toString();
                String email = signupEmail.getEditText().getText().toString();

                // Check if passwords match
                if (!password.equals(confirmPassword)) {
                    Toast.makeText(MainActivity.this, "Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show();
                    return; // Exit the method if passwords don't match
                }

                reference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Username already exists
                            Toast.makeText(MainActivity.this, "Username already exists. Please try a different one.", Toast.LENGTH_SHORT).show();
                        } else {
                            // Username is available, proceed with signup
                            User helperClass = new User(username, password, email);
                            DatabaseReference userRef = reference.child(username);
                            userRef.setValue(helperClass);
                            // Store selected courses under the user's "courses" node
                            if (!selectedCourses.isEmpty()) {
                                for (Course course : selectedCourses) {
                                    String courseId = userRef.child("courses").push().getKey(); // Generate a unique ID for each course
                                    DatabaseReference courseRef = userRef.child("courses").child(courseId);

                                    DatabaseReference masterCourseRef = database.getReference(course.getCourseId());
//                                    complete here
                                    masterCourseRef.child(username).setValue(true);

                                    courseRef.child("courseName").setValue(course.getCourseName());
                                    courseRef.child("courseDescription").setValue(course.getDescription());
                                    courseRef.child("courseCode").setValue(course.getCourseId());
                                    // Add more course details as needed
                                }
                            }







                            Toast.makeText(MainActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT).show();



//                            TODO: change back after testing
//                            Intent intent = new Intent(MainActivity.this, MessagesActivity.class);
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);

                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle possible errors with Firebase operation
                        Toast.makeText(MainActivity.this, "Failed to check username. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
//                HelperClass helperClass = new HelperClass(username, password);
//                reference.child(username).setValue(helperClass);
//                Toast.makeText(MainActivity.this, "You have signup successfully!", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        courseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//
                showCourseMenu(view);
            }
        });


    }

    private void showCourseMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);

        // Add courses to the popup menu
        for (int i = 0; i < availableCourses.size(); i++) {
            popupMenu.getMenu().add(0, i, 0, availableCourses.get(i).getCourseName());
        }

        // Handle course selection and enrollment
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            int courseIndex = menuItem.getItemId();
            Course selectedCourse = availableCourses.get(courseIndex);



            if (!selectedCourses.contains(selectedCourse)) {
                selectedCourses.add(selectedCourse); // Add the selected course to the list if not already selected
                Toast.makeText(this, selectedCourse.getCourseName() + " added to selection.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, selectedCourse.getCourseName() + " is already selected.", Toast.LENGTH_SHORT).show();
            }

            return true;
        });

        popupMenu.show();
    }
}
