package share.fair.fairshare;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import share.fair.fairshare.activities.MainActivity;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.instanceOf;
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

    @Test
    public void clickOnGroup() {
        //click on a group and test if new activity action bar
        onView(withId(R.id.main_activity_action_bar_add_group))
                .perform(click());
        onView(withId(R.id.dialog_create_new_group_groupname)).perform(typeText("testGroupName"));
        onView(withId(R.id.dialog_create_new_group_yourname)).perform(typeText("someName"));
        onView(withText(R.string.create)).perform(click());
        onView(withText("testGroupName")).perform(click());
        onView(withId(R.id.group_activity_action_bar)).check(matches(isDisplayed()));
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.group_activity_action_bar))))
                .check(matches(withText("testGroupName")));
    }

    @Test
    public void completeInstrumentedOfflineTest() {
        //create new group:
        onView(withId(R.id.main_activity_action_bar_add_group))
                .perform(click());
        onView(withId(R.id.dialog_create_new_group_groupname)).perform(typeText("testGroupName"));
        onView(withId(R.id.dialog_create_new_group_yourname)).perform(typeText("someName"));
        onView(withText(R.string.create)).perform(click());
        onView(withId(R.id.dialog_create_new_group_groupname)).check(doesNotExist());
        onView(withText("testGroupName")).check(matches(isDisplayed()));

        //click on group to get inside, verify actionbar title contains the group name:
        onView(withText("testGroupName")).perform(click());
        onView(withId(R.id.group_activity_action_bar)).check(matches(isDisplayed()));
        onView(allOf(instanceOf(TextView.class), withParent(withId(R.id.group_activity_action_bar))))
                .check(matches(withText("testGroupName")));

        //add useres to the group:
        this.addUserToGroup("user1");
        this.addUserToGroup("user2");
        onView(withText("user1")).check(matches(isDisplayed()));
        onView(withText("user2")).check(matches(isDisplayed()));

        //check that users have ballance 0:
        onData(anything())
                .inAdapterView(withId(R.id.group_activity_users_list))
                .atPosition(0)
                .onChildView(withId(R.id.layout_group_activity_user_row_user_ballance))
                .check(matches(withText("0.0")));
    }


    private void addUserToGroup(String userName) {
        onView(withId(R.id.group_activity_action_bar_add_user)).perform(click());
        onView(withId(R.id.dialog_create_new_user_username)).perform(typeText(userName));
        onView(withText(R.string.create)).perform(click());
    }
}
