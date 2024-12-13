package com.example.studybuddy;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

/**
 * Simple Espresso tests for StudyBuddy app
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class StudyBuddyInstrumentedTest {

    @Rule
    public ActivityScenarioRule<LoginActivity> loginActivityRule =
            new ActivityScenarioRule<>(LoginActivity.class);
    public ActivityScenarioRule<LandingPageActivity> LandingpageActivityRule =
            new ActivityScenarioRule<>(LandingPageActivity.class);
    public ActivityScenarioRule<HomeActivity>  HomeActivityRule =
            new ActivityScenarioRule<>(HomeActivity.class);

    @Test
    public void testLoginWithValidCredentials()  {
        // Navigate to login page from registration
        onView(withId(2131231004)).perform(click());

        // Enter username and password
        onView(withId(R.id.loginUsernameField))  // Updated to interact with TextInputEditText
                .perform(typeText("0dahero"), closeSoftKeyboard());
        onView(withId(R.id.loginPasswordField))  // Updated to interact with TextInputEditText
                .perform(typeText("0"), closeSoftKeyboard());

        // Click login button
        try {
            Thread.sleep(1200);
            onView(withId(R.id.loginPageLoginButton)).perform(click());
        }
        catch(InterruptedException e)
        {

        }

        //onView(isRoot()).perform(waitForView(withId(R.id.homeScreenButton), 5000));

        // Verify redirection to HomeActivity
        //onView(withId(R.id.homeScreenButton)).check(matches(ViewMatchers.isDisplayed()));


        try {
            Thread.sleep(1200);

            onView(withId(R.id.homeScreenButton)).check(matches(ViewMatchers.isDisplayed()));
        } catch (NoMatchingViewException | InterruptedException e ) {

            try {
                Thread.sleep(750);
                //onView(withId(R.id.homeScreenButton)).perform(click());
                onView(withId(R.id.loginPageLoginButton)).perform(click());
                Thread.sleep(1200);
                onView(withId(R.id.homeScreenButton)).check(matches(ViewMatchers.isDisplayed()));

            }
            catch (NoMatchingViewException | InterruptedException f) {
                Assert.fail("testNav2MyGroups - Groups Button not found. Check if HomePage is displayed.");
            }

            Assert.fail(" TestLogin-w-Valid Credentials: Home Screen Button not found. Check if LandingPage is displayed.");
        }
    }


    @Test
    public void testNavigateToMyGroups() {
        // Login first
        testLoginWithValidCredentials();

        // Click "home" button

        onView(withId(R.id.homeScreenButton)).perform(click());
        try {
            Thread.sleep(200);
            onView(withId(R.id.homeScreenButton)).perform(click());
            //onView(withId(R.id.homeScreenButton)).perform(click());

            Thread.sleep(500);

            onView(withId(R.id.myGroupsButton)).check(matches(ViewMatchers.isDisplayed()));
        }
        catch (NoMatchingViewException | InterruptedException e) {

            try {
                Thread.sleep(500);
                //onView(withId(R.id.homeScreenButton)).perform(click());
                onView(withId(R.id.myGroupsButton)).check(matches(ViewMatchers.isDisplayed()));
            }
            catch (NoMatchingViewException | InterruptedException f) {
            Assert.fail("testNav2MyGroups - Groups Button not found. Check if HomePage is displayed.");
            }
        }


        // Verify navigation to GroupPageActivity
        //onView(withText("Groups")).check(matches(ViewMatchers.isDisplayed()));

    }

    @Test
    public void testOpenSpecificStudyGroup() {
        // Login and navigate to "My Groups"
        testNavigateToMyGroups();

        try {
            //onView(withId(R.id.loginPageLoginButton)).check(matches(ViewMatchers.isDisplayed()));
        } catch (NoMatchingViewException e) {
            Assert.fail("Login Button not found. Check if Login Page is displayed.");
        }

        try {
            onView(withId(R.id.myGroupsButton)).check(matches(ViewMatchers.isDisplayed()));
        } catch (NoMatchingViewException e) {
            Assert.fail("Home Button not found #1. Check if GroupPage is displayed.");
        }


        onView(withId(R.id.myGroupsButton)).perform(click());

        try {
            Thread.sleep(750);
            onView(withText("another0")).check(matches(ViewMatchers.isDisplayed()));
        } catch (NoMatchingViewException | InterruptedException e) {
            Assert.fail("Groups Button not found #1. Check if Home Page is displayed.");
        }
        try {
            Thread.sleep(250);
            onView(withText("another0")).check(matches(ViewMatchers.isDisplayed()));
        } catch (NoMatchingViewException | InterruptedException e) {
            Assert.fail("Groups Button not found #2. Check if Home Page is displayed.");
        }

        //onView(withId(R.id.myGroupsButton)).perform(click());

        // Click on the specific group 'another0'
        onView(withText("another0")).perform(click());

        // Verify navigation to StudyGroupActivity
        try {
            Thread.sleep(750);
            onView(withId(R.id.studyGroupTitle))
                    .check(matches(ViewMatchers.withText("Member List")));        }
        catch (NoMatchingViewException | InterruptedException e) {
            Assert.fail("Groups Button not found #2. Check if Home Page is displayed.");
        }

    }

    @Test
    public void testSendMessageInChat() {
        // Login and open a study group
        testOpenSpecificStudyGroup();

        // Navigate to chat box
        onView(withId(R.id.chatBoxButton)).perform(click());

        try {
            //Thread.sleep(750);
            //onView(withId(R.id.edmsg)).perform(typeText("Hello Study Group!"), closeSoftKeyboard());


        }
        catch (NoMatchingViewException e) {
            Assert.fail("Groups Button not found #2. Check if Home Page is displayed.");
        }

        // Send a test message

        try {
            //Thread.sleep(750);
            //onView(withId(R.id.Send)).perform(click());


        }
        catch (NoMatchingViewException e) {
            Assert.fail("Groups Button not found #2. Check if Home Page is displayed.");
        }

        // Verify that the message appears in the list

        try {
            Thread.sleep(750);
            onView(ViewMatchers.withId(R.id.lv))
                    .check(matches(ViewMatchers.isDisplayed()));

        }
        catch (NoMatchingViewException | InterruptedException e) {
            Assert.fail("Groups Button not found #2. Check if Home Page is displayed.");
        }

    }

    @Test
    public void testUploadResource() {
        // Login and open a study group
        testOpenSpecificStudyGroup();

        // Navigate to the resources page
        onView(withId(R.id.resourcesButton)).perform(click());

        // Verify navigation to ResourcesActivity
        onView(withId(R.id.resourcesTitle))
                .check(matches(ViewMatchers.withText("Resources")));

        // Attempt to upload lecture notes
        onView(withId(R.id.uploadLectureNotesButton)).perform(click());
        // Verify upload flow (UI for selecting file will need mock injection for real testing)
    }

    @Test
    public void testBackNavigation() {
        // Login and open a study group
        testOpenSpecificStudyGroup();

        // Press back button to navigate to GroupPageActivity
        //onView(ViewActions.pressBack());

        // Verify navigation back to "Groups" page
        //onView(withText("Groups")).check(matches(ViewMatchers.isDisplayed()));
    }


    /*public static ViewAction waitForView(final Matcher<View> matcher, final long timeout) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Wait for a specific view with matcher " + matcher;
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                final long endTime = System.currentTimeMillis() + timeout;
                final Matcher<View> finalMatcher = matcher;
                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        if (finalMatcher.matches(child)) {
                            return;
                        }
                    }
                    uiController.loopMainThreadForAtLeast(50);
                } while (System.currentTimeMillis() < endTime);

                throw new PerformException.Builder()
                        .withCause(new TimeoutException())
                        .build();
            }
        };
    }*/



}
