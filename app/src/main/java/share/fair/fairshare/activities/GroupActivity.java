package share.fair.fairshare.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import share.fair.fairshare.R;
import share.fair.fairshare.activities.NewBillActivity.NewBillActivity;
import share.fair.fairshare.databinding.ActivityGroupBinding;
import share.fair.fairshare.models.Group;
import share.fair.fairshare.models.User;

/**
 * Created by niryo on 01/10/2017.
 */

public class GroupActivity extends AppCompatActivity {
    public static final String GROUP_ID_EXTRA = "GROUP_ID_EXTRA";
    private Group group;
    private ListView usersListView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGroupBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_group);
        String groupId = getIntent().getStringExtra(GROUP_ID_EXTRA);
        this.group = ((AppActivity) getApplication()).getGroupList().getGroupById(groupId);
        binding.groupActivityActionBar.setTitle(group.getName());
        getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        setSupportActionBar(binding.groupActivityActionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.groupActivityActionBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        //todo: delete:
        group.createUser("a");
        group.createUser("b");
        group.createUser("c");
        //=====================
        this.usersListView = binding.groupActivityUsersList;
        this.usersListView.setAdapter(new UserRowAdapter(this, group.getUsers()));
        binding.groupActivityFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotToNewBillActivity = new Intent(getApplicationContext(), NewBillActivity.class);
                gotToNewBillActivity.putExtra(NewBillActivity.GROUP_ID_EXTRA ,group.getId());
                startActivity(gotToNewBillActivity);
            }
        });
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
                        ((BaseAdapter) usersListView.getAdapter()).notifyDataSetChanged();
                    }
                }).create().show();

    }

    private class UserRowAdapter extends ArrayAdapter<List<User>> {
        private final List<User> users;

        public UserRowAdapter(Context context, List users) {
            super(context, -1, users);
            this.users = users;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.layout_group_activity_user_row, null);
            }
            TextView userNameText = convertView.findViewById(R.id.layout_group_activity_user_row_user_name);
            TextView userBallanceText = convertView.findViewById(R.id.layout_group_activity_user_row_user_ballance);
            userNameText.setText(users.get(position).getName());
            userBallanceText.setText(Double.toString(users.get(position).getBallance()));
            return convertView;
        }
    }
}
