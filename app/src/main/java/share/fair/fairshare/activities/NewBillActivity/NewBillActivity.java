package share.fair.fairshare.activities.NewBillActivity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import share.fair.fairshare.R;
import share.fair.fairshare.activities.GroupActivity.GroupActivity;
import share.fair.fairshare.databinding.ActivityNewBillBinding;
import share.fair.fairshare.models.PaymentAction;
import share.fair.fairshare.services.Calculator;
import share.fair.fairshare.models.Group;
import share.fair.fairshare.services.DeviceStorageManager;

public class NewBillActivity extends AppCompatActivity {
    public static final String USERS_IN_BILL_EXTRA = "USERS_IN_BILL_EXTRA";
    public static final String GROUP_KEY_EXTRA = "GROUP_KEY_EXTRA";
    public static final String ACTION_TO_EDIT_ID = "ACTION_TO_EDIT_ID";

    private List<NewBillRowViewHolder> billRows = new ArrayList<>();
    private Group group;
    private ActivityNewBillBinding binding;
    private double autoShare = 0.0;
    private boolean isEditMode = false;
    private PaymentAction paymentActionToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.group = DeviceStorageManager.readGroup(getApplicationContext() ,getIntent().getStringExtra(GROUP_KEY_EXTRA));
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_new_bill);
        this.binding.newBillActivityBlockTouchOverlay.requestFocus();
        String actionIdToEdit = getIntent().getStringExtra(ACTION_TO_EDIT_ID);
        if (actionIdToEdit != null) {
            paymentActionToEdit = this.group.getActionById(actionIdToEdit);
            binding.newBillActivityBillDescription.setText(paymentActionToEdit.getDescription());
        } else {
            this.enterEditMode();
        }
        createRows();
        this.initActionBar();
    }

    private List<UserInvolvedInBill> getUsersInvolvedInBillFromAction(PaymentAction paymentAction) {
        List<UserInvolvedInBill> usersInvolvedInBill = new ArrayList<>();
        for (PaymentAction.Operation operation : paymentAction.getOperations()) {
            String userId = operation.getUserId();
            String userName = group.findUserById(userId).getName();
            String amountPaid = "";
            if(operation.getAmountPaid() != 0.0){
                amountPaid = Double.toString(operation.getAmountPaid());
            }
            String share = "";
            if(!operation.isShareAutoCalculated()) {
               share = Double.toString(operation.getShare());
            }
            usersInvolvedInBill.add(new UserInvolvedInBill(userId, userName, amountPaid, share));
        }
        return usersInvolvedInBill;
    }

    private void enterEditMode() {
        this.isEditMode = true;
        this.binding.newBillActivityBlockTouchOverlay.setVisibility(View.INVISIBLE);
        this.binding.newBillActivityContainer.setAlpha(1);
        this.binding.newBillActivityBillDescription.requestFocus();
    }

    private void initActionBar() {
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
        List<UserInvolvedInBill> usersInvolvedInBill = ( List<UserInvolvedInBill>) getIntent().getSerializableExtra(USERS_IN_BILL_EXTRA);
        for (UserInvolvedInBill userInvolvedInBill : usersInvolvedInBill) {
            View inflatedRow = this.getLayoutInflater().inflate(R.layout.layout_new_bill_activity_user_row, null);
            NewBillRowViewHolder rowViewHolder = new NewBillRowViewHolder(userInvolvedInBill.userId, inflatedRow);
            rowViewHolder.userName.setText(userInvolvedInBill.userName);
            rowViewHolder.amountPaid.setText(userInvolvedInBill.amountPaid);
            rowViewHolder.amountPaid.addTextChangedListener(new TextChangeListener());
            rowViewHolder.share.setText(userInvolvedInBill.share);
            rowViewHolder.share.addTextChangedListener(new TextChangeListener());
            billRows.add(rowViewHolder);
            this.binding.newBillActivityList.addView(inflatedRow);
            updateShares();
        }
    }

    private void createBill() {
        if((paymentActionToEdit != null)){
            group.cancelAction(paymentActionToEdit);
        }

        List<PaymentAction.Operation> operations = new ArrayList<>();
        for (NewBillRowViewHolder viewHolder : this.billRows) {
            double amountPaid = this.readAmountPaid(viewHolder);
            double share = this.readShare(viewHolder);
            boolean isShareAutoCalculated = Double.isNaN(share);
            if (isShareAutoCalculated) {
                share = this.autoShare;
            }
            //todo: clean this userNameShit
            operations.add(new PaymentAction.Operation(viewHolder.userId, viewHolder.userName.getText().toString(), amountPaid, share, isShareAutoCalculated));
        }
        String description = this.binding.newBillActivityBillDescription.getText().toString();
        this.group.addAction(new PaymentAction(operations, "testCreatorName", description, true));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_bill_activity_action_bar_create_mode, menu);
        if(this.paymentActionToEdit != null && !this.paymentActionToEdit.isEditable()) {
            menu.findItem(R.id.new_bill_activity_action_bar_done).setVisible(false);
            menu.findItem(R.id.new_bill_activity_action_bar_edit).setVisible(false);
        } else if (this.isEditMode) {
            menu.findItem(R.id.new_bill_activity_action_bar_done).setVisible(true);
            menu.findItem(R.id.new_bill_activity_action_bar_edit).setVisible(false);
        } else {
            menu.findItem(R.id.new_bill_activity_action_bar_done).setVisible(false);
            menu.findItem(R.id.new_bill_activity_action_bar_edit).setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_bill_activity_action_bar_done:
                this.createBill();
                Intent intent = new Intent(this, GroupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(GROUP_KEY_EXTRA, group.getKey());
                finish();
                getBaseContext().startActivity(intent);
                return true;

            case R.id.new_bill_activity_action_bar_edit:
                this.enterEditMode();
                this.invalidateOptionsMenu();
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

    //todo: clean this shit, amount paid and share should not be strings
    public static class UserInvolvedInBill implements Serializable {
        private final String userId;
        private final String userName;
        private final String amountPaid;
        private final String share;

        public UserInvolvedInBill(String userId, String userName, String amountPaid, String share) {
            this.userId = userId;
            this.userName = userName;
            this.amountPaid = amountPaid;
            this.share = share;
        }
    }
}
