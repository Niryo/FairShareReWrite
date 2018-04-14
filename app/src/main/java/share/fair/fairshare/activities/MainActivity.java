package share.fair.fairshare.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import share.fair.fairshare.R;
import share.fair.fairshare.activities.GroupActivity.GroupActivity;
import share.fair.fairshare.databinding.ActivityMainBinding;
import share.fair.fairshare.models.Group;
import share.fair.fairshare.models.GroupNameAndKey;
import share.fair.fairshare.services.DeviceStorageManager;

import static share.fair.fairshare.activities.GroupActivity.GroupActivity.GROUP_ID_EXTRA;

public class MainActivity extends AppCompatActivity {
    private static final String GROUP_NAMES_FILE = "groupNames";
    private ListView groupsNamesListView;
    private List<GroupNameAndKey> groupNamesAndKeysList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.readGroupNamesFromDeviceStorage();
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.mainActivityActionBar);
        this.groupsNamesListView = binding.mainActivityList;
        GroupNamesAdapter adapter = new GroupNamesAdapter(this, this.groupNamesAndKeysList);
        this.groupsNamesListView.setAdapter(adapter);
        registerForContextMenu(this.groupsNamesListView);
        this.groupsNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent goToGroupActivity = new Intent(getApplicationContext(), GroupActivity.class);
                goToGroupActivity.putExtra(GROUP_ID_EXTRA, groupNamesAndKeysList.get(i).key);
                startActivity(goToGroupActivity);
            }
        });

//        FireBaseServerApi
//                .group("a13jpr16q5qhub9gg707g7jms51")
//                .getActionsSince(0, new FireBaseServerApi.FireBaseCallback() {
//                    @Override
//                    public void onData(Object data) {
//                        Log.w("custom", "hey2 " + data);
//                    }
//                });
//        FireBaseServerApi.addGroup(("wow"));

    }

    private void readGroupNamesFromDeviceStorage(){
        try {
            this.groupNamesAndKeysList = (List<GroupNameAndKey>) DeviceStorageManager.readObject(getBaseContext(),GROUP_NAMES_FILE);
        } catch (FileNotFoundException e) {
            this.groupNamesAndKeysList = new ArrayList<>();
            this.updateGroupNamesOnDeviceStorage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateGroupNamesOnDeviceStorage(){
        try {
            DeviceStorageManager.writeObject(getApplicationContext(),GROUP_NAMES_FILE, this.groupNamesAndKeysList);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                        Group newGroup = new Group(groupName.getText().toString());
                        groupNamesAndKeysList.add(new GroupNameAndKey(newGroup.getName(),newGroup.getKey()));
                        updateGroupNamesOnDeviceStorage();
                        ((AppActivity) getApplication()).cloudApi.addGroup(newGroup.getKey(),newGroup.getName());
                        ((BaseAdapter) groupsNamesListView.getAdapter()).notifyDataSetChanged();
                    }
                }).create().show();

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
                String message = "Do you really want to remove " + groupToRemove.name;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.wait)
                        .setMessage(message)
                        .setNegativeButton(R.string.cancel,null)
                        .setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                groupNamesAndKeysList.remove(groupToRemove);
                                updateGroupNamesOnDeviceStorage();
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class GroupNamesAdapter extends ArrayAdapter<List<GroupNameAndKey>>{
        private final List<GroupNameAndKey> groupNamesAndKeys;

        public GroupNamesAdapter(Context context, List<GroupNameAndKey> groupNamesAndKeys) {
            super(context, -1, (List) groupNamesAndKeys);
            this.groupNamesAndKeys = groupNamesAndKeys;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.layout_group_row, null);
            }
            TextView groupName = convertView.findViewById(R.id.layout_group_row_group_name);
            groupName.setText(this.groupNamesAndKeys.get(position).name);
            return convertView;
        }
    }
}


