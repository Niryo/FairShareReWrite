package share.fair.fairshare.activities;

import android.app.Application;

import share.fair.fairshare.services.FireBaseServerApi;

public class AppActivity extends Application {
    public  FireBaseServerApi cloudApi = new FireBaseServerApi() ;
}
