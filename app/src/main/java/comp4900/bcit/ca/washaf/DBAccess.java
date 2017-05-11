package comp4900.bcit.ca.washaf;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by Michael Chen on 2017-05-01.
 * This class is in charge of accessing with database.
 *
 */

public class DBAccess {

    private static final String TAG = "DBAccess";
    private static HashMap<String,User> userList;
    private static HashMap<String,CurrentOrder> curOrderList;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private DatabaseReference groupRef;
    private DatabaseReference curOrderRef;

    /**
     * Default constructor.
     * Initialize the database reference.
     */
    public DBAccess() {
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference("user");
        userList = new HashMap<>();
        // Read from the database
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DATA CHANGE", "activated");
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                userList.clear();
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    User user = snapShot.getValue(User.class);
                    userList.put(snapShot.getKey(), user);
                }
                Log.d(TAG, "there are " + userList.size() + " users");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        groupRef = database.getReference("group");
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DATA CHANGE", "group");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void getCurrentOrderInfoForCustomer(String uid) {
        curOrderList = new HashMap<>();
        curOrderRef = database.getReference("current order").child(uid);
        curOrderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DATA CHANGE", "current order");
                Log.d("current Order", dataSnapshot.getChildrenCount() + "");
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                curOrderList.clear();
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    CurrentOrder order = snapShot.getValue(CurrentOrder.class);
                    curOrderList.put(snapShot.getKey(), order);
                    Log.d("current Order", curOrderList.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    /**
     * Load a reference from the database base on input.
     * @param reference name of reference
     */
    public void getRef(String reference) {
        userRef = database.getReference(reference);
    }

    public void writeUser(String uid, User user) {
        Log.d(TAG, "ID is: " + uid);
        userRef.child(uid).setValue(user);
    }

    public void writeUserToGroup(String uid, User user) {
        if (user.getType() == UserType.CUSTOMER.ordinal()) {
            groupRef.child("customer").child(uid).setValue(user);
        } else if (user.getType() == UserType.EMPLOYEE.ordinal()) {
            groupRef.child("employee").child(uid).setValue(user);
        } else if (user.getType() == UserType.ADMIN.ordinal()) {
            groupRef.child("admin").child(uid).setValue(user);
        } else if (user.getType() == UserType.TOP_ADMIN.ordinal()) {
            groupRef.child("top admin").child(uid).setValue(user);
        }
    }

    public void writeNewOrder(String uid, CurrentOrder order) {
        curOrderRef = database.getReference("current order");
        Log.d(TAG, "order is for " + order.getServiceType());
        curOrderRef.child(uid).child(order.getRequestedTime()).setValue(order);
    }

    public long getUserType(String uid) {
        Log.d(TAG, "there are " + userList.size() + " users");
        return userList.get(uid).getType();
    }

    public User getUser(String uid) {
        return userList.get(uid);
    }

    public HashMap<String, CurrentOrder> getCustomerCurrentOrders() {
        return curOrderList;
    }
}
