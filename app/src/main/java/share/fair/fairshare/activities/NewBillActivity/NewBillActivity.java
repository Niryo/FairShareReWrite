package share.fair.fairshare.activities.NewBillActivity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import share.fair.fairshare.R;
import share.fair.fairshare.activities.AppActivity;
import share.fair.fairshare.databinding.ActivityNewBillBinding;
import share.fair.fairshare.models.Action;
import share.fair.fairshare.models.Calculator;
import share.fair.fairshare.models.Group;
import share.fair.fairshare.models.User;

public class NewBillActivity extends AppCompatActivity {
    public static final String USER_IDS_LIST_EXTRA = "USER_IDS_LIST_EXTRA";
    public static final String GROUP_ID_EXTRA = "GROUP_ID_EXTRA";

    private List<NewBillRowViewHolder> billRows = new ArrayList<>();
    private Group group;
    private ActivityNewBillBinding binding;
    private double autoShare = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.group = ((AppActivity) getApplication()).getGroupList().getGroupById(getIntent().getStringExtra(GROUP_ID_EXTRA));
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_new_bill);
        this.initActionBar();
        this.createRows();

    }

    private void initActionBar(){
        binding.newBillActivityActionBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(binding.newBillActivityActionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private double readAmountPaid(NewBillRowViewHolder viewHolder) {
        double amountPaid = 0.0;
        try {
            amountPaid = Double.parseDouble(viewHolder.amountPaid.getText().toString());
        } catch (Exception e) {
            //doNothing
        }
        return amountPaid;
    }

    private double readShare(NewBillRowViewHolder viewHolder) {
        double share = Double.NaN;
        try {
            share = Double.parseDouble(viewHolder.share.getText().toString());
        } catch (Exception e) {
            //doNothing
        }
        return share;
    }

    private void updateShares() {
        List<Calculator.BillLine> calculatorBillLines = new ArrayList<>();
        for (NewBillRowViewHolder rowViewHolder : this.billRows) {
            double amountPaid = this.readAmountPaid(rowViewHolder);
            double share = this.readShare(rowViewHolder);
            calculatorBillLines.add(new Calculator.BillLine(rowViewHolder.userId, amountPaid, share));
        }
        this.autoShare = 0.0;
        try {
            this.autoShare = Calculator.CalculateShares(calculatorBillLines);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (NewBillRowViewHolder rowViewHolder : this.billRows) {
            rowViewHolder.share.setHint(Double.toString(this.autoShare));
        }

    }


    private void createRows() {
        List<String> userIdsExtra = getIntent().getStringArrayListExtra(USER_IDS_LIST_EXTRA);
        List<User> usersInvolveInBill = new ArrayList<>();
        if (userIdsExtra == null) {
            usersInvolveInBill = this.group.getUsers();
        } else {
            //todo
        }

        for (User user : usersInvolveInBill) {
            View inflatedRow = this.getLayoutInflater().inflate(R.layout.layout_new_bill_activity_user_row, null);
            NewBillRowViewHolder rowViewHolder = new NewBillRowViewHolder(user.getId(), inflatedRow);
            rowViewHolder.userName.setText(user.getName());
            rowViewHolder.amountPaid.addTextChangedListener(new TextChangeListener());
            rowViewHolder.share.addTextChangedListener(new TextChangeListener());
            billRows.add(rowViewHolder);
            this.binding.newBillActivityList.addView(inflatedRow);
        }

    }

    private void createBill() {
        List<Action.Operation> operations = new ArrayList<>();
        for(NewBillRowViewHolder viewHolder: this.billRows){
            double amountPaid = this.readAmountPaid(viewHolder);
            double share = this.readShare(viewHolder);
            boolean isShareAutoCalculated = Double.isNaN(share);
            if(isShareAutoCalculated) {
                share = this.autoShare;
            }
            operations.add(new Action.Operation(viewHolder.userId, amountPaid, share, isShareAutoCalculated));
        }
        String description = this.binding.newBillActivityBillDescription.getText().toString();
        this.group.addAction(new Action(operations, "testCreatorName",description,true));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_bill_activity_action_bar_create_mode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_bill_activity_action_bar_create_mode_done:
                this.createBill();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private class TextChangeListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            updateShares();
        }
    }
}
