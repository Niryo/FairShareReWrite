package share.fair.fairshare.services;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import share.fair.fairshare.Constants.GroupActionTypes;
import share.fair.fairshare.interfaces.IGroupAction;
import share.fair.fairshare.models.GroupActions;
import share.fair.fairshare.models.PaymentAction;


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
        public static String INSTALLATION_ID = "installationId";
        public static String JSON_PAYMENT_ACTION = "jsonAction";
    }

    public interface FireBaseCallback {
        void onData(Object data);
    }

    public static GroupReference group(String groupKey) {
        return new GroupReference(groupKey);
    }

    public void addGroup(String groupKey, String groupName) {
        Log.d("nir", "saving to firebase");
        FirebaseDatabase.getInstance()
                .getReference()
                .child(groupKey)
                .child(DataBaseNodes.GROUP_NAME)
                .setValue(groupName);
    }

    public void getActionsSince(long timeStamp, String groupKey, final FireBaseCallback callback) {
        FirebaseDatabase
                .getInstance()
                .getReference()
                .child(groupKey)
                .child(DataBaseNodes.GROUP_ACTIONS)
                .orderByChild(DataBaseNodes.TIME_STEMP)
                .startAt(timeStamp)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        List<IGroupAction> groupActions = new ArrayList<>();
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            String groupActionType = child.child(DataBaseNodes.GROUP_ACTION_TYPE).getValue().toString();
                            if (groupActionType.equals(GroupActionTypes.PAYMENT_ACTION)) {
                                groupActions.add(getNewPaymentAction(child));
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

    public void getGroupName(String key, final FireBaseCallback callback) {
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

    public static class GroupReference {
        private String groupKey;

        private GroupReference(String groupKey) {
            this.groupKey = groupKey;
        }


    }
}
