package share.fair.fairshare.activities.GroupActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import share.fair.fairshare.interfaces.IGroupAction;
import share.fair.fairshare.models.User;
import share.fair.fairshare.services.DeviceStorageManager;
import share.fair.fairshare.services.FireBaseServerApi;
import share.fair.fairshare.views.GroupActionsHistoryView.GroupActionsHistoryView;
import share.fair.fairshare.R;
import share.fair.fairshare.activities.AppActivity;
import share.fair.fairshare.views.GroupDetailsView.GroupDetailsView;
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
    private GroupActionsHistoryView  groupActionsHistoryView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        String groupKey = getIntent().getStringExtra(GROUP_KEY_EXTRA);
        this.group = DeviceStorageManager.readGroup(getApplicationContext(), groupKey);
        this.fetchGroupActionsInBackground();
        Toolbar toolBar = (Toolbar) findViewById(R.id.group_activity_action_bar);
        toolBar.setTitle(group.getName());
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
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
        groupActionsHistoryView =  new GroupActionsHistoryView(this.getBaseContext(), this.group);
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

            case R.id.group_activity_action_bar_show_group_key:
                AlertDialog.Builder showGroupKeyDialogBuilder = new AlertDialog.Builder(this);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("group key",group.getKey());
                clipboard.setPrimaryClip(clip);
                showGroupKeyDialogBuilder
                        .setTitle(R.string.group_key)
                        .setMessage(this.group.getKey())
                        .setPositiveButton(R.string.copy_to_clipboard, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getBaseContext(), "Group key has been copied to clipboard",
                                        Toast.LENGTH_LONG).show();
                            }
                        })
                        .create()
                        .show();
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
        Log.d("nir","" + item.getItemId() );
        switch(item.getItemId()) {
            case R.id.group_details_view_remove_user:
                final User userToRemove = group.getUsers().get(info.position);
                String message = "Do you really want to remove "
                        + userToRemove.getName() +
                        " from the group?\n" +
                        "(All user's debts within the group will be settled up)";
                AlertDialog.Builder removeUserDialogBuilder = new AlertDialog.Builder(this);
                removeUserDialogBuilder.setTitle(R.string.wait)
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
                        User newUser = new User(userName.getText().toString());
                        group.addUser(newUser);
                        DeviceStorageManager.saveGroup(getBaseContext(), group);
                        ((AppActivity) getApplication()).cloudApi.saveAddUserAction(group,newUser);
//                        ((BaseAdapter) usersListView.getAdapter()).notifyDataSetChanged();
                        groupDetailsView.notifyAdapterChange();
                    }
                }).create().show();

    }

    private void fetchGroupActionsInBackground(){
        new Thread(new Runnable() {
            public void run(){
                ((AppActivity)getApplication()).cloudApi.getActionsSince(group, new FireBaseServerApi.FireBaseCallback() {
                    @Override
                    public void onData(Object data) {
                        group.consumeGroupAction((List<IGroupAction>) data);
                        DeviceStorageManager.saveGroup(getApplicationContext(), group);
                        groupDetailsView.notifyAdapterChange();
                        groupActionsHistoryView.notifyAdapterChange();
                    }
                });
            }
        }).start();
    }
}
