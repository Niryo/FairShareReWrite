package share.fair.fairshare.activities;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import share.fair.fairshare.R;
import share.fair.fairshare.databinding.ActivityNewBillBinding;
import share.fair.fairshare.models.Calculator;
import share.fair.fairshare.models.Group;
import share.fair.fairshare.models.User;

public class NewBillActivity extends AppCompatActivity {
    public static final String USER_IDS_LIST_EXTRA= "USER_IDS_LIST_EXTRA";
    public static final String GROUP_ID_EXTRA= "GROUP_ID_EXTRA";

    private ListView billLinesListView;
    private List<Calculator.BillLine> billLines = new ArrayList<>();
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.group = ((AppActivity)getApplication()).getGroupList().getGroupById(getIntent().getStringExtra(GROUP_ID_EXTRA));
        ActivityNewBillBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_new_bill);
        setSupportActionBar(binding.newBillActivityActionBar);
        getSupportActionBar().setTitle(R.string.new_bill_activity_action_bar_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.newBillActivityActionBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        this.billLinesListView = binding.newBillActivityList;
        List<String> userIdsExtra = getIntent().getStringArrayListExtra(USER_IDS_LIST_EXTRA);
        if(userIdsExtra == null) {
            userIdsExtra = new ArrayList<>();
            for(User user: this.group.getUsers()) {
               userIdsExtra.add(user.getId());
            }
        }

        for(String id: userIdsExtra) {
            this.billLines.add(new Calculator.BillLine(id, 0.0, 0.0));
        }
        this.billLinesListView.setAdapter(new NewBillActivity.UserRowAdapter(this, billLines));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private class UserRowAdapter extends ArrayAdapter<List<Calculator.BillLine>> {
        private final List<Calculator.BillLine> billLines;

        public UserRowAdapter(Context context, List billLines) {
            super(context, -1, billLines);
            this.billLines = billLines;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.layout_new_bill_activity_user_row, null);
            }
            TextView userNameText = convertView.findViewById(R.id.layout_new_bill_activity_user_row_user_name);
            EditText amountPaid = convertView.findViewById(R.id.layout_new_bill_activity_user_row_amount_paid);
            EditText share = convertView.findViewById(R.id.layout_new_bill_activity_user_row_share);
            String userName = group.findUserById(billLines.get(position).getUserId()).getName();
            userNameText.setText(userName);
            amountPaid.setText(billLines.get(position).getAmountPaid().toString());
            share.setText(billLines.get(position).getShare().toString());
            return convertView;
        }
    }
}
