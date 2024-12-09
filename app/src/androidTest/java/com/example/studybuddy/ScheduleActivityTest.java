package com.example.studybuddy;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;

public class ScheduleActivityTest {

    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.WRITE_CALENDAR);

    @Test
    public void testUIInitialization() {
        //Test to see Scheduling Compotent works in the project
        try (ActivityScenario<ScheduleActivity> scenario = ActivityScenario.launch(ScheduleActivity.class)) {
            scenario.onActivity(activity -> {
                // Verify all UI components are initialized
                assertNotNull("Title EditText should not be null", activity.title);
                assertNotNull("Location EditText should not be null", activity.location);
                assertNotNull("Description EditText should not be null", activity.description);
                assertNotNull("Add Event Button should not be null", activity.addEvent);
            });
        }
    }

    @Test
    public void testAddEventButtonClick() {
        //Test the addEvent function of the code
        ActivityScenario.launch(ScheduleActivity.class);

        // Simulate entering text in fields
        onView(withId(R.id.eventTitle)).perform(replaceText("Meeting"));
        onView(withId(R.id.eventLocation)).perform(replaceText("Conference Room"));
        onView(withId(R.id.eventDescription)).perform(replaceText("Project Discussion"));

        onView(withId(R.id.addEventBtn)).perform(click());

    }

    @Test
    public void testPermissionRequest() {
        //Test to see if permission for the Google Calendar works correctly
        ActivityScenario.launch(ScheduleActivity.class);

        assertEquals("Permission should be granted", PackageManager.PERMISSION_GRANTED, ContextCompat.checkSelfPermission(
                ApplicationProvider.getApplicationContext(),
                Manifest.permission.WRITE_CALENDAR));
    }
}
