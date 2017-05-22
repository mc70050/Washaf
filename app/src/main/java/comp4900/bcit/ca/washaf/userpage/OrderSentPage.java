package comp4900.bcit.ca.washaf.userpage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import comp4900.bcit.ca.washaf.R;

/**
 * Shows the cofirmation message that an order has been successfully sent
 * to the store.
 */
public class OrderSentPage extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_sent_page);

        TextView message = (TextView) findViewById(R.id.back_message);
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
