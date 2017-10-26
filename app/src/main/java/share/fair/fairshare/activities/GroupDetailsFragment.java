package share.fair.fairshare.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import share.fair.fairshare.R;
import share.fair.fairshare.activities.NewBillActivity.NewBillActivity;
import share.fair.fairshare.models.Group;
import share.fair.fairshare.models.User;

public class GroupDetailsFragment extends ListFragment {
    private ListView usersListView;
    private Group group;

    public GroupDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String groupId = this.getArguments().getString("groupId");
        this.group = ((AppActivity) getActivity().getApplication()).getGroupList().getGroupById(groupId);
        setListAdapter(new UserRowAdapter(getContext(), this.group.getUsers()));
    }

    public void notifyAdapterChange() {
        ((BaseAdapter) getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_group_details, container, false);
        view.findViewById(R.id.group_activity_floating_action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotToNewBillActivity = new Intent(getContext(), NewBillActivity.class);
                gotToNewBillActivity.putExtra(NewBillActivity.GROUP_ID_EXTRA ,group.getId());
                startActivity(gotToNewBillActivity);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
                convertView = getActivity().getLayoutInflater().inflate(R.layout.layout_group_activity_user_row, null);
            }
            TextView userNameText = convertView.findViewById(R.id.layout_group_activity_user_row_user_name);
            TextView userBallanceText = convertView.findViewById(R.id.layout_group_activity_user_row_user_ballance);
            userNameText.setText(users.get(position).getName());
            userBallanceText.setText(Double.toString(users.get(position).getBallance()));
            return convertView;
        }
    }
}