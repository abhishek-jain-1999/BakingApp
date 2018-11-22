package com.example.abhishek.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.abhishek.bakingapp.activities.MainActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsAnything.anything;

@RunWith(AndroidJUnit4.class)

public class MainActivityBasicTest {


    @Rule
    public ActivityTestRule<MainActivity> testRule = new ActivityTestRule<>(MainActivity.class);
    private static final String s = "Ingredients List";

    @Test
    public void clickRecipe_OpensDetailActivity() {

        onData(anything()).inAdapterView(withId(R.id.main_recycler_view_portrait)).atPosition(0).perform(click());
        onView(withId(R.id.open_activity_test)).check(matches(withText(s)));
    }

}
