package comp4900.bcit.ca.washaf;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

public class PurchaseActivity extends Activity {
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AccBRuQPf2OVPU8RmtJewWzJKr5qcAgqqxEFULaU4VJdifvJ_pss3ffWhbzwzFOh6SQPV0AAwLPxFMYW";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);
    Button payPal;
    private TextView paymentAmt;
    private String paymentAmount;
    boolean paid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        payPal = (Button) findViewById(R.id.payPal);
        paymentAmt = (TextView) findViewById(R.id.paymentAmt);
        paymentAmt.setText(getIntent().getExtras().getLong("total price") + "");
        payPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBuyPressed();
            }
        });
    }

    public void onBuyPressed() {
        paymentAmount = paymentAmt.getText().toString();
        PayPalPayment thingToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(PurchaseActivity.this, com.paypal.android.sdk.payments.PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal(String.valueOf(paymentAmount)), "CAD", "sample item",
                paymentIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.e("Show", confirm.toJSONObject().toString(4));
                        Log.e("Show", confirm.getPayment().toJSONObject().toString(4));
                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         */
                        Toast.makeText(getApplicationContext(), "PaymentConfirmation info received" +
                                " from PayPal", Toast.LENGTH_LONG).show();
                        paid = true;
                        TextView fp = (TextView) findViewById(R.id.finishPayment);
                        fp.setVisibility(View.VISIBLE);
                        TextView atp = (TextView) findViewById(R.id.AskToPay);
                        atp.setVisibility(View.GONE);
                        TextView pi = (TextView) findViewById(R.id.paymentInfo);
                        pi.setVisibility(View.GONE);
                        TextView pa = (TextView) findViewById(R.id.paymentAmt);
                        pa.setVisibility(View.GONE);
                        Button pp = (Button) findViewById(R.id.payPal);
                        pp.setVisibility(View.GONE);
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "an extremely unlikely failure" +
                                " occurred:", Toast.LENGTH_LONG).show();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Payment Canceled.", Toast.LENGTH_LONG).show();
            } else if (resultCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID) {
                Toast.makeText(getApplicationContext(), "An invalid Payment or PayPalConfiguration" +
                        " was submitted. Please see the docs.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onDestroy() {
        // Stop service when done
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if(paid==false){
            Toast.makeText(getApplicationContext(), "Please make your payment", Toast.LENGTH_LONG).show();
        } else {
            super.onBackPressed(); // Process Back key  default behavior.
            super.finish();
        }
    }
}