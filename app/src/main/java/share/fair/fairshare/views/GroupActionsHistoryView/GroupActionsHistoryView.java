package share.fair.fairshare.views.GroupActionsHistoryView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import share.fair.fairshare.R;
import share.fair.fairshare.activities.NewBillActivity.NewBillActivity;
import share.fair.fairshare.models.Group;


public class GroupActionsHistoryView extends LinearLayout {
    private Group group;
    private ListView listView;


    public GroupActionsHistoryView(final Context context, final Group group) {
        super(context);
        this.group = group;
        View rootView = inflate(context, R.layout.layout_group_actions_history, this);
        this.listView = rootView.findViewById(R.id.layout_group_actions_history_list);
        GroupActionsHistoryAdapter adapter = new GroupActionsHistoryAdapter(getContext(), this.group.getActions());
        this.listView.setAdapter(adapter);
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("custom", group.getActions().get(i).getDescription());
                Intent intent = new Intent(getContext(), NewBillActivity.class);
                intent.putExtra(NewBillActivity.ACTION_TO_EDIT_ID, group.getActions().get(i).getId());
                intent.putExtra(NewBillActivity.GROUP_ID_EXTRA, group.getId());
                getContext().startActivity(intent);
            }
        });
    }
}
