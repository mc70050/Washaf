package comp4900.bcit.ca.washaf.userpage.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.util.Date;

import comp4900.bcit.ca.washaf.CurrentOrder;
import comp4900.bcit.ca.washaf.DBAccess;
import comp4900.bcit.ca.washaf.R;
import comp4900.bcit.ca.washaf.User;

/**
 * Created by Michael on 2017-05-08.
 */

public class CustomerMainFrag extends Fragment {
    private final String TAG = "CustomerMainFrag";
    private static final String TITLE = "Place Orders Here";

    private Activity mActivity;
    private static Spinner spin;
    private static Spinner bagRequestSpin;
    private static Button orderButton;
    private static Button orderBagButton;
    private static User user;
    private static DBAccess db;
    private static FirebaseUser auth;
    private static RadioGroup group;

    @Override
    public void onAttach(Activity act)
    {
        super.onAttach(act);

        this.mActivity = act;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.customer_main_frag, container, false);
        getActivity().setTitle(TITLE);
        db = new DBAccess();
        user = (User)getArguments().getSerializable("user");
        auth = FirebaseAuth.getInstance().getCurrentUser();
        group = (RadioGroup) view.findViewById(R.id.radioService);


        //do whatever you want here - like set text to display in your fragment
        spin = (Spinner) view.findViewById(R.id.bag_number);
        bagRequestSpin = (Spinner) view.findViewById(R.id.request_bag_number);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.bag_number_array,
                android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        bagRequestSpin.setAdapter(adapter);
        orderButton = (Button) view.findViewById(R.id.order_button);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
                Log.d(TAG, user.getFullName() + " has " + user.getNumOfBags() + " bags");
                if (Integer.parseInt(spin.getSelectedItem().toString()) > user.getNumOfBags()) {
                    Toast.makeText(view.getContext(), "You don't have this many bags", Toast.LENGTH_LONG).show();
                } else {
                    ServiceRequestDialog dialog = new ServiceRequestDialog();
                    dialog.show(getFragmentManager(), "service request");
                }
            }
        });
        orderBagButton = (Button) view.findViewById(R.id.order_bag_button);
        orderBagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BagRequestDialog dialog = new BagRequestDialog();
                dialog.show(getFragmentManager(), "bag request");
            }
        });

        return view;
    }

    public static class BagRequestDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.order_confirmation_content).setTitle(R.string.order_confirmation_title);
            builder.setPositiveButton(R.string.order_confirmation_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());

                    OrderSentDialog message = new OrderSentDialog();
                    message.show(getFragmentManager(), "thank you message");
                }
            });
            builder.setNegativeButton(R.string.order_confirmation_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog message = builder.create();
            return message;
        }
    }

    public static class ServiceRequestDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            View button = group.findViewById(group.getCheckedRadioButtonId());
            final RadioButton radio = (RadioButton) group.getChildAt(group.indexOfChild(button));
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.order_confirmation_content).setTitle(R.string.order_confirmation_title);
            builder.setPositiveButton(R.string.order_confirmation_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());

                    OrderSentDialog message = new OrderSentDialog();
                    message.show(getFragmentManager(), "thank you message");
                }
            });
            builder.setNegativeButton(R.string.order_confirmation_cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog message = builder.create();
            return message;
        }
    }

    public static class OrderSentDialog extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.order_sent_content).setTitle(R.string.order_sent_title);
            AlertDialog message = builder.create();
            return message;
        }
    }
}
