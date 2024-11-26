package com.example.studybuddy;

import static android.app.PendingIntent.getActivity;


import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.doubleClick;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;



import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;

import static androidx.test.espresso.matcher.RootMatchers.withDecorView;

import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;


@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegistrationEspressoTest {

    public static final String SUCCESS_STRING = "You have signed up successfully";
    public static final String USER_NAME = "testuser";
    public static final String PASSWORD = "1";
    public static final String EMAIL = "testuser@example.com";

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testUserRegistrationEmail() throws InterruptedException {
        // Enter email
        onView(withHint("Enter your Email here!"))
                .perform(typeText(EMAIL), closeSoftKeyboard());

        // Click the "Register" button
        onView(withId(R.id.registerButton)).check(matches(isDisplayed())).perform(click());

        Thread.sleep(3000);
        // Verify that the login form is displayed
        onView(withId(R.id.resgistrationFormTitle)).check(matches(isDisplayed()));


    }

    @Test
    public void testUserRegistrationToLogin() throws InterruptedException {

        // Click the "Register" button
        onView(withId(R.id.goToSignInPage)).check(matches(isDisplayed())).perform(click());

        Thread.sleep(3000);

        onView(withId(R.id.loginPageLoginButton)).check(matches(isDisplayed()));


    }

    @Test
    public void testUserRegistrationUserName() throws InterruptedException {
//
        		onView(withHint("Enter your Email here!"))
                .perform(typeText(EMAIL), closeSoftKeyboard());
////
        onView(withHint("Enter your Username here!"))
                .perform(typeText(USER_NAME), closeSoftKeyboard());
////
////
//
////
//
//
//
        onView(withId(R.id.registerButton)).check(matches(isDisplayed())).perform(click());
//
        Thread.sleep(3000);
//
        onView(withId(R.id.resgistrationFormTitle)).check(matches(isDisplayed()));
//
//
    }

    @Test
    public void testUserRegistrationPassword() throws InterruptedException {
        // Enter email
        onView(withHint("Enter your Email here!"))
                .perform(typeText(EMAIL), closeSoftKeyboard());
//
        onView(withHint("Enter your Username here!"))
                .perform(typeText(USER_NAME), closeSoftKeyboard());
//
//

//
        onView(withHint("Enter your Password here!"))
                .perform(typeText(PASSWORD), closeSoftKeyboard());


        onView(withId(R.id.registerButton)).check(matches(isDisplayed())).perform(click());

        Thread.sleep(3000);

        onView(withId(R.id.resgistrationFormTitle)).check(matches(isDisplayed()));


    }

}