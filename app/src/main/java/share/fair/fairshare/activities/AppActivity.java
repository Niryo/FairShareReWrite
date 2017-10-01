package share.fair.fairshare.activities;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import share.fair.fairshare.models.GroupList;

public class AppActivity extends Application {
    private GroupList groupList = new GroupList();

    public GroupList getGroupList() {
        return groupList;
    }
}
