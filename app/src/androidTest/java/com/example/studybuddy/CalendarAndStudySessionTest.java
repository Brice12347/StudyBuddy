package com.example.studybuddy;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import static org.mockito.Mockito.*;


import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Black-box tests for the StudyBuddy app, including Study Session functionality
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class CalendarAndStudySessionTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> loginActivityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Rule
    public ActivityScenarioRule<LandingPageActivity> landingPageActivityRule =
            new ActivityScenarioRule<>(LandingPageActivity.class);

    @Rule
    public ActivityScenarioRule<HomeActivity> homeActivityRule =
            new ActivityScenarioRule<>(HomeActivity.class);

    @Rule
    public ActivityScenarioRule<ScheduleActivity> schedulingActivityRule =
            new ActivityScenarioRule<>(ScheduleActivity.class);

    @Test
    public void testLoginWithValidCredentials() {
        // Mock FirebaseAuth instance
        FirebaseAuth mockAuth = Mockito.mock(FirebaseAuth.class);

        // Simulate successful sign-in
        Mockito.doAnswer(invocation -> {
            FirebaseAuth.AuthResult authResult = Mockito.mock(FirebaseAuth.AuthResult.class);
            ((OnCompleteListener) invocation.getArguments()[0]).onComplete(
                    Mockito.mock(Task.class)
            );
            return null;
        }).when(mockAuth).signInWithEmailAndPassword("validUser", "validPassword");

        // Perform login
        try {
            onView(withId(R.id.loginUsernameInput))
                    .perform(typeText("validUser"), closeSoftKeyboard());
            onView(withId(R.id.loginPassInput))
                    .perform(typeText("validPassword"), closeSoftKeyboard());
            onView(withId(R.id.loginPageLoginButton)).perform(click());

            // Verify redirection to the Landing Page
            onView(withId(R.id.landingPage))
                    .check(matches(ViewMatchers.isDisplayed()));
        } catch (NoMatchingViewException e) {
            Assert.fail("Landing page not displayed after login with valid credentials.");
        }
    }


    @Test
    public void testAddStudySession() {
        // Perform login
        testLoginWithValidCredentials();

        // Navigate to Add Study Session feature
        try {
            onView(withId(R.id.addNewSessionsButton)).perform(click());
        } catch (NoMatchingViewException e) {
            Assert.fail("Add Study Session button not functioning.");
        }

        // Fill out study session details
        onView(withId(R.id.eventTitle))
                .perform(typeText("Math Study Group"), closeSoftKeyboard());
        onView(withId(R.id.eventLocation))
                .perform(typeText("Library Room 101"), closeSoftKeyboard());
        onView(withId(R.id.eventDescription))
                .perform(typeText("Discuss calculus topics"), closeSoftKeyboard());

        // Submit the session
        onView(withId(R.id.addEventBtn)).perform(click());

        // Verify the study session is added
        try {
            onView(withText("Math Study Group"))
                    .check(matches(ViewMatchers.isDisplayed()));
        } catch (NoMatchingViewException e) {
            Assert.fail("Study session not displayed after being added.");
        }
    }

    @Test
    public void testViewMemberList() {
        // Perform login
        testLoginWithValidCredentials();

        // Navigate to Member List
        try {
            onView(withId(R.id.studyGroupTitle))
                    .check(matches(ViewMatchers.isDisplayed()))
                    .check(matches(withText("Member List")));
        } catch (NoMatchingViewException e) {
            Assert.fail("Member list header not displayed.");
        }

        // Verify scrollable member list container
        try {
            onView(withId(R.id.memberListContainer))
                    .check(matches(ViewMatchers.isDisplayed()));
        } catch (NoMatchingViewException e) {
            Assert.fail("Member list container not functioning correctly.");
        }
    }

    @Test
    public void testNavigateToGroupCalendar() {
        // Perform login
        testLoginWithValidCredentials();

        // Click Group Calendar Button
        try {
            onView(withId(R.id.groupCalendarButton)).perform(click());

            // Verify navigation to calendar
            onView(withId(R.id.schedulingTitle))
                    .check(matches(ViewMatchers.withText("Scheduling Form")));
        } catch (NoMatchingViewException e) {
            Assert.fail("Group calendar button not functioning correctly.");
        }
    }
}
