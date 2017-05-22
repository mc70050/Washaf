package comp4900.bcit.ca.washaf.userpage.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.util.Date;

import comp4900.bcit.ca.washaf.CurrentOrder;
import comp4900.bcit.ca.washaf.DBAccess;
import comp4900.bcit.ca.washaf.PurchaseActivity;
import comp4900.bcit.ca.washaf.R;
import comp4900.bcit.ca.washaf.User;
import comp4900.bcit.ca.washaf.userpage.OrderSentPage;

/**
 * Created by Michael on 2017/5/15.
 */

public class OrderConfirmFrag extends Fragment {

    private TextView service_type;
    private TextView pickup_type;
    private TextView pickup_day;
    private TextView pickup_time;
    private TextView dropoff_store;
    private TextView num_bag;
    private TextView price;
    private TextView delivery_type;
    private TextView delivery_day;
    private TextView delivery_time;
    private TextView delivery_location;
    private TextView send_order;
    private DBAccess db;
    private User user;

    private boolean isDelivery = false;
    private boolean isDropOff  = false;

    public static final String SERVICE = "Service: ";
    public static final String PICKUP_DROP = "Pick Up/Drop Off: ";
    public static final String PICKUP_DAY = "Pick Up/Drop Off Day: ";
    public static final String PICKUP_TIME = "Pick Up/Drop Off Time: ";
    public static final String DROP_STORE = "Drop Off Store: ";
    public static final String NUM_BAG = "Number of bags: ";
    public static final String ESTIMATED_PRICE = "Estimated Price: ";
    public static final String DELIVERY_TYPE = "Delivery/Pick Up: ";
    public static final String DELIVERY_DAY = "Delivery/Pick Up Day: ";
    public static final String DELIVERY_TIME = "Delivery/Pick Up Time: ";
    public static final String DELIVERY_LOCATION = "Delivery/Pick Up Address: ";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.order_confirm_frag, container, false);
        db = new DBAccess();

        service_type = (TextView) view.findViewById(R.id.service_type);
        pickup_type = (TextView) view.findViewById(R.id.pick_up_type);
        pickup_day = (TextView) view.findViewById(R.id.pick_up_day);
        pickup_time = (TextView) view.findViewById(R.id.pick_up_time);
        dropoff_store = (TextView) view.findViewById(R.id.drop_off_store);
        num_bag = (TextView) view.findViewById(R.id.num_bag);
        price = (TextView) view.findViewById(R.id.price);
        delivery_type = (TextView) view.findViewById(R.id.delivery_type);
        delivery_day = (TextView) view.findViewById(R.id.delivery_day);
        delivery_time = (TextView) view.findViewById(R.id.delivery_time);
        delivery_location = (TextView) view.findViewById(R.id.delivery_location);
        send_order = (TextView) view.findViewById(R.id.send_order_text);

        service_type.setText(SERVICE + getArguments().getString("service"));
        pickup_type.setText(PICKUP_DROP + getArguments().get("pick up service"));
        if (getArguments().get("pick up service").toString().equalsIgnoreCase("drop off")) {
            pickup_day.setVisibility(View.GONE);
            pickup_time.setVisibility(View.GONE);
            isDropOff = true;
        }
        pickup_day.setText(PICKUP_DAY + getArguments().getString("pick up day"));
        pickup_time.setText(PICKUP_TIME + getArguments().getString("pick up time"));
        if (getArguments().getString("drop off location") != null) {
            dropoff_store.setVisibility(View.VISIBLE);
            dropoff_store.setText(DROP_STORE + getArguments().getString("drop off location"));
        }
        num_bag.setText(NUM_BAG + getArguments().getString("quantity"));
        price.setText(ESTIMATED_PRICE + getPrice());
        delivery_type.setText(DELIVERY_TYPE + getArguments().getString("delivery service"));
        delivery_day.setText(DELIVERY_DAY + getArguments().getString("delivery day"));
        delivery_time.setText(DELIVERY_TIME + getArguments().getString("delivery time"));
        if (getArguments().getString("delivery location") != null) {
            delivery_location.setVisibility(View.VISIBLE);
            delivery_location.setText(DELIVERY_LOCATION + getArguments().getString("delivery location"));
            isDelivery = true;
        }
        user = (User) getArguments().getSerializable("user");
        send_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
                String id = (db.getOrderId() + 1) + "";
                CurrentOrder newOrder;
                Intent pay = new Intent(getActivity(), PurchaseActivity.class);
                if (!isDelivery) {
                    if (isDropOff) {
                        newOrder = new CurrentOrder(user.getFullName(),
                                user.getAddress(), user.getPhoneNum(), user.getEmail(), getArguments().getString("service"),
                                currentDateTime, Long.parseLong(getArguments().getString("quantity")), getArguments().getString("pick up service"),
                                getArguments().getString("delivery service"), getArguments().getString("delivery day"),
                                getArguments().getString("delivery time"), id);
                    } else {
                        newOrder = new CurrentOrder(user.getFullName(),
                                user.getAddress(), user.getPhoneNum(), user.getEmail(), getArguments().getString("service"),
                                currentDateTime, Long.parseLong(getArguments().getString("quantity")), getArguments().getString("pick up service"),
                                getArguments().getString("pick up day"), getArguments().getString("pick up time"), getArguments().getString("delivery service"),
                                getArguments().getString("delivery day"), getArguments().getString("delivery time"), id);
                    }
                    db.writeNewOrder(FirebaseAuth.getInstance().getCurrentUser().getUid(), newOrder);
                    pay.putExtra("total price", newOrder.getPrice());
                } else {
                    if (isDropOff) {
                        newOrder = new CurrentOrder(user.getFullName(),
                                user.getAddress(), user.getPhoneNum(), user.getEmail(), getArguments().getString("service"),
                                currentDateTime, Long.parseLong(getArguments().getString("quantity")), getArguments().getString("pick up service"),
                                getArguments().getString("delivery service"),
                                getArguments().getString("delivery day"), getArguments().getString("delivery time"),
                                getArguments().getString("delivery location"), id);
                    } else {
                        newOrder = new CurrentOrder(user.getFullName(),
                                user.getAddress(), user.getPhoneNum(), user.getEmail(), getArguments().getString("service"),
                                currentDateTime, Long.parseLong(getArguments().getString("quantity")), getArguments().getString("pick up service"),
                                getArguments().getString("pick up day"), getArguments().getString("pick up time"), getArguments().getString("delivery service"),
                                getArguments().getString("delivery day"), getArguments().getString("delivery time"),
                                getArguments().getString("delivery location"), id);
                    }
                    db.writeNewOrder(FirebaseAuth.getInstance().getCurrentUser().getUid(), newOrder);
                    pay.putExtra("total price", newOrder.getPrice());
                }
                db.setOrderId(id);
                Intent intent = new Intent(getActivity(), OrderSentPage.class);
                startActivity(intent);
                getActivity().finish();
                Toast.makeText(view.getContext(), "Order sent", Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }

    private long getPrice() {
        return Long.parseLong(getArguments().getString("quantity")) * 35;
    }
}
