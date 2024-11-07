package com.example.studybuddy;

import static java.security.AccessController.getContext;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class ScheduleActivity extends AppCompatActivity {

    private static final int CALENDAR_PERMISSION_CODE = 100;
    EditText title;
    EditText location;
    EditText description;
    Button addEvent;
    private DatabaseReference databaseReference;
    private String calendarId;
    private DatabaseReference groupRef;
    private String groupId;
    private String groupName;
    private ArrayList<String> inviteesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduling_page);

        title = findViewById(R.id.eventTitle);
        location = findViewById(R.id.eventLocation);
        description = findViewById(R.id.eventDescription);
        addEvent = findViewById(R.id.addEventBtn);
//        TODO:
//        I need the user's information for this to work.
//        what we can do is send the information using intents.
//        databaseReference = FirebaseDatabase.getInstance().getReference("Courses/Course1/Groups/Group1/calendarId");
        // Retrieve the calendar ID for the specific group
//        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    calendarId = dataSnapshot.getValue(String.class);
//                } else {
//                    Toast.makeText(ScheduleActivity.this, "Calendar ID not found", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(ScheduleActivity.this, "Failed to load calendar ID", Toast.LENGTH_SHORT).show();
//            }
//        });

        Intent intent = getIntent();
        groupId = intent.getStringExtra("GROUP_ID");
        groupName = intent.getStringExtra("GROUP_NAME");


        // Initialize Firebase reference for the group
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        groupRef = db.getReference("Courses").child(intent.getStringExtra("COURSE_NAME")).child("Groups").child(groupId).child("members");

        inviteesList = new ArrayList<>();

        // Fetch group members' emails
        loadGroupMembers();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR
            }, CALENDAR_PERMISSION_CODE);
        }

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!title.getText().toString().isEmpty() && !location.getText().toString().isEmpty() && !description
                        .getText().toString().isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_INSERT);
                    intent.setData(CalendarContract.Events.CONTENT_URI);
                    intent.putExtra(CalendarContract.Events.TITLE, title.getText().toString());
                    intent.putExtra(CalendarContract.Events.EVENT_LOCATION, location.getText().toString());
                    intent.putExtra(CalendarContract.Events.DESCRIPTION, description.getText().toString());
                    intent.putExtra(CalendarContract.Events.ALL_DAY, true);

                    intent.putExtra(CalendarContract.Events.CALENDAR_DISPLAY_NAME, groupName);
//                  intent.putExtra(CalendarContract.Events.CALENDAR_ID, calendarId);
//                    guests
//                    //intent.putExtra(Intent.EXTRA_EMAIL, "test@yahoo.com, test2@yahoo.com, test3@yahoo.com");

                    if (!inviteesList.isEmpty()) {
                        StringBuilder invitees = new StringBuilder();
                        for (String email : inviteesList) {
                            invitees.append(email).append(",");
                        }
                        intent.putExtra(Intent.EXTRA_EMAIL, invitees.toString());
                    }

                    if(intent.resolveActivity(getPackageManager()) != null){
//                        startActivity(intent);
                        startActivity(Intent.createChooser(intent, "Choose Calendar App"));
                    }else{
                        Toast.makeText(ScheduleActivity.this, "There is no app that support this action", Toast.LENGTH_SHORT).show();
                    }



                }else{
                    Toast.makeText(ScheduleActivity.this, "Please fill all the fields",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    private void loadGroupMembers() {
        groupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot memberSnapshot : dataSnapshot.getChildren()) {
                    String username = memberSnapshot.getKey();  // Get the username of each member

                    // Now look up the email in the users node
                    FirebaseDatabase.getInstance().getReference("users")
                            .child(username)
                            .child("email")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot emailSnapshot) {
                                    String memberEmail = emailSnapshot.getValue(String.class);
                                    if (memberEmail != null) {
                                        inviteesList.add(memberEmail);
                                    }
                                    Log.i("ScheduleActivity", "Invitees loaded: " + inviteesList);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Log.e("ScheduleActivity", "Error loading email for user " + username, databaseError.toException());
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ScheduleActivity.this, "Failed to load group members", Toast.LENGTH_SHORT).show();
                Log.e("ScheduleActivity", "Error loading members", databaseError.toException());
            }
        });
    }



    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALENDAR_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Calendar permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Calendar permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}