package com.example.studybuddy;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.widget.TextView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class) // Ensure the test class uses the correct runner
public class StudyGroupActivityTest {


    @Test
    //Test to see if the add member function works
    public void testAddMemberToList() {
        try (ActivityScenario<StudyGroupActivity> scenario = ActivityScenario.launch(StudyGroupActivity.class)) {
            scenario.onActivity(activity -> {
                // Simulate adding a member
                activity.addMemberToList("Test Member");

                // Verify that the member was added to the layout
                TextView addedMember = (TextView) activity.memberListLayout.getChildAt(0);
                assertNotNull("Added member should not be null", addedMember);
                assertEquals("Test Member", addedMember.getText().toString());
            });
        }
    }


    @Test
    //Test to see if the ChatBox button is functional
    public void testChatBoxButtonClick() {
        try (ActivityScenario<StudyGroupActivity> scenario = ActivityScenario.launch(StudyGroupActivity.class)) {
            // Simulate clicking the chatBoxButton
            onView(withId(R.id.chatBoxButton)).perform(click());
        }
    }


    @Test
    //Test that the member list is displayed and the the title is displayed properly
    public void testUIInitialization() {
        try (ActivityScenario<StudyGroupActivity> scenario = ActivityScenario.launch(StudyGroupActivity.class)) {
            // Verify that the title TextView is displayed
            onView(withId(R.id.studyGroupTitle)).check(matches(isDisplayed()));

            // Verify that the member list ScrollView is displayed
            onView(withId(R.id.memberListContainer)).check(matches(isDisplayed()));

            // Verify that each button is displayed
            onView(withId(R.id.chatBoxButton)).check(matches(isDisplayed()));
            onView(withId(R.id.groupCalendarButton)).check(matches(isDisplayed()));
            onView(withId(R.id.addNewSessionsButton)).check(matches(isDisplayed()));
            onView(withId(R.id.resourcesButton)).check(matches(isDisplayed()));
        }
    }
}

