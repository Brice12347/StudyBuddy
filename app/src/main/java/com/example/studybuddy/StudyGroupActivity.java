package com.example.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class StudyGroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studygrouppage);

        Button chatBoxButton = findViewById(R.id.chatBoxButton);
        Button groupCalendarButton = findViewById(R.id.groupCalendarButton);
        Button addNewSessionsButton = findViewById(R.id.addNewSessionsButton);
        Button resourcesButton = findViewById(R.id.resourcesButton);

        chatBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyGroupActivity.this, GroupChatActivity.class);
                startActivity(intent);
            }
        });

        groupCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyGroupActivity.this, ResourcesActivity.class);
                startActivity(intent);
            }
        });

        addNewSessionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyGroupActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        resourcesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudyGroupActivity.this, ResourcesActivity.class);
                startActivity(intent);
            }
        });
    }
}
