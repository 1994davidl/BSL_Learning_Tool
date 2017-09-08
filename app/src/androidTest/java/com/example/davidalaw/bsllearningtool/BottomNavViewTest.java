package com.example.davidalaw.bsllearningtool;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.example.davidalaw.bsllearningtool.mActivities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

/**
 * UI Test for the bottom navigation user interactions
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class BottomNavViewTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void bottomNavViewTest() {
        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.home_nav), isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.favourites_nav), isDisplayed()));
        bottomNavigationItemView2.perform(click());

        ViewInteraction bottomNavigationItemView3 = onView(
                allOf(withId(R.id.quiz_menu_nav), isDisplayed()));
        bottomNavigationItemView3.perform(click());

        ViewInteraction bottomNavigationItemView4 = onView(
                allOf(withId(R.id.home_nav), isDisplayed()));
        bottomNavigationItemView4.perform(click());

        ViewInteraction bottomNavigationItemView5 = onView(
                allOf(withId(R.id.favourites_nav), isDisplayed()));
        bottomNavigationItemView5.perform(click());

    }

}
