package comp4900.bcit.ca.washaf.userpage.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import comp4900.bcit.ca.washaf.R;

/**
 * Created by Michael on 2017/5/15.
 */

public class OrderConfirmFrag extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.order_confirm_frag, container, false);
        return view;
    }
}
