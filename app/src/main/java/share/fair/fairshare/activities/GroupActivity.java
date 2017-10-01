package share.fair.fairshare.activities;

import android.app.ActivityGroup;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import share.fair.fairshare.R;
import share.fair.fairshare.databinding.ActivityGroupBinding;

/**
 * Created by niryo on 01/10/2017.
 */

public class GroupActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGroupBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_group);
        setSupportActionBar(binding.groupActivityActionBar);
//        getSupportActionBar().setTitle("blamos");

    }
}
