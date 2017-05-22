package comp4900.bcit.ca.washaf.userpage.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import comp4900.bcit.ca.washaf.CurrentOrder;
import comp4900.bcit.ca.washaf.R;
import comp4900.bcit.ca.washaf.User;
import comp4900.bcit.ca.washaf.userpage.OrderPage;

/**
 * Created by apple on 2017-05-18.
 */

public class CustomerOrderHistoryFrag extends Fragment {
    private static final String ADDRESS = "Address: ";
    private static final String ORDER_ID = "Order ID: ";
    private static final String DELIVERY_ADDRESS = "Delivery address: ";
    private static final String PICKUP_TYPE = "Pick up/Drop off: ";
    private static final String PICKUP_DAY = "Day of pick up: ";
    private static final String PICKUP_TIME = "Time of pick up: ";
    private static final String DELIVERY_TYPE = "Delivery/Pick up from store: ";
    private static final String DELIVERY_DAY = "Delivery day: ";
    private static final String DELIVERY_TIME = "Delivery Time: ";
    private static final String TITLE = "Your Order Information";
    private static final String SERVICE = "Service: ";
    private static final String PRICE = "Price: $";
    private static final String REQUESTED_TIME = "Requested Time: ";
    private static final String STATUS         = "Status: ";
    private static final String QUANTITY = "Quantity: ";

    private static FirebaseDatabase db;
    private static HashMap<String,CurrentOrder> curOrderList;
    private DatabaseReference curOrderRef;
    private static ListView currentOrders;
    private FirebaseUser auth;
    private Button orderButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.customer_order_history_frag, container, false);
        orderButton = (Button) view.findViewById(R.id.order_button);
        getActivity().setTitle(TITLE);
        db = FirebaseDatabase.getInstance();
        currentOrders = (ListView) view.findViewById(R.id.current_orders);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        curOrderRef = db.getReference("old order").child(auth.getUid());
        final FirebaseListAdapter<CurrentOrder> adapter = new FirebaseListAdapter<CurrentOrder>(getActivity(),
                CurrentOrder.class, R.layout.cust_current_order_process, curOrderRef) {
            @Override
            protected void populateView(View v, CurrentOrder model, int position) {
                ((ImageView) v.findViewById(R.id.list_logo)).setVisibility(View.GONE);
                ((TextView) v.findViewById(R.id.text1)).setText(SERVICE + model.getServiceType());
                ((TextView) v.findViewById(R.id.text2)).setText(REQUESTED_TIME + model.getRequestedTime());
                ((TextView) v.findViewById(R.id.text3)).setText(STATUS + model.getStatus().toString());
            }
        };
        currentOrders.setAdapter(adapter);
        currentOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showOrderDetailDialog(adapter.getItem(position));
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((User)getArguments().getSerializable("user")).getNumOfBags() > 0) {
                    Intent intent = new Intent(view.getContext(), OrderPage.class);
                    Bundle bun = new Bundle();
                    bun.putSerializable("user", getArguments().getSerializable("user"));
                    startActivity(intent.putExtras(bun));
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else {
                    Toast.makeText(v.getContext(), "Please order bags first", Toast.LENGTH_LONG).show();
                }
            }
        });


        return view;
    }

    private void showOrderDetailDialog(CurrentOrder order) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.radio_selected);
        builder.setTitle("Order Detail");
        if (order.getServiceType().equalsIgnoreCase("request for bags")) {
            builder.setMessage(ORDER_ID + order.getOrderId() + "\n\n"
                    + SERVICE + order.getServiceType() + "\n\n"
                    + QUANTITY + order.getQuantity() + "\n\n"
                    + PRICE + order.getPrice() + "\n\n"
                    + DELIVERY_TYPE + order.getDelivery_type() + "\n\n");
        } else {
            builder.setMessage(ORDER_ID + order.getOrderId() + "\n\n"
                    + SERVICE + order.getServiceType() + "\n\n"
                    + QUANTITY + order.getQuantity() + "\n\n"
                    + PRICE + order.getPrice() + "\n\n"
                    + PICKUP_TYPE + order.getPickup_type() + "\n\n"
                    + PICKUP_DAY + order.getPickup_day() + "\n\n"
                    + PICKUP_TIME + order.getPickup_time() + "\n\n"
                    + DELIVERY_TYPE + order.getDelivery_type() + "\n\n"
                    + DELIVERY_DAY + order.getDelivery_day() + "\n\n"
                    + DELIVERY_TIME + order.getDelivery_time() + "\n\n"
                    + (order.getDelivery_type().equalsIgnoreCase("delivery") ? (DELIVERY_ADDRESS + order.getDelivery_address() + "\n\n") : ""));
        }
        builder.show();
    }

}
