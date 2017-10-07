package share.fair.fairshare.activities.NewBillActivity;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import share.fair.fairshare.R;
import share.fair.fairshare.models.Calculator;
import share.fair.fairshare.models.Group;

/**
 * Created by niryo on 06/10/2017.
 */

public class BillLinesAdapter extends ArrayAdapter<List<Calculator.BillLine>> {
    private final List<Calculator.BillLine> billLines;
    private Context context;
    private Group group;
    private Double splitEvenShare = 0.0;
    private int viewInFocus;
    private int rowInFocus;
    private int cursorPosition;

    public BillLinesAdapter(Context context, List billLines, Group group) {
        super(context, -1, billLines);
        this.billLines = billLines;
        this.context = context;
        this.group = group;
    }

    public void updateShare(){
        try {
            this.splitEvenShare = Calculator.CalculateShares(this.billLines);
            Log.d("custom", "new share is: " +this.splitEvenShare);
            notifyDataSetChanged();
        } catch (Calculator.TotalPaidNotEqualsToTotalShareException e) {
            e.printStackTrace();
            return;
        } catch (Calculator.NotEnoughMoneyToPayTheBillException e) {
            e.printStackTrace();
            return;
        }
    }

    private void addListeners(ViewHolder viewHolder, final int position) {
        viewHolder.share.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Double newShare = null;
                try {
                    newShare = Double.parseDouble(editable.toString());
                }catch (Exception e) {
                    //Do Nothing
                }
                billLines.get(position).setShare(newShare);
                updateShare();
            }
        });

        viewHolder.amountPaid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Double newPaid = 0.0;
                try {
                     newPaid = Double.parseDouble(editable.toString());
                } catch (Exception e){
                    //doNothing
                }
                billLines.get(position).setAmountPaid(newPaid);
                updateShare();
            }
        });

        viewHolder.amountPaid.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                cursorPosition += 1;
                Log.d("custom","cursor pos: " + cursorPosition);

                return false;
            }
        });


        viewHolder.share.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if(isFocus){
                    viewInFocus = view.getId();
                    rowInFocus = position;
                }
            }
        });

        viewHolder.amountPaid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean isFocus) {
                if(isFocus){
                    viewInFocus = view.getId();
                    rowInFocus = position;
                }
            }
        });

    }

    private ViewHolder createViewHolder(View convertView, final int position) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.userName = convertView.findViewById(R.id.layout_new_bill_activity_user_row_user_name);
        viewHolder.amountPaid =  convertView.findViewById(R.id.layout_new_bill_activity_user_row_amount_paid);
        viewHolder.share = convertView.findViewById(R.id.layout_new_bill_activity_user_row_share);
        this.addListeners(viewHolder,position);
        return viewHolder;
    }

    private void requestFocusIfNeeded(ViewHolder viewHolder, int position) {
        if(rowInFocus == position && viewInFocus == R.id.layout_new_bill_activity_user_row_share) {
            viewHolder.share.requestFocus();
        }
        if(rowInFocus == position && viewInFocus == R.id.layout_new_bill_activity_user_row_amount_paid) {
            viewHolder.amountPaid.requestFocus();
            viewHolder.amountPaid.setSelection(this.cursorPosition);
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(this.context).inflate(R.layout.layout_new_bill_activity_user_row, null);
            viewHolder = createViewHolder(convertView, position);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String userName = group.findUserById(billLines.get(position).getUserId()).getName();
        viewHolder.userName.setText(userName);
        Double share = billLines.get(position).getShare();
        if(share == null) {
            viewHolder.share.setHint(splitEvenShare.toString());
        } else {
            viewHolder.share.setText(share.toString());
        }
        Double amountPaid = billLines.get(position).getAmountPaid();
        if(amountPaid == 0.0){
            viewHolder.amountPaid.setText("");
        } else {
            viewHolder.amountPaid.setText(billLines.get(position).getAmountPaid().toString());
        }
        viewHolder.amountPaid.setHint("0.0");
        requestFocusIfNeeded(viewHolder, position);
        return convertView;
    }

    private class ViewHolder {
        public TextView userName;
        public EditText amountPaid;
        public EditText share;
    }
}

