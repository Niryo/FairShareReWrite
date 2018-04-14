package share.fair.fairshare.activities.GroupActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import share.fair.fairshare.models.User;
import share.fair.fairshare.services.DeviceStorageManager;
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
    public static final String GROUP_KEY_EXTRA = "GROUP_KEY_EXTRA";
    private Group group;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private GroupDetailsView groupDetailsView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGroupBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_group);
        String groupKey = getIntent().getStringExtra(GROUP_KEY_EXTRA);
        this.group = DeviceStorageManager.readGroup(getApplicationContext(), groupKey);
        binding.groupActivityActionBar.setTitle(group.getName());
        setSupportActionBar(binding.groupActivityActionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.groupActivityActionBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        viewPager = (ViewPager) findViewById(R.id.group_activity_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.group_activity_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        GroupActivityPagerAdapter adapter = new GroupActivityPagerAdapter(getBaseContext());
        Bundle args = new Bundle();
        String groupId = getIntent().getStringExtra(GROUP_KEY_EXTRA);
        args.putString("groupId", groupId);
        this.groupDetailsView = new GroupDetailsView(this, this.group);
        GroupActionsHistoryView groupActionsHistoryView =  new GroupActionsHistoryView(this.getBaseContext(), this.group);
        adapter.addView(groupDetailsView, getResources().getString(R.string.group_activity_tabs_group_details));
        adapter.addView(groupActionsHistoryView, getResources().getString(R.string.group_activity_tabs_group_actions_history));
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        recreate();
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

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_details_view_user_context_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.group_details_view_remove_user:
                final User userToRemove = group.getUsers().get(info.position);
                String message = "Do you really want to remove "
                        + userToRemove.getName() +
                        " from the group?\n" +
                        "(All user's debts within the group will be settled up)";
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.wait)
                        .setMessage(message)
                        .setNegativeButton(R.string.cancel,null)
                        .setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                group.removeUserById(userToRemove.getId());
                                groupDetailsView.notifyAdapterChange();
                            }
                        }).create().show();

                return true;
            default:
                return super.onContextItemSelected(item);
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
