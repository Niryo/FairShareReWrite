package share.fair.fairshare.activities.GroupActivity;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import share.fair.fairshare.views.GroupActionsHistoryView.GroupActionsHistoryView;
import share.fair.fairshare.R;
import share.fair.fairshare.activities.AppActivity;
import share.fair.fairshare.views.GroupDetailsView.GroupDetailsView;
import share.fair.fairshare.databinding.ActivityGroupBinding;
import share.fair.fairshare.models.Group;

/**
 * Created by niryo on 01/10/2017.
 */

public class GroupActivity extends AppCompatActivity {
    public static final String GROUP_ID_EXTRA = "GROUP_ID_EXTRA";
    private Group group;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private GroupDetailsView groupDetailsView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGroupBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_group);
        String groupId = getIntent().getStringExtra(GROUP_ID_EXTRA);
        this.group = ((AppActivity) getApplication()).getGroupList().getGroupById(groupId);
        binding.groupActivityActionBar.setTitle(group.getName());
        setSupportActionBar(binding.groupActivityActionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.groupActivityActionBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        //todo: delete:
        group.createUser("a");
        group.createUser("b");
        group.createUser("c");
        //=====================
        viewPager = (ViewPager) findViewById(R.id.group_activity_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.group_activity_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        GroupActivityPagerAdapter adapter = new GroupActivityPagerAdapter(getBaseContext());
        Bundle args = new Bundle();
        String groupId = getIntent().getStringExtra(GROUP_ID_EXTRA);
        args.putString("groupId", groupId);
        this.groupDetailsView = new GroupDetailsView(getBaseContext(), this.group);
        GroupActionsHistoryView groupActionsHistoryView =  new GroupActionsHistoryView(this.getBaseContext(), this.group);
        adapter.addView(groupDetailsView, getResources().getString(R.string.group_activity_tabs_group_details));
        adapter.addView(groupActionsHistoryView, getResources().getString(R.string.group_activity_tabs_group_actions_history));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_activity_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.group_activity_action_bar_add_user:
                this.showCreateUserDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showCreateUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogContent = this.getLayoutInflater().inflate(R.layout.dialog_create_new_user, null);

        builder.setTitle(R.string.add_user)
                .setView(dialogContent)
                .setNegativeButton(R.string.cancel,null)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText userName = dialogContent.findViewById(R.id.dialog_create_new_user_username);
                        group.createUser(userName.getText().toString());
//                        ((BaseAdapter) usersListView.getAdapter()).notifyDataSetChanged();
                        groupDetailsView.notifyAdapterChange();
                    }
                }).create().show();

    }
}
