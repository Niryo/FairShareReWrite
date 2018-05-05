package share.fair.fairshare.activities;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.math.BigInteger;
import java.security.SecureRandom;

import share.fair.fairshare.Constants.FileNames;
import share.fair.fairshare.Constants.SharedPreferencesNames;
import share.fair.fairshare.services.FireBaseServerApi;

public class AppActivity extends Application {
    public FireBaseServerApi cloudApi;

    @Override
    public void onCreate() {
        super.onCreate();
        this.cloudApi = new FireBaseServerApi();
    }
//
//    private String generateInstallationIdIfNeeded(){
//        SharedPreferences sharedPreferences = getSharedPreferences(FileNames.SHARED_PREFERENCES, Context.MODE_PRIVATE);
//        String installationId = sharedPreferences.getString(SharedPreferencesNames.INSTALLATION_ID, "");
//        if(installationId.isEmpty()){
//            String newInstallationId = new BigInteger(130, new SecureRandom()).toString(32);
//            sharedPreferences.edit().putString(SharedPreferencesNames.INSTALLATION_ID, newInstallationId).commit();
//        }
//
//        return installationId;
//    }
}