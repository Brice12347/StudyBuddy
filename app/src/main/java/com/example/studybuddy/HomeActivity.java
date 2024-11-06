package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView userNameText;
    private Button myGroupsButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // Initialize views
        userNameText = findViewById(R.id.userNameText);
        myGroupsButton = findViewById(R.id.myGroupsButton);
        logoutButton = findViewById(R.id.logoutButton);

        // Get the username from the intent
        String username = getIntent().getStringExtra("username");
        if (username != null) {
            userNameText.setText("Welcome, " + username + "!");
        }

        // Set click listener for My Groups button
        myGroupsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, GroupPageActivity.class); // NEEDS TO BE CHANGED ONCE Group FRONTEND IS MADE
                startActivity(intent);
                //Intent intent = new Intent(HomeActivity.this, ResourcesActivity.class); // NEEDS TO BE CHANGED ONCE Group FRONTEND IS MADE
                //startActivity(intent);
            }
        });

        // Set click listener for Log Out button
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish(); // Finish current activity
            }
        });
    }
}