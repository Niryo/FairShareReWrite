package share.fair.fairshare.views.GroupDetailsView;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import share.fair.fairshare.R;
import share.fair.fairshare.activities.NewBillActivity.NewBillActivity;
import share.fair.fairshare.models.Group;
import share.fair.fairshare.models.User;

public class GroupDetailsView extends LinearLayout {
    private ListView usersListView;
    private Group group;
    private final List<User> selectedUsers = new ArrayList<>();
    private final Animation shakeAnimation;
    private final Animation scaleUpAnimation;
    private final Animation scaleDownAnimation;
    private final TextView personCountBadge;

    public GroupDetailsView(final Context context, final Group group) {
        super(context);
        this.group = group;
        View rootView = inflate(context, R.layout.layout_group_details, this);
        this.usersListView = rootView.findViewById(R.id.group_activity_list);
        this.usersListView.setAdapter(new GroupDetailsAdapter(getContext(), this, this.group.getUsers()));
        rootView.findViewById(R.id.group_activity_floating_action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotToNewBillActivity = new Intent(getContext(), NewBillActivity.class);
                gotToNewBillActivity.putExtra(NewBillActivity.GROUP_ID_EXTRA, group.getId());
                ArrayList userIdsListExtra = new ArrayList<>();
                for (User user : group.getUsers()) {
                    userIdsListExtra.add(user.getId());
                }
                gotToNewBillActivity.putStringArrayListExtra(NewBillActivity.USER_IDS_LIST_EXTRA, userIdsListExtra);
                context.startActivity(gotToNewBillActivity);
            }
        });
        shakeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        scaleUpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        scaleDownAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
        personCountBadge = rootView.findViewById(R.id.group_activity_person_count_badge);
    }

    public void updatePersonCountBadge() {
        this.personCountBadge.setText(Integer.toString(this.selectedUsers.size()));
        personCountBadge.startAnimation(shakeAnimation);
    }

    public void addSelectedPerson(User user) {
        this.selectedUsers.add(user);
        if (this.selectedUsers.size() == 1) {
            this.personCountBadge.setVisibility(VISIBLE);
            this.personCountBadge.startAnimation(scaleUpAnimation);
        } else {
            this.updatePersonCountBadge();
        }
    }

    public void removeSelectedPerson(User user) {
        this.selectedUsers.remove(user);
        if (this.selectedUsers.size() == 0) {
            personCountBadge.startAnimation(scaleDownAnimation);
        } else {
            this.updatePersonCountBadge();

        }
    }

    public void notifyAdapterChange() {
        ((BaseAdapter) this.usersListView.getAdapter()).notifyDataSetChanged();
    }
}