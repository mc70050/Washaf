package comp4900.bcit.ca.washaf.userpage.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import comp4900.bcit.ca.washaf.CurrentOrder;
import comp4900.bcit.ca.washaf.OrderAdapter;
import comp4900.bcit.ca.washaf.OrderStatus;
import comp4900.bcit.ca.washaf.R;
import comp4900.bcit.ca.washaf.User;

/**
 * Created by apple on 2017-05-15.
 */

public class EmployeeMainFrag extends Fragment {

    private Button changeStatusButton;
    private boolean working_status;
    private static DatabaseReference workingRef;
    private static DatabaseReference orderRef;
    private static DatabaseReference curOrderRef;
    private User user;
    private static HashMap<String,CurrentOrder> curOrderList;
    private ListView list;
    private EmployeeOrderAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.employee_main_frag, container, false);
        curOrderList = new HashMap<>();
        list = (ListView) view.findViewById(R.id.list);
        user = (User) getArguments().getSerializable("user");
        working_status = false;
        changeStatusButton = (Button) view.findViewById(R.id.working_button);
        workingRef = FirebaseDatabase.getInstance().getReference("working employee");
        orderRef  = FirebaseDatabase.getInstance().getReference("assigned order").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int oldSize = curOrderList.size();
                curOrderList.clear();
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    CurrentOrder order = snapShot.getValue(CurrentOrder.class);
                    curOrderList.put(snapShot.getKey(), order);

                }
                adapter = new EmployeeOrderAdapter(curOrderList);

                list.setAdapter(adapter);
                if (curOrderList.size() > oldSize)
                    showNotification(view);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        changeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!working_status) {
                    startWorking();
                } else {
                    stopWorking();
                }
            }
        });
        return view;
    }

    public static void logout() {
        workingRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
    }

    private void stopWorking() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.employee_stop_working);
        builder.setPositiveButton(R.string.order_confirmation_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                workingRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                working_status = false;
                changeStatusButton.setText("Start Working");
            }
        });
        builder.setNegativeButton(R.string.order_confirmation_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.show();
    }

    private void startWorking() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.employee_start_working);
        builder.setPositiveButton(R.string.order_confirmation_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                workingRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user);
                working_status = true;
                changeStatusButton.setText("Stop Working");
            }
        });
        builder.setNegativeButton(R.string.order_confirmation_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.show();
    }

    public class EmployeeOrderAdapter extends OrderAdapter {

        public EmployeeOrderAdapter(Map<String, CurrentOrder> map) {
            super(map);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final View result;

            if (convertView == null) {
                result = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_assigned_order, parent, false);
            } else {
                result = convertView;
            }

            final Map.Entry<String, CurrentOrder> item = getItem(position);

            // TODO replace findViewById by ViewHolder
            ((TextView) result.findViewById(R.id.text1)).setText(item.getValue().getServiceType());
            ((TextView) result.findViewById(R.id.text2)).setText(item.getValue().getRequestedTime());
            ((TextView) result.findViewById(R.id.text3)).setText(item.getValue().getStatus().toString());
            ((Button) result.findViewById(R.id.update)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getValue().getStatus() != OrderStatus.COMPLETED)
                        showConfirmDialog(item.getValue());
                }
            });
            return result;
        }
    }

    private void showNotification(View view) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(view.getContext())
                        .setSmallIcon(R.drawable.radio_selected)
                        .setContentTitle("New Order!")
                        .setContentText("Please check your order list")
                        .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Hello World!"))
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setPriority(NotificationCompat.PRIORITY_MAX);
        NotificationManager mNotificationManager =
                (NotificationManager) view.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(100, mBuilder.build());
        Log.d("notify", "end of showNotification");
    }

    private void showConfirmDialog(final CurrentOrder item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.radio_selected);
        builder.setTitle("Confirm status update");
        builder.setPositiveButton(R.string.order_confirmation_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (item.getStatus() == OrderStatus.ASSIGNED) {
                    if (item.getServiceType().equalsIgnoreCase("request for bags")) {
                        curOrderRef = FirebaseDatabase.getInstance().getReference("current order").child(item.getCustomer_id()).child(item.getRequestedTime()).child("status");
                        curOrderRef.setValue(OrderStatus.IN_DELIVERY);
                        orderRef.child(item.getRequestedTime()).child("status").setValue(OrderStatus.IN_DELIVERY);
                    } else {
                        curOrderRef = FirebaseDatabase.getInstance().getReference("current order").child(item.getCustomer_id()).child(item.getRequestedTime()).child("status");
                        curOrderRef.setValue(OrderStatus.PICKED_UP);
                        orderRef.child(item.getRequestedTime()).child("status").setValue(OrderStatus.PICKED_UP);
                    }
                } else if (item.getStatus() == OrderStatus.PICKED_UP) {
                    curOrderRef = FirebaseDatabase.getInstance().getReference("current order").child(item.getCustomer_id()).child(item.getRequestedTime()).child("status");
                    curOrderRef.setValue(OrderStatus.IN_STORE);
                    orderRef.child(item.getRequestedTime()).child("status").setValue(OrderStatus.IN_STORE);
                    FirebaseDatabase.getInstance().getReference("assigned order").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(item.getRequestedTime()).removeValue();
                } else if (item.getStatus() == OrderStatus.READY_FOR_DELIVERY) {
                    curOrderRef = FirebaseDatabase.getInstance().getReference("current order").child(item.getCustomer_id()).child(item.getRequestedTime()).child("status");
                    curOrderRef.setValue(OrderStatus.IN_DELIVERY);
                    orderRef.child(item.getRequestedTime()).child("status").setValue(OrderStatus.IN_DELIVERY);
                } else if (item.getStatus() == OrderStatus.IN_DELIVERY) {
                    curOrderRef = FirebaseDatabase.getInstance().getReference("current order").child(item.getCustomer_id()).child(item.getRequestedTime()).child("status");
                    curOrderRef.setValue(OrderStatus.COMPLETED);
                    orderRef.child(item.getRequestedTime()).child("status").setValue(OrderStatus.COMPLETED);
                }
            }
        });
        builder.setNegativeButton(R.string.order_confirmation_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.show();
    }
}
