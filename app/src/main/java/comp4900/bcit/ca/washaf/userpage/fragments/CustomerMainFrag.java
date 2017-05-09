package comp4900.bcit.ca.washaf.userpage.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    private Activity mActivity;
    private Spinner spin;
    private Button orderButton;
    private User user;
    private DBAccess db;

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
        db = new DBAccess();
        db.getCurrentOrderInfo();
        user = (User)getArguments().getSerializable("user");
        final FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();

        //do whatever you want here - like set text to display in your fragment
        spin = (Spinner) view.findViewById(R.id.bag_number);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.bag_number_array,
                android.R.layout.simple_list_item_1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        orderButton = (Button) view.findViewById(R.id.order_button);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentDateTime = DateFormat.getDateTimeInstance().format(new Date());
                if (Integer.parseInt(spin.getSelectedItem().toString()) > user.getNumOfBags()) {
                    Toast.makeText(view.getContext(), "You don't have this many bags", Toast.LENGTH_LONG).show();
                } else {
                    db.writeNewOrder(auth.getUid(), new CurrentOrder(user.getFullName(), user.getAddress(), user.getPhoneNum(),
                                                                     user.getEmail(), orderButton.getText().toString(), currentDateTime,
                                                                     Long.parseLong(spin.getSelectedItem().toString())
                                                                     ));
                }
            }
        });

        return view;
    }


}
