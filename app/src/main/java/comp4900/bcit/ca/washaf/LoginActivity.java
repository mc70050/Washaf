package comp4900.bcit.ca.washaf;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.ArrayList;
import java.util.HashSet;

import butterknife.ButterKnife;
import butterknife.Bind;

import comp4900.bcit.ca.washaf.userpage.*;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private static final int REQUEST_RESET  = 0;
    private static final String APP_NAME = "Washaf";
    private FirebaseAuth auth;
    private static DBAccess db;
    private SharedPreferences preferences;
    private ArrayList<String> emailList;

    @Bind(R.id.input_email)
                                AutoCompleteTextView _emailText;
    @Bind(R.id.input_password)  EditText _passwordText;
    @Bind(R.id.btn_login)       Button   _loginButton;
    @Bind(R.id.link_signup)     TextView _signupLink;
    @Bind(R.id.link_reset)      TextView _resetLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initializePreference();
        auth = FirebaseAuth.getInstance();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, emailList);
        _emailText.setThreshold(1);
        _emailText.setAdapter(adapter);
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });

        _resetLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                startActivityForResult(intent, REQUEST_RESET);
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
        db = new DBAccess();
    }

    /**
     * Login function that is called when login button is clicked on.
     * Data entered is validated by other helper functions.
     */
    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_DarkActionBar);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email    = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        // TODO: Implement your own authentication logic here.
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            progressDialog.dismiss();
                            checkIfEmailVerified();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            onLoginFailed();
                        }

                        // ...
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                startActivity(new Intent(getBaseContext(), TopAdminPage.class));

                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(false);
    }

    /**
     * Enable the login button to be clicked next time
     * the user is in the login page again.
     */
    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        FirebaseUser user = auth.getCurrentUser();
        savePreference();
        goToUserPage(user.getUid());
        finish();
    }

    /**
     * Show message for a failed attempt to login.
     * Enable login button to allow for another try.
     */
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    /**
     * Validation of login information.
     * @return true if all information matches a record in database
     *         false if one of the information does not match or does not exist in database
     */
    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            _passwordText.setError("must be more than 6 characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private void goToUserPage(String uid) {
        final UserType type = UserType.values()[(int)db.getUserType(auth.getCurrentUser().getUid())];
        switch (type) {
            case TOP_ADMIN:
                startActivity(new Intent(getBaseContext(), TopAdminPage.class).putExtra("user", db.getUser(uid)));
                break;
            case ADMIN:
                startActivity(new Intent(getBaseContext(), AdminPage.class).putExtra("user", db.getUser(uid)));
                break;
            case EMPLOYEE:
                startActivity(new Intent(getBaseContext(), EmployeePage.class).putExtra("user", db.getUser(uid)));
                break;
            case CUSTOMER:
                startActivity(new Intent(getBaseContext(), CustomerPage.class).putExtra("user", db.getUser(uid)));
                break;
            default:
                Toast.makeText(getBaseContext(), "User information type error", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void checkIfEmailVerified()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user.isEmailVerified())
        {
            // user is verified, so you can finish this activity or send user to activity which you want.
            onLoginSuccess();
        }
        else
        {
            // email is not verified, so just prompt the message to the user and restart this activity.
            // NOTE: don't forget to log out the user.
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(getBaseContext(), "Email verification not verified yet", Toast.LENGTH_LONG).show();
            //restart this activity

        }
    }

    private void initializePreference() {
        preferences = getSharedPreferences(APP_NAME, MODE_PRIVATE);
        emailList = new ArrayList<String>(preferences.getStringSet("email", new HashSet<String>()));
        Log.d("initialize", emailList.toString());
    }

    private void savePreference() {
        SharedPreferences.Editor editor = preferences.edit();
        String name = _emailText.getText().toString();
        if(!(emailList.contains(name))){
            //Adding input string into the name array-list
            emailList.add(name) ;
        }
        editor.putStringSet("email", new HashSet<String>(emailList));
        Log.d("save pref", emailList.toString());
        editor.commit();
    }
}
