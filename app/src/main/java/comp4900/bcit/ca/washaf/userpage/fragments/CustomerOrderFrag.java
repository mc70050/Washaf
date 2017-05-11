package comp4900.bcit.ca.washaf.userpage.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import comp4900.bcit.ca.washaf.CurrentOrder;
import comp4900.bcit.ca.washaf.R;

/**
 * Created by Michael on 2017-05-09.
 */

public class CustomerOrderFrag extends Fragment {
    private static final String TAG = "CustomerOrderFrag";

    private Activity mActivity;
    private static FirebaseDatabase db;
    private static HashMap<String,CurrentOrder> curOrderList;
    private DatabaseReference curOrderRef;
    private static ListView currentOrders;
    private FirebaseUser auth;

    @Override
    public void onAttach(Activity act)
    {
        super.onAttach(act);

        this.mActivity = act;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.customer_order_frag, container, false);
        db = FirebaseDatabase.getInstance();
        currentOrders = (ListView) view.findViewById(R.id.current_orders);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        curOrderRef = db.getReference("current order").child(auth.getUid());
        FirebaseListAdapter<CurrentOrder> adapter = new FirebaseListAdapter<CurrentOrder>(getActivity(),
                CurrentOrder.class, R.layout.cust_current_order_process, curOrderRef) {
            @Override
            protected void populateView(View v, CurrentOrder model, int position) {
                ((TextView) v.findViewById(android.R.id.text1)).setText(model.getServiceType());
                ((TextView) v.findViewById(android.R.id.text2)).setText(model.getRequestedTime());
            }
        };
        currentOrders.setAdapter(adapter);




        return view;
    }

}

