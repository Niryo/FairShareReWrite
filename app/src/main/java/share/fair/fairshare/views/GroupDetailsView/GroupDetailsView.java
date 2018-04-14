package share.fair.fairshare.views.GroupDetailsView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
        ((Activity)context).registerForContextMenu(this.usersListView);
        this.usersListView.setAdapter(new GroupDetailsAdapter(getContext(), this, this.group.getUsers()));
        rootView.findViewById(R.id.group_activity_floating_action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToNewBillActivity = new Intent(getContext(), NewBillActivity.class);
                goToNewBillActivity.putExtra(NewBillActivity.GROUP_KEY_EXTRA, group.getKey());
                goToNewBillActivity.putExtra(NewBillActivity.USERS_IN_BILL_EXTRA, getUsersInvolvedInBill());
                context.startActivity(goToNewBillActivity);

            }
        });

        shakeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);
        scaleUpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_up);
        scaleDownAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_down);
        personCountBadge = rootView.findViewById(R.id.group_activity_person_count_badge);
    }

    private ArrayList<NewBillActivity.UserInvolvedInBill> getUsersInvolvedInBill() {
        ArrayList<NewBillActivity.UserInvolvedInBill> usersInvolvedInBills = new ArrayList<>();
        ArrayList<User> users;
        if(selectedUsers.size() == 0){
            users =  (ArrayList<User>) group.getUsers();
        } else {
            users = (ArrayList<User>) selectedUsers;
        }
        for (User user : users) {
            usersInvolvedInBills.add(new NewBillActivity.UserInvolvedInBill(user.getId(),user.getName(),"",""));
        }
        return usersInvolvedInBills;
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