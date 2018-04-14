package share.fair.fairshare.activities;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import share.fair.fairshare.models.GroupList;
import share.fair.fairshare.services.FireBaseServerApi;

public class AppActivity extends Application {
    private GroupList groupList = new GroupList(new FireBaseServerApi());
    public  FireBaseServerApi cloudApi = new FireBaseServerApi() ;

    public GroupList getGroupList() {
        return groupList;
    }
}
