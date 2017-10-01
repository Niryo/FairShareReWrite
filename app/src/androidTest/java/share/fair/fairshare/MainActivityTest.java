package share.fair.fairshare;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import share.fair.fairshare.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    private String mStringToBetyped;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void createNewGroup() {
        onView(withId(R.id.main_activity_action_bar_add_group))
                .perform(click());
        onView(withId(R.id.dialog_create_new_group_groupname)).perform(typeText("testGroupName"));
        onView(withId(R.id.dialog_create_new_group_yourname)).perform(typeText("someName"));
        onView(withText(R.string.create)).perform(click());
        onView(withId(R.id.dialog_create_new_group_groupname)).check(doesNotExist());
        onView(withText("testGroupName")).check(matches(isDisplayed()));
    }
}
