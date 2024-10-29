package com.example.studybuddy;

import static java.security.AccessController.getContext;
import android.Manifest;
import android.content.pm.PackageManager;
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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ScheduleActivity extends AppCompatActivity {

    private static final int CALENDAR_PERMISSION_CODE = 100;
    EditText title;
    EditText location;
    EditText description;
    Button addEvent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scheduling_page);

        title = findViewById(R.id.eventTitle);
        location = findViewById(R.id.eventLocation);
        description = findViewById(R.id.eventDescription);
        addEvent = findViewById(R.id.addEventBtn);
//      calendarId = database.getReference("Courses/Course1/Groups/Group1/id");
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
//                  intent.putExtra(CalendarContract.Events.CALENDAR_ID, calendarId);
//                    guests
//                    intent.putExtra(Intent.EXTRA_EMAIL, "test@yahoo.com, test2@yahoo.com, test3@yahoo.com");

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