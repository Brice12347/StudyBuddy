package com.example.studybuddy;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

public class StudyBuddyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Set Firebase persistence enabled before any database usage
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
