package share.fair.fairshare.views.GroupDetailsView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import share.fair.fairshare.R;
import share.fair.fairshare.models.User;

/**
 * Created by niryo on 26/10/2017.
 */

public class GroupDetailsAdapter extends ArrayAdapter<List<User>> {
    private final List<User> users;
    private final Context context;

    public GroupDetailsAdapter(Context context, List users) {
        super(context, -1, users);
        this.users = users;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_group_activity_user_row, null);
        }
        TextView userNameText = convertView.findViewById(R.id.layout_group_activity_user_row_user_name);
        TextView userBallanceText = convertView.findViewById(R.id.layout_group_activity_user_row_user_ballance);
        userNameText.setText(users.get(position).getName());
        userBallanceText.setText(Double.toString(users.get(position).getBallance()));
        return convertView;
    }
}