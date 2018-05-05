package share.fair.fairshare.activities.MainActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import share.fair.fairshare.R;
import share.fair.fairshare.activities.AppActivity;
import share.fair.fairshare.activities.GroupActivity.GroupActivity;
import share.fair.fairshare.models.Group;
import share.fair.fairshare.models.GroupNameAndKey;
import share.fair.fairshare.models.User;
import share.fair.fairshare.services.DeviceStorageManager;
import share.fair.fairshare.services.FireBaseServerApi;

import static java.lang.Thread.sleep;
import static share.fair.fairshare.activities.GroupActivity.GroupActivity.GROUP_KEY_EXTRA;

public class MainActivity extends AppCompatActivity {
    private ListView groupsNamesListView;
    private List<GroupNameAndKey> groupNamesAndKeysList;
    private FireBaseServerApi cloudApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.cloudApi = ((AppActivity) getApplication()).cloudApi;
        this.groupNamesAndKeysList = DeviceStorageManager.readGroupNamesAndKeys(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_activity_action_bar);
        setSupportActionBar(toolbar);
        this.groupsNamesListView = (ListView) findViewById(R.id.main_activity_list);
        GroupNamesAdapter adapter = new GroupNamesAdapter(this, this.groupNamesAndKeysList);
        this.groupsNamesListView.setAdapter(adapter);
        registerForContextMenu(this.groupsNamesListView);
        this.groupsNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent goToGroupActivity = new Intent(getApplicationContext(), GroupActivity.class);
                goToGroupActivity.putExtra(GROUP_KEY_EXTRA, groupNamesAndKeysList.get(i).key);
                startActivity(goToGroupActivity);
            }
        });
    }

    private void showCreateNewGroupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View dialogContent = this.getLayoutInflater().inflate(R.layout.dialog_create_new_group, null);

        builder.setTitle(R.string.create_new_group)
                .setView(dialogContent)
                .setNegativeButton(R.string.cancel,null)
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText groupName = dialogContent.findViewById(R.id.dialog_create_new_group_groupname);
                        EditText yourName = dialogContent.findViewById(R.id.dialog_create_new_group_yourname);
                        User newUser = new User(yourName.getText().toString());
                        Group newGroup = new Group(groupName.getText().toString());
                        newGroup.addUser(newUser);
                        groupNamesAndKeysList.add(new GroupNameAndKey(newGroup.getName(),newGroup.getKey()));
                        DeviceStorageManager.saveGroup(getApplicationContext(), newGroup);
                        DeviceStorageManager.saveGroupNamesAndKeys(getApplicationContext(), groupNamesAndKeysList);
                        cloudApi.saveAddUserAction(newGroup, newUser);
                        cloudApi.addGroup(newGroup.getKey(),newGroup.getName());
                        ((BaseAdapter) groupsNamesListView.getAdapter()).notifyDataSetChanged();
                    }
                }).create().show();

    }

    private void showGroupKeyErrorDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.error_group_key_dialog_title)
                .setMessage(R.string.error_group_key_dialog_message)
                .setNegativeButton(R.string.Dismiss,null)
                .create()
                .show();
    }

    private void showGroupKeySuccessDialog(String groupName) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.success_group_key_dialog_title)
                .setMessage(getResources().getString(R.string.success_group_key_dialog_message, groupName))
                .setNegativeButton(R.string.Dismiss,null)
                .create()
                .show();
    }

    private boolean isGroupAlreadyExist(Group group){
        for(GroupNameAndKey groupNameAndKey: this.groupNamesAndKeysList){
            if(groupNameAndKey.key.equals( group.getKey())){
                return true;
            }
        }
        return false;
    }

    private void showEnterGroupKeyDialog(){
        final View dialogContent = this.getLayoutInflater().inflate(R.layout.dialog_enter_group_key, null);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.enter_group_key_dialog_title)
                .setView(dialogContent)
                .setNegativeButton(R.string.cancel,null)
                .setPositiveButton(R.string.join, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button possitiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                possitiveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String groupKey = ((EditText) dialogContent.findViewById(R.id.dialog_enter_group_key)).getText().toString();
                        cloudApi.getGroupName(groupKey, new FireBaseServerApi.FireBaseCallback() {
                            @Override
                            public void onData(Object data) {
                                if(data == null) {
                                    showGroupKeyErrorDialog();
                                } else {
                                    Group addedGroup = new Group(groupKey, data.toString());
                                    if(!isGroupAlreadyExist(addedGroup)){
                                        groupNamesAndKeysList.add(new GroupNameAndKey(addedGroup.getName(),addedGroup.getKey()));
                                        DeviceStorageManager.saveGroupNamesAndKeys(getApplicationContext(), groupNamesAndKeysList);
                                        ((BaseAdapter) groupsNamesListView.getAdapter()).notifyDataSetChanged();
                                        DeviceStorageManager.saveGroup(getApplicationContext(), addedGroup);
                                    }
                                    dialog.dismiss();
                                    showGroupKeySuccessDialog(addedGroup.getName());
                                }
                            }
                        });
                    }
                });
            }
        });

        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_action_bar, menu);
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_remove_group_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.main_activity_remove_group:
                final GroupNameAndKey groupToRemove = groupNamesAndKeysList.get(info.position);
                String message = getResources().getString(R.string.remove_group_message, groupToRemove.name);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.wait)
                        .setMessage(message)
                        .setNegativeButton(R.string.cancel,null)
                        .setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                groupNamesAndKeysList.remove(groupToRemove);
                                DeviceStorageManager.saveGroupNamesAndKeys(getApplicationContext(), groupNamesAndKeysList);
                                ((BaseAdapter) groupsNamesListView.getAdapter()).notifyDataSetChanged();
                            }
                        }).create().show();

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_activity_action_bar_add_group:
                this.showCreateNewGroupDialog();
                return true;
            case R.id.main_activity_action_bar_enter_group_key:
                this.showEnterGroupKeyDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


