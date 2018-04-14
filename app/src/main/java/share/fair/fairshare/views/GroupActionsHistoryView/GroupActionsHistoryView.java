package share.fair.fairshare.views.GroupActionsHistoryView;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import share.fair.fairshare.R;
import share.fair.fairshare.activities.NewBillActivity.NewBillActivity;
import share.fair.fairshare.models.Action;
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
                Intent intent = new Intent(getContext(), NewBillActivity.class);
                Action action = ((Action)adapterView.getItemAtPosition(i));
                ArrayList<NewBillActivity.UserInvolvedInBill> usersInvolvedInBills = new ArrayList<>();
                for (Action.Operation operation: action.getOperations()) {
                    String share="";
                    if(!operation.isShareAutoCalculated()) {
                        share = Double.toString(operation.getShare());
                    }

                    String amountPaid="";
                    if(operation.getAmountPaid() >= 0.0000001) {
                        amountPaid = Double.toString(operation.getAmountPaid());
                    }
                    usersInvolvedInBills.add(new NewBillActivity.UserInvolvedInBill(
                            operation.getUserId(),
                            operation.getUserName(),
                            amountPaid,
                            share));
                }
                intent.putExtra(NewBillActivity.USERS_IN_BILL_EXTRA, usersInvolvedInBills);
                intent.putExtra(NewBillActivity.ACTION_TO_EDIT_ID, action.getId());
                intent.putExtra(NewBillActivity.GROUP_ID_EXTRA, group.getKey());
                getContext().startActivity(intent);
            }
        });
    }


}
