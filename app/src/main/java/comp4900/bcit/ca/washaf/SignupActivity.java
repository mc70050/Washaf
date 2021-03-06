package comp4900.bcit.ca.washaf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * Sign up page. Only used to create customer accounts.
 */
public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private static final double SERVICE_RANGE = 5;

    private HashMap<String, User> admins;


    @Bind(R.id.atv_places)          TextView _addressText;
    @Bind(R.id.input_first_name)    EditText _firstNameText;
    @Bind(R.id.input_last_name)     EditText _lastNameText;
    @Bind(R.id.input_email)         EditText _emailText;
    @Bind(R.id.input_phone)         EditText _phoneText;
    @Bind(R.id.input_password)      EditText _passwordText;
    @Bind(R.id.input_password_check) EditText _passwordCheckText;
    @Bind(R.id.btn_signup)          Button   _signupButton;
    @Bind(R.id.link_login)          TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        });
        admins = new HashMap<>();
        getAdmins();

        setTextWatcher();
    }

    /**
     * Method to initiate signup process.
     */
    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            Toast.makeText(getBaseContext(), "1 or more information is not valid", Toast.LENGTH_LONG).show();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Theme_AppCompat_DayNight_DarkActionBar);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String fName      = _firstNameText.getText().toString();
        final String lName      = _lastNameText.getText().toString();
        final String email      = _emailText.getText().toString();
        final String password   = _passwordText.getText().toString();
        final String phone      = _phoneText.getText().toString();
        final String address    = _addressText.getText().toString();


        // TODO: Implement your own signup logic here.
        final FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // For testing purpose only
//                        Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:"
//                                + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getApplicationContext(), "Login failed",
                                    Toast.LENGTH_SHORT).show();

                            if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                                Toast.makeText(getApplicationContext(), "User with Email id already exists",
                                        Toast.LENGTH_SHORT).show();
                            }
                            onSignupFailed();
                        } else {
                            Log.d(TAG, "sign up successful");
                            Log.d(TAG, auth.getCurrentUser().getUid() + " is UID");
                            saveUser(auth.getCurrentUser().getUid(), fName, lName, email, phone, address);
                            onSignupSuccess();
                        }
                    }
                });
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        sendVerificationEmail();
        finish();
    }

    /*
     * Save an user into database.
     */
    private void saveUser(String uid, String fName, String lName, String email, String phone, String address) {
        DBAccess db = new DBAccess();
        String id = findNearestStoreId(address);
        User user = new User(fName, lName, address, email, phone, UserType.CUSTOMER.ordinal(), (!id.isEmpty()) ? id : "");
        if (id.isEmpty()) {
            Toast.makeText(this, "Please contact Sahil through message system to be put under a store, sorry for the " +
            "inconvenience", Toast.LENGTH_LONG).show();
        }
        db.writeUser(uid, user);
        db.writeUserToGroup(uid, user);
    }

    /*
     * Finds and returns the UID of the administrator that owns a store
     * closest to the user that just signed up for an account.
     */
    private String findNearestStoreId(String newUserAddress) {
        String id = "";
        double nearestDist = SERVICE_RANGE;
        double tempDist;
        for (String key : admins.keySet()) {
            if ((tempDist = DistanceCalculator.CalculationByDistance(getLocationFromAddress(newUserAddress),
                                                         getLocationFromAddress(admins.get(key).getAddress()))) < nearestDist) {
                id = key;
                nearestDist = tempDist;
            }
        }
        return id;
    }

    /*
     * Takes a string of address and returns the latitude and longitude.
     */
    private LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this, Locale.getDefault());
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

            return p1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;
    }

    /*
     * Fill a HashMap<String, User> with all the admins that are in the database.
     * This is used in conjunction with finding the nearest store method.
     */
    private void getAdmins() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference adminRef = db.getReference("group").child("admin");
        adminRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DATA CHANGE", "get admin info");
                for ( DataSnapshot snap : dataSnapshot.getChildren()) {
                    User user = snap.getValue(User.class);
                    admins.put(snap.getKey(), user);
                }
                Log.d("findNearestStore", "there are " + admins.size() + " admins");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    /*
     * Validate all parameters entered by user trying to sign up.
     */
    private boolean validate() {
        if (checkFirstName(_firstNameText.getText().toString()) && checkLastName(_lastNameText.getText().toString())
                && checkEmail(_emailText.getText().toString())  && checkPassword(_passwordText.getText().toString())
                && checkPhone(_phoneText.getText().toString())  && checkRePassword(_passwordCheckText.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * First names have to have at least 3 characters. No other limitations.
     */
    private boolean checkFirstName(String s) {
        if (s.isEmpty() || s.length() < 3) {
            _firstNameText.setError("at least 3 characters");
            return false;
        } else {
            _firstNameText.setError(null);
            return true;
        }
    }

    /*
     * Last names have to have at least 2 characters. No other limitations.
     */
    private boolean checkLastName(String s) {
        if (s.isEmpty() || s.length() < 2) {
            _lastNameText.setError("at least 2 characters");
            return false;
        } else {
            _lastNameText.setError(null);
            return true;
        }
    }

    /*
     * Email is checked using built-in functions.
     * Has to be in correct format.
     * E.g. **@*.***
     */
    private boolean checkEmail(String s) {
        if (s.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
            _emailText.setError("enter a valid email address");
            return false;
        } else {
            _emailText.setError(null);
            return true;
        }
    }

    /*
     * Passwords need to be more than 6 characters.
     * This is because of Firebase's built-in restriction.
     */
    private boolean checkPassword(String s) {
        if (s.isEmpty() || s.length() < 6) {
            _passwordText.setError("password needs to be more than 6 characters");
            return false;
        } else {
            _passwordText.setError(null);
            return true;
        }
    }

    /*
     * Second column for entering password need to equal to first column.
     */
    private boolean checkRePassword(String s) {
        if (s.isEmpty() || !_passwordText.getText().toString().equals(s)) {
            _passwordCheckText.setError("Please enter an identical password");
            return false;
        } else {
            _passwordCheckText.setError(null);
            return true;
        }
    }

    /*
     * Phone number needs to be exactly 10 digits.
     */
    private boolean checkPhone(String s) {
        if (s.isEmpty() || s.length() != 10) {
            _phoneText.setError("Please enter a 10 digit phone number");
            return false;
        } else {
            _phoneText.setError(null);
            return true;
        }
    }

    /*
     * Add Text Watchers onto every EditText object to constantly validate
     * every column.
     */
    private void setTextWatcher() {
        _firstNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                checkFirstName(value);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {}
        });
        _lastNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                checkLastName(value);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {}
        });
        _emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                checkEmail(value);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {}
        });
        _passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                checkPassword(value);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {}
        });
        _passwordCheckText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                checkRePassword(value);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {}
        });
        _phoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String value = s.toString();
                checkPhone(value);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {}
        });
    }

    /**
     * Opens an intent to search for address.
     * This uses Google Place API.
     * @param view that's calling this method
     */
    public void searchAddress(final View view) {
        int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
        try {
            PlaceAutocomplete.IntentBuilder builder = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY);
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(Place.TYPE_COUNTRY).setCountry("CA")
                    .build();
            builder.setFilter(typeFilter);
            Intent intent = builder.build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                _addressText.setText(place.getAddress());
                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

    /*
     * Send a verification email to the user attempting to sign up.
     */
    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent


                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                            Toast.makeText(getBaseContext(), "Verification email sent, please check your email", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }
}
