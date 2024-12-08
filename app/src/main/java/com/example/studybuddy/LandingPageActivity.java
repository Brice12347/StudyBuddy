package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LandingPageActivity extends AppCompatActivity {

    private DatabaseReference userRef;
    private LinearLayout coursesLayout;
    private Button homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);

        coursesLayout = findViewById(R.id.linearLayout);
        homeBtn = findViewById(R.id.homeScreenButton);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        // Initialize Firebase reference to user's courses
        userRef = FirebaseDatabase.getInstance("https://studybuddy-eeec8-default-rtdb.firebaseio.com/")
                .getReference("users").child(username).child("courses");

        // Fetch and display enrolled courses
        loadEnrolledCourses();

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingPageActivity.this, HomeActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }


    private void loadEnrolledCourses() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot courseSnapshot : snapshot.getChildren()) {
                    String courseName = courseSnapshot.child("courseName").getValue(String.class);

                    if (courseName != null) {
                        TextView courseTextView = new TextView(LandingPageActivity.this);
                        courseTextView.setLayoutParams(new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
                        courseTextView.setText(courseName);
                        courseTextView.setGravity(android.view.Gravity.CENTER);
                        coursesLayout.addView(courseTextView);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle potential errors
            }
        });
    }
}