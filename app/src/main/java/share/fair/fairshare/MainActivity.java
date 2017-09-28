package share.fair.fairshare;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import share.fair.fairshare.services.FireBaseServerApi;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(share.fair.fairshare.R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_activity_action_bar);
        setSupportActionBar(myToolbar);
        ListView listView = (ListView) findViewById(R.id.main_activity_list);
        String[] data = new String[]{"test1", "test2"};
        MySimpleArrayAdapter adapter = new MySimpleArrayAdapter(this, data);
        listView.setAdapter(adapter);


        FireBaseServerApi
                .group("a13jpr16q5qhub9gg707g7jms51")
                .getActionsSince(0, new FireBaseServerApi.FireBaseCallback() {
                    @Override
                    public void onData(Object data) {
                        Log.w("custom", "hey2 " + data);
                    }
                });
//
        FireBaseServerApi.addGroup(("wow"));
        //=============================
//        ValueEventListener postListener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get Post object and use the values to update the UI
//                Log.w("custom","blaaa" + dataSnapshot.getValue().toString());
//                // ...
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w("custom", "loadPost:onCancelled", databaseError.toException());
//                // ...
//            }
//        };
//     DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
//
//
//        mDatabase.addValueEventListener(postListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(share.fair.fairshare.R.menu.main_activity_action_bar, menu);
        return true;
    }


    private class MySimpleArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        private final String[] values;

        public MySimpleArrayAdapter(Context context, String[] values) {
            super(context, -1, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout testView = new LinearLayout(this.context);
            TextView textView = new TextView(this.context);
            textView.setText("test");
            textView.setTextColor(getResources().getColor(share.fair.fairshare.R.color.colorPrimary));
            testView.addView(textView);

            return testView;
        }
    }
}


