package share.fair.fairshare.views.GroupDetailsView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import share.fair.fairshare.R;
import share.fair.fairshare.models.User;

/**
 * Created by niryo on 26/10/2017.
 */

public class GroupDetailsAdapter extends ArrayAdapter<User> {
    private final List<User> users;
    private final Context context;
    private final GroupDetailsView groupDetailsView;

    public GroupDetailsAdapter(Context context,GroupDetailsView parent, List users) {
        super(context, -1, users);
        this.users = users;
        this.context = context;
        this.groupDetailsView = parent;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_group_activity_user_row, null);
            CheckBox checkBox = convertView.findViewById(R.id.layout_group_activity_user_checkbox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(((CheckBox) view).isChecked()) {
                        groupDetailsView.addSelectedPerson(users.get(position));

                    } else {
                        groupDetailsView.removeSelectedPerson(users.get(position));
                    }

                }
            });
        }
        TextView userNameText = convertView.findViewById(R.id.layout_group_activity_user_row_user_name);
        TextView userBallanceText = convertView.findViewById(R.id.layout_group_activity_user_row_user_ballance);
        userNameText.setText(getItem(position).getName());
        userBallanceText.setText(Double.toString(getItem(position).getBalance()));
        return convertView;
    }


}