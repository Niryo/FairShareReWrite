package share.fair.fairshare.activities.NewBillActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import share.fair.fairshare.R;

/**
 * Created by niryo on 11/10/2017.
 */

class NewBillRowViewHolder {
    final String userId;
    final TextView userName;
    final EditText amountPaid;
    final EditText share;

    NewBillRowViewHolder(String userId, View view) {
        this.userId = userId;
        this.userName = view.findViewById(R.id.layout_new_bill_activity_user_row_user_name);
        this.amountPaid = view.findViewById(R.id.layout_new_bill_activity_user_row_amount_paid);
        this.share = view.findViewById(R.id.layout_new_bill_activity_user_row_share);
    }
}