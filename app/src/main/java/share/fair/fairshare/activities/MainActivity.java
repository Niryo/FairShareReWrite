package share.fair.fairshare.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import share.fair.fairshare.R;
import share.fair.fairshare.databinding.ActivityMainBinding;
import share.fair.fairshare.models.Group;
import share.fair.fairshare.models.GroupList;

public class MainActivity extends AppCompatActivity {
    private ListView groupsNamesListView;
    private GroupList groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.groupList = ((AppActivity)this.getApplication()).getGroupList();
        this.groupList.createNewGroup("bla");
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.mainActivityActionBar);
        this.groupsNamesListView = binding.mainActivityList;
        GroupNamesAdapter adapter = new GroupNamesAdapter(this, this.groupList.getGroups());
        this.groupsNamesListView.setAdapter(adapter);
        this.groupsNamesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Alert!!");
                alert.setMessage("Are you sure to delete record");
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do your work here
                        dialog.dismiss();

                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });

                alert.show();

                return true;
            }
        });

        this.groupsNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent goToNextActivity = new Intent(getApplicationContext(), GroupActivity.class);
                startActivity(goToNextActivity);
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
                        groupList.createNewGroup(groupName.getText().toString());
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_activity_action_bar_add_group:
                this.showCreateNewGroupDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class GroupNamesAdapter extends ArrayAdapter<List<Group>>{
        private final List<Group> groups;

        public GroupNamesAdapter(Context context, List groups) {
            super(context, -1, groups);
            this.groups = groups;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.layout_group_row, null);
            }
            TextView groupName = convertView.findViewById(R.id.layout_group_row_group_name);
            groupName.setText(this.groups.get(position).getName());
            return convertView;
        }
    }
}


