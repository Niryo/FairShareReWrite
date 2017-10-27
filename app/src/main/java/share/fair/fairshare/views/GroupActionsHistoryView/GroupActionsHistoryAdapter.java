package share.fair.fairshare.views.GroupActionsHistoryView;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import share.fair.fairshare.R;
import share.fair.fairshare.models.Action;


public class GroupActionsHistoryAdapter extends ArrayAdapter<Action> {
    private Context context;
    public GroupActionsHistoryAdapter(Context context, List<Action> actions) {
        super(context, 0, actions);
        this.context = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.layout_group_actions_history_row, null);
        }
        TextView time = convertView.findViewById(R.id.layout_group_actions_history_row_time);
        TextView description = convertView.findViewById(R.id.layout_group_actions_history_row_description);
        Action actoin = getItem(getCount() -1 - position);
        Date timeCreated = new Date(actoin.getTimeCreated());
        DateFormat format = new SimpleDateFormat("dd/MM/yy   HH:mm");
        time.setText(format.format(timeCreated));
        description.setText(actoin.getDescription());
        return convertView;
    }
}
