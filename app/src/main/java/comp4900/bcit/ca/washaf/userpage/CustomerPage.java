package comp4900.bcit.ca.washaf.userpage;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import comp4900.bcit.ca.washaf.R;

public class CustomerPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_page);
    }

    public void display() {
        System.out.print(12321);
    }
}

