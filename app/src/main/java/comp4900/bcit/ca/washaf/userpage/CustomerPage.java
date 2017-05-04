package comp4900.bcit.ca.washaf.userpage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import comp4900.bcit.ca.washaf.DBAccess;
import comp4900.bcit.ca.washaf.R;
import comp4900.bcit.ca.washaf.User;

public class CustomerPage extends AppCompatActivity {

    private TextView username;
    private static DBAccess db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_page);

        db = new DBAccess();
        User user = (User) getIntent().getSerializableExtra("user");
        username = (TextView) findViewById(R.id.textView2);
        username.setText("This is the customer page.");
        String first = user.getFirstName();
        String last = user.getLastName();
        setTitle("Thank you for using Washaf, " + first + " " + last);

    }
}

