package share.fair.fairshare.GroupActionsHistory;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import share.fair.fairshare.R;
import share.fair.fairshare.models.Group;


public class GroupActionsHistoryFragment extends ListFragment {
    private Group group;

    public GroupActionsHistoryFragment() {
    }

    public static GroupActionsHistoryFragment CreateInstance(Group group) {
        GroupActionsHistoryFragment instance = new GroupActionsHistoryFragment();
        instance.setGroup(group);
        return instance;
    }

    private void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Log.d("custom", this.group.getActions().get(position).getDescription());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GroupActionsHistoryAdapter adapter = new GroupActionsHistoryAdapter(getContext(), this.group.getActions());
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_actions_history, container, false);
        return view;
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

}
