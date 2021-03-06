package com.example.davidalaw.bsllearningtool;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.davidalaw.bsllearningtool.mActivities.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Test Case:
 *
 * Favourite a sign - navigate to sign of choice >>> favourite the sign. Then navigate
 * to Favourites display (check if there) and select the just favourited sign.
 * Once favourite sign is display remove the favourite and navigate back to Favourites
 * display to check if gone.
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public class FavouriteASignTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void favouriteASignTest() {
        ViewInteraction appCompatTextView = onView(
                allOf(withId(android.R.id.text1), withText("Greetings"),
                        childAtPosition(
                                withId(R.id.category_list_view),
                                3),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(android.R.id.text1), withText("How are you?"),
                        childAtPosition(
                                withId(R.id.sign_list_view),
                                2),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withText("Video"), isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction appCompatCheckBox = onView(
                allOf(withId(R.id.favourite), isDisplayed()));
        appCompatCheckBox.perform(click());

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.favourites_nav), isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(android.R.id.text1), withText("How are you?"),
                        childAtPosition(
                                withId(R.id.favourite_list_view),
                                0),
                        isDisplayed()));
        appCompatTextView4.perform(click());

        ViewInteraction appCompatCheckBox2 = onView(
                allOf(withId(R.id.favourite), isDisplayed()));
        appCompatCheckBox2.perform(click());

        ViewInteraction bottomNavigationItemView2 = onView(
                allOf(withId(R.id.favourites_nav), isDisplayed()));
        bottomNavigationItemView2.perform(click());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
