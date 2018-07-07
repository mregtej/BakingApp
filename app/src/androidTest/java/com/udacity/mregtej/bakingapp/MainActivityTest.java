package com.udacity.mregtej.bakingapp;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.udacity.mregtej.bakingapp.ui.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule public ActivityTestRule<MainActivity> mainActivityActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup(){
        mainActivityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
        // Delay time for allowing the app to retrieve data from JSON response
        // TODO improve this mechanism
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {}

    }

    @Test
    public void gridView_checkNumberOfRecipes() {
        onView(withId(R.id.recipe_card_recyclerview)).check(new RecyclerViewItemCountAssertion(4));
    }

    @Test
    public void clickGridViewItem_checkRecipeNameTitleOnToolbar() {
        CharSequence title = InstrumentationRegistry.getTargetContext().getString(R.string.brownies_title);
        onView(withId(R.id.recipe_card_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(isAssignableFrom(Toolbar.class)).check(matches(withToolbarTitle(is(title))));
    }


    //--------------------------------------------------------------------------------|
    //                           Test Support Classes                                 |
    //--------------------------------------------------------------------------------|

    private static Matcher<Object> withToolbarTitle(
            final Matcher<CharSequence> textMatcher) {
        return new BoundedMatcher<Object, Toolbar>(Toolbar.class) {
            @Override public boolean matchesSafely(Toolbar toolbar) {
                return textMatcher.matches(toolbar.getTitle());
            }
            @Override public void describeTo(Description description) {
                description.appendText("with toolbar title: ");
                textMatcher.describeTo(description);
            }
        };
    }

    public class RecyclerViewItemCountAssertion implements ViewAssertion {
        private final int expectedCount;

        public RecyclerViewItemCountAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void check(View view, NoMatchingViewException noViewFoundException) {
            if (noViewFoundException != null) {
                throw noViewFoundException;
            }

            RecyclerView recyclerView = (RecyclerView) view;
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            assertThat(adapter.getItemCount(), is(expectedCount));
        }
    }

}
