package share.fair.fairshare.services;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by niryo on 26/09/2017.
 */
public class FireBaseServerApi {
    public interface FireBaseCallback {
        void onData(Object data);
    }

    public static GroupReference group(String groupKey) {
        return new GroupReference(groupKey);
    }

    public void addGroup(String groupKey, String groupName) {
        Log.d("nir","saving to firebase");
        FirebaseDatabase.getInstance()
                .getReference()
                .child(groupKey)
                .child("groupName")
                .setValue(groupName);
    }


    public static class GroupReference {
        private String groupKey;
        private GroupReference(String groupKey) {
            this.groupKey = groupKey;
        }


        public void getActionsSince(long timeStamp, final FireBaseCallback callback) {
            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child(this.groupKey)
                    .child("Actions")
                    .orderByChild("timeStamp")
                    .startAt(timeStamp)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            callback.onData(dataSnapshot.getValue().toString());
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("custom", "puk" + databaseError.toString());

                        }
                    });
        }
    }
}
