package comp4900.bcit.ca.washaf;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michael Chen on 2017-05-01.
 * This class is in charge of accessing with database.
 *
 */

public class DBAccess {

    private static final String TAG = "DBAccess";
    private static int nextUserId = 1;
    private static List<User> userList;
    private FirebaseDatabase database;
    private DatabaseReference mRef;

    /**
     * Default constructor.
     * Initialize the database reference.
     */
    public DBAccess() {
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference("user");

        userList = new ArrayList<>();
        // Read from the database
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DATA CHANGE", "activated");
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                userList.clear();
                for (DataSnapshot snapShot : dataSnapshot.getChildren()) {
                    User user = snapShot.getValue(User.class);
                    userList.add(user);
                    Log.d(TAG, "user is " + user.getFirstName() + " " + user.getLastName());
                }
                Log.d(TAG, "there are " + userList.size() + " users");
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
        mRef = database.getReference(reference);
    }

    /**
     * Attempts to write data into the database.
     * @param content that will be written into the database
     * @return true if successful in writing data
     *         false if attempt failed
     */
    public boolean writeData(String content) {
        if (mRef != null) {
            mRef.setValue(content);
            return true;
        } else {
            Log.d(TAG, "Need to get reference first");
            return false;
        }
    }

    public void writeUser(User user) {
        String id = String.format("%08d", nextUserId);
        Log.d(TAG, "ID is: " + id);
        nextUserId++;
        mRef.child(id).setValue(user);
    }


}