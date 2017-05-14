package comp4900.bcit.ca.washaf.userpage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Service;
import android.content.DialogInterface;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import comp4900.bcit.ca.washaf.CurrentOrder;
import comp4900.bcit.ca.washaf.R;

public class OrderActivity extends AppCompatActivity {

    private ImageView chooseDayIcon;
    private TextView  chooseDayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        chooseDayIcon = (ImageView) findViewById(R.id.choose_day_icon);
        chooseDayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });
        chooseDayText = (TextView) findViewById(R.id.choose_day_text);

    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        return true;

    }

    public void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
        builder.setIcon(R.drawable.icon_schedule);
        builder.setTitle("Select One Name:-");
        SimpleDateFormat curFormater = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        GregorianCalendar date = new GregorianCalendar();
        date.roll(Calendar.DAY_OF_YEAR, true);
        String[] dateStringArray = new String[7];
        for (int day = 0; day < 7; day++) {
            dateStringArray[day] = curFormater.format(date.getTime());
            date.roll(Calendar.DAY_OF_YEAR, true);
        }
        final ArrayAdapter<String>adapter = new ArrayAdapter<String>(OrderActivity.this, android.R.layout.simple_list_item_1, dateStringArray);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strName = adapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(OrderActivity.this);
                builderInner.setMessage(strName);
                builderInner.setTitle("Your Selected Item is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        chooseDayText.setText(strName);
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builder.show();
    }

    public static class ServiceRequestDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.order_confirmation_content).setTitle(R.string.order_confirmation_title);
            SimpleDateFormat curFormater = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
            GregorianCalendar date = new GregorianCalendar();
            date.roll(Calendar.DAY_OF_YEAR, true);
            String[] dateStringArray = new String[7];
            for (int day = 0; day < 7; day++) {
                dateStringArray[day] = curFormater.format(date.getTime());
                date.roll(Calendar.DAY_OF_YEAR, true);
            }
            final ArrayAdapter<String>adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, dateStringArray);
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String strName = adapter.getItem(which);
                    AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
                    builderInner.setMessage(strName);
                    builderInner.setTitle("Your Selected Item is");
                    builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,int which) {
                            dialog.dismiss();
                        }
                    });
                    builderInner.show();
                }
            });
//            builder.setPositiveButton(R.string.order_confirmation_ok, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
//                    db.writeNewOrder(auth.getUid(), new CurrentOrder(user.getFullName(), user.getAddress(), user.getPhoneNum(),
//                            user.getEmail(), orderButton.getText().toString(), currentDateTime,
//                            Long.parseLong(spin.getSelectedItem().toString())
//                    ));
//                    CustomerMainFrag.OrderSentDialog message = new CustomerMainFrag.OrderSentDialog();
//                    message.show(getFragmentManager(), "thank you message");
//                }
//            });
//            builder.setNegativeButton(R.string.order_confirmation_cancel, new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int id) {
//                    // User cancelled the dialog
//                }
//            });
            AlertDialog message = builder.create();
            return message;
        }
    }
}
