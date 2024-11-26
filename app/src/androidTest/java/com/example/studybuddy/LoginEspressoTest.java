package com.example.studybuddy;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import android.app.Activity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
//import static android.support.test.espresso.intent.Intents.intended;
//import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginEspressoTest {

    public static final String WELCOME_STRING = "Enrolled Courses";
    public static final String USER_NAME = "testuser1";
    public static final String PASSWORD = "1";

    @Rule
    public ActivityScenarioRule<LoginActivity> activityRule =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void testUserLogin() throws InterruptedException {
        // Enter username
        onView(withHint("Enter your Username here!"))
                .perform(typeText("testuser1"), closeSoftKeyboard());

        // Enter password
        onView(withHint("Enter your Password here!"))
                .perform(typeText("1"), closeSoftKeyboard());

        // Click on login button
        onView(withId(R.id.loginPageLoginButton)).perform(click());


        Thread.sleep(3000);

        onView(withId(R.id.homeScreenButton)).check(matches(withText("Home Screen Button")));

    }

    @Test
    public void testUserLoginFalsePassword() throws InterruptedException {
        // Enter username
        onView(withHint("Enter your Username here!"))
                .perform(typeText("testuser1"), closeSoftKeyboard());

        // Enter password
        onView(withHint("Enter your Password here!"))
                .perform(typeText("2"), closeSoftKeyboard());

        // Click on login button
        onView(withId(R.id.loginPageLoginButton)).perform(click());


        Thread.sleep(3000);

        onView(withId(R.id.loginFormTitle)).check(matches(withText("Login Page!")));

    }

    @Test
    public void testUserLoginFalseUser() throws InterruptedException {
        // Enter username
        onView(withHint("Enter your Username here!"))
                .perform(typeText("testuser2"), closeSoftKeyboard());

        // Enter password
        onView(withHint("Enter your Password here!"))
                .perform(typeText("1"), closeSoftKeyboard());

        // Click on login button
        onView(withId(R.id.loginPageLoginButton)).perform(click());


        Thread.sleep(3000);

        onView(withId(R.id.loginFormTitle)).check(matches(withText("Login Page!")));

    }
}
