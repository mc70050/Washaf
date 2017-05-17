package comp4900.bcit.ca.washaf.userpage.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import comp4900.bcit.ca.washaf.R;
import comp4900.bcit.ca.washaf.User;

/**
 * Created by apple on 2017-05-11.
 */

public class CustomerAccountFrag extends Fragment {

    private TextView emailText;
    private TextView nameText;
    private TextView addressText;
    private TextView phoneText;
    private User     user;

    private static final String NAME    = "Name: ";
    private static final String EMAIL   = "Email: ";
    private static final String ADDRESS = "Address: ";
    private static final String PHONE   = "Phone: ";
    private static final String TITLE   = "Your Account Information";
    private static final String TAG     = "CustomerAccountFrag";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.customer_account_frag, container, false);
        getActivity().setTitle(TITLE);
        user        = (User) getArguments().getSerializable("user");
        emailText   = (TextView) view.findViewById(R.id.email_text);
        nameText    = (TextView) view.findViewById(R.id.fullname_text);
        addressText = (TextView) view.findViewById(R.id.address_text);
        phoneText   = (TextView) view.findViewById(R.id.phone_text);

        emailText.setPaintFlags(emailText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        nameText.setPaintFlags(nameText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        addressText.setPaintFlags(addressText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        phoneText.setPaintFlags(phoneText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        emailText.setText(EMAIL + user.getEmail());
        nameText.setText(NAME + user.getFullName());
        addressText.setText(ADDRESS + user.getAddress());
        phoneText.setText(PHONE + user.getPhoneNum());
        Log.d("phone", user.getPhoneNum());

        return view;
    }
}
