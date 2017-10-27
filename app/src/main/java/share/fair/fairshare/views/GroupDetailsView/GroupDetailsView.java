package share.fair.fairshare.views.GroupDetailsView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import share.fair.fairshare.R;
import share.fair.fairshare.activities.NewBillActivity.NewBillActivity;
import share.fair.fairshare.models.Group;

public class GroupDetailsView extends LinearLayout {
    private ListView usersListView;
    private Group group;

    public GroupDetailsView(final Context context, final Group group) {
        super(context);
        this.group = group;
        View rootView = inflate(context, R.layout.layout_group_details, this);
        this.usersListView = rootView.findViewById(R.id.group_activity_list);
        this.usersListView.setAdapter(new GroupDetailsAdapter(getContext(), this.group.getUsers()));
        rootView.findViewById(R.id.group_activity_floating_action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotToNewBillActivity = new Intent(getContext(), NewBillActivity.class);
                gotToNewBillActivity.putExtra(NewBillActivity.GROUP_ID_EXTRA, group.getId());
                context.startActivity(gotToNewBillActivity);
            }
        });
    }

    public void notifyAdapterChange() {
        ((BaseAdapter) this.usersListView.getAdapter()).notifyDataSetChanged();
    }
}