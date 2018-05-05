package share.fair.fairshare.services;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import share.fair.fairshare.Constants.GroupActionTypes;
import share.fair.fairshare.interfaces.IGroupAction;
import share.fair.fairshare.models.Group;
import share.fair.fairshare.models.GroupActions;
import share.fair.fairshare.models.PaymentAction;
import share.fair.fairshare.models.User;


/**
 * Created by niryo on 26/09/2017.
 */
public class FireBaseServerApi {
    public static class DataBaseNodes {
        public static String GROUP_NAME = "groupName";
        public static String GROUP_ACTIONS = "Actions";
        public static String TIME_STEMP = "timeStamp";
        public static String GROUP_ACTION_TYPE = "action";
        public static String ACTION_ID = "actionId";
        public static String USER_ID = "userId";
        public static String USER_NAME = "userName";
        public static String INSTALLATION_ID = "installationId";
        public static String JSON_PAYMENT_ACTION = "jsonAction";
    }


    public interface FireBaseCallback {
        void onData(Object data);
    }

    public void addGroup(String groupKey, String groupName) {
        Log.d("nir", "saving to firebase");
        FirebaseDatabase.getInstance()
                .getReference()
                .child(groupKey)
                .child(DataBaseNodes.GROUP_NAME)
                .setValue(groupName);
    }

    public void getActionsSince(final Group group, final FireBaseCallback callback) {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child(group.getKey())
                .child(DataBaseNodes.GROUP_ACTIONS)
                .orderByChild(DataBaseNodes.TIME_STEMP)
                .startAt(group.getLastSyncTime(), "fakeKeyToMakeTheStartAtNonInclusive")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<IGroupAction> groupActions = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String actionInstallationId =   child.child(DataBaseNodes.INSTALLATION_ID).getValue().toString();
                            String groupActionType = child.child(DataBaseNodes.GROUP_ACTION_TYPE).getValue().toString();
                            if(actionInstallationId.equals(group.getGroupInstallationId())) {
                                groupActions.add(getUpdateLastSyncTimestampAction(child));
                            } else if (groupActionType.equals(GroupActionTypes.NEW_PAYMENT_ACTION)) {
                                groupActions.add(getNewPaymentAction(child));
                            } else if(groupActionType.equals(GroupActionTypes.USER_ADDED_ACTION)) {
                                groupActions.add(getAddUserAction(child));
                            }
                        }
                        callback.onData(groupActions);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("custom", "puk" + databaseError.toString());

                    }
                });
    }

    private GroupActions.NewPaymentAction getNewPaymentAction(DataSnapshot dataSnapshot) {
        String id = dataSnapshot.child(DataBaseNodes.ACTION_ID).getValue().toString();
        String installationId = dataSnapshot.child(DataBaseNodes.INSTALLATION_ID).getValue().toString();
        long timeStamp = Long.parseLong(dataSnapshot.child(DataBaseNodes.TIME_STEMP).getValue().toString());
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject((String) dataSnapshot.child(DataBaseNodes.JSON_PAYMENT_ACTION).getValue());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        PaymentAction paymentAction = new PaymentAction(jsonObject);
        return new GroupActions.NewPaymentAction(id, timeStamp, installationId, paymentAction);
    }

    private GroupActions.UpdateLastSyncTimestampAction getUpdateLastSyncTimestampAction(DataSnapshot dataSnapshot){
        long timeStamp = Long.parseLong(dataSnapshot.child(DataBaseNodes.TIME_STEMP).getValue().toString());
        return new GroupActions.UpdateLastSyncTimestampAction(timeStamp);
    }

    private GroupActions.AddUserAction getAddUserAction(DataSnapshot dataSnapshot){
        String id = dataSnapshot.child(DataBaseNodes.USER_ID).getValue().toString();
        String installationId = dataSnapshot.child(DataBaseNodes.INSTALLATION_ID).getValue().toString();
        String userName = dataSnapshot.child(DataBaseNodes.USER_NAME).getValue().toString();
        long timeStamp = Long.parseLong(dataSnapshot.child(DataBaseNodes.TIME_STEMP).getValue().toString());
        return new GroupActions.AddUserAction(id, timeStamp, installationId, userName);
    }

    public void saveAddUserAction(Group group, User user) {
        Map<String, Object> addUserAction = new HashMap<>();
        addUserAction.put(DataBaseNodes.GROUP_ACTION_TYPE, GroupActionTypes.USER_ADDED_ACTION);
        addUserAction.put(DataBaseNodes.USER_ID, user.getId());
        addUserAction.put(DataBaseNodes.USER_NAME, user.getName());
        addUserAction.put(DataBaseNodes.INSTALLATION_ID, group.getGroupInstallationId());
        addUserAction.put(DataBaseNodes.TIME_STEMP, ServerValue.TIMESTAMP);

        FirebaseDatabase.getInstance()
                .getReference()
                .child(group.getKey())
                .child(DataBaseNodes.GROUP_ACTIONS)
                .push()
                .setValue(addUserAction);
    }

    public void savePaymentAction(Group group, PaymentAction paymentAction) {
        Map<String, Object> newPaymentAction = new HashMap<>();
        newPaymentAction.put(DataBaseNodes.GROUP_ACTION_TYPE, GroupActionTypes.NEW_PAYMENT_ACTION);
        newPaymentAction.put(DataBaseNodes.ACTION_ID, paymentAction.getId());
        newPaymentAction.put(DataBaseNodes.JSON_PAYMENT_ACTION, paymentAction.toJson().toString());
        newPaymentAction.put(DataBaseNodes.INSTALLATION_ID, group.getGroupInstallationId());
        newPaymentAction.put(DataBaseNodes.TIME_STEMP, ServerValue.TIMESTAMP);

        FirebaseDatabase.getInstance()
                .getReference()
                .child(group.getKey())
                .child(DataBaseNodes.GROUP_ACTIONS)
                .push()
                .setValue(newPaymentAction);
    }


    public void getGroupName(final String key, final FireBaseCallback callback) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(key)
                .child(DataBaseNodes.GROUP_NAME)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            callback.onData(null);
                        } else {
                            callback.onData(dataSnapshot.getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

}
