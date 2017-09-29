package share.fair.fairshare;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private List<GroupNameEntry> groupNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.groupNames.add(new GroupNameEntry("test","bla"));
        setContentView(share.fair.fairshare.R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_activity_action_bar);
        setSupportActionBar(myToolbar);
        ListView listView = (ListView) findViewById(R.id.main_activity_list);
        GroupNamesAdapter adapter = new GroupNamesAdapter(this, this.groupNames);
        listView.setAdapter(adapter);


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
                        Log.d("custom", "ok! "+ groupName.getText());
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

    private class GroupNameEntry {
        public final String groupName;
        public final String groupKey;
        GroupNameEntry(String groupName, String groupKey){
            this.groupName= groupName;
            this.groupKey=groupKey;
        }
    }

    private class GroupNamesAdapter extends ArrayAdapter<List<GroupNameEntry>>{
        private final Context context;
        private final List<GroupNameEntry> groupNames;

        public GroupNamesAdapter(Context context, List groupNames) {
            super(context, -1, groupNames);
            this.context = context;
            this.groupNames = groupNames;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.layout_group_row, null);
            }
            TextView groupName = convertView.findViewById(R.id.layout_group_row_group_name);
            groupName.setText(this.groupNames.get(position).groupName);
            return convertView;
        }
    }
}


