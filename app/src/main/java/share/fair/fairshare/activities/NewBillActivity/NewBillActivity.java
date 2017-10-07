package share.fair.fairshare.activities.NewBillActivity;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import share.fair.fairshare.R;
import share.fair.fairshare.activities.AppActivity;
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
            this.billLines.add(new Calculator.BillLine(id, 0.0, null));
        }
        this.billLinesListView.setAdapter(new BillLinesAdapter(this, billLines, this.group));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
