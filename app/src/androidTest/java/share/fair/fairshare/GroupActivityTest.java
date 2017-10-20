package share.fair.fairshare;

import android.content.Context;
import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import share.fair.fairshare.activities.GroupActivity.GroupActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static share.fair.fairshare.activities.GroupActivity.GroupActivity.GROUP_ID_EXTRA;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class GroupActivityTest {

    private String mStringToBetyped;

    @Rule
    public ActivityTestRule<GroupActivity> mActivityRule =
            new ActivityTestRule<GroupActivity>(GroupActivity.class) {
                @Override
                protected Intent getActivityIntent() {
                    Context targetContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
                    Intent result = new Intent(targetContext, GroupActivity.class);
                    result.putExtra(GROUP_ID_EXTRA, "testGroupId");
                    return result;
                }
            };

    @Before
    public void init() {

    }


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
