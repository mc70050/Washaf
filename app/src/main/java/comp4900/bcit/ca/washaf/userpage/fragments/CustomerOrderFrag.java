package comp4900.bcit.ca.washaf.userpage.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import comp4900.bcit.ca.washaf.userpage.OrderPage;

/**
 * Created by Michael on 2017-05-09.
 */

public class CustomerOrderFrag extends Fragment {
    private static final String TAG = "CustomerOrderFrag";
    private static final String TITLE = "Your Order Information";
    private static final String SERVICE = "Service: ";
    private static final String REQUESTED_TIME = "Requested Time: ";
    private static final String COMPLETED_TIME = "Completed Time: ";
    private static final String STATUS         = "Status: ";

    private Activity mActivity;
    private static FirebaseDatabase db;
    private static HashMap<String,CurrentOrder> curOrderList;
    private DatabaseReference curOrderRef;
    private static ListView currentOrders;
    private FirebaseUser auth;
    private Button orderButton;

    @Override
    public void onAttach(Activity act)
    {
        super.onAttach(act);

        this.mActivity = act;
    }

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.customer_order_frag, container, false);
        orderButton = (Button) view.findViewById(R.id.order_button);
        getActivity().setTitle(TITLE);
        db = FirebaseDatabase.getInstance();
        currentOrders = (ListView) view.findViewById(R.id.current_orders);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        curOrderRef = db.getReference("current order").child(auth.getUid());
        FirebaseListAdapter<CurrentOrder> adapter = new FirebaseListAdapter<CurrentOrder>(getActivity(),
                CurrentOrder.class, R.layout.cust_current_order_process, curOrderRef) {
            @Override
            protected void populateView(View v, CurrentOrder model, int position) {
                ((TextView) v.findViewById(R.id.text1)).setText(SERVICE + model.getServiceType());
                ((TextView) v.findViewById(R.id.text2)).setText(REQUESTED_TIME + model.getRequestedTime());
                ((TextView) v.findViewById(R.id.text3)).setText(STATUS + model.getStatus().toString());
            }
        };
        currentOrders.setAdapter(adapter);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), OrderPage.class);
                Bundle bun = new Bundle();
                bun.putSerializable("user", getArguments().getSerializable("user"));
                startActivity(intent.putExtras(bun));
                getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });


        return view;
    }

}

