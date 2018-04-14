package share.fair.fairshare.activities.MainActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import share.fair.fairshare.R;
import share.fair.fairshare.models.GroupNameAndKey;


public class GroupNamesAdapter extends ArrayAdapter<List<GroupNameAndKey>> {
    private final List<GroupNameAndKey> groupNamesAndKeys;
    private Context context;

    public GroupNamesAdapter(Context context, List<GroupNameAndKey> groupNamesAndKeys) {
        super(context, -1, (List) groupNamesAndKeys);
        this.context = context;
        this.groupNamesAndKeys = groupNamesAndKeys;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(this.context).inflate(R.layout.layout_group_row, null);
        }
        TextView groupName = convertView.findViewById(R.id.layout_group_row_group_name);
        groupName.setText(this.groupNamesAndKeys.get(position).name);
        return convertView;
    }
}