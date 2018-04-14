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
        onView(withId(R.id.main_activity_action_bar_add_group)).perform(click());
        onView(withId(R.id.dialog_create_new_group_groupname)).perform(typeText("testGroupName"));
        onView(withId(R.id.dialog_create_new_group_yourname)).perform(typeText("someName"));
        onView(withText(R.string.create)).perform(click());
        onView(withId(R.id.dialog_create_new_group_groupname)).check(doesNotExist());
        onView(withText("testGroupName")).check(matches(isDisplayed()));
    }

//should contain groupDetailsTab
// should contain paymentHistoryTab
//payment history tab should contain list of actions with description, time,
//payment history tab should show latest actions first,
//payment history tab should open the newBillActivity in edit mode
//group tab should show in the action button how many people selected, if there are more than 1 selected
//group tab should not show selected counter badge if no one is selected
//clicking on the action button should switch to newBillActivity with the correct users
//clicking on the action button when no user is selected, should create new bill with all users
//should show userOptionsMenu on long click
//clicking on remove user should open a confirmation dialog.
//removing a user from the group should remove it from the list and split his debts/profit across all other group members.
}
