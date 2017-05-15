package comp4900.bcit.ca.washaf.userpage.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

import comp4900.bcit.ca.washaf.R;
import comp4900.bcit.ca.washaf.User;

/**
 * Created by apple on 2017-05-15.
 */

public class EmployeeMainFrag extends Fragment {

    private Button changeStatusButton;
    private boolean working_status;
    private DatabaseReference workingRef;
    private DatabaseReference orderRef;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.employee_main_frag, container, false);
        user = (User) getArguments().getSerializable("user");
        working_status = false;
        changeStatusButton = (Button) view.findViewById(R.id.working_button);
        workingRef = FirebaseDatabase.getInstance().getReference("working employee");
        orderRef  = FirebaseDatabase.getInstance().getReference("working employee").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        changeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!working_status) {
                    startWorking();
                } else {
                    stopWorking();
                }
            }
        });
        return view;
    }

    private void stopWorking() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.employee_stop_working);
        builder.setPositiveButton(R.string.order_confirmation_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                working_status = false;
                changeStatusButton.setText("Stop Working");
            }
        });
        builder.setNegativeButton(R.string.order_confirmation_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.show();
    }

    private void startWorking() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.employee_start_working);
        builder.setPositiveButton(R.string.order_confirmation_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                working_status = true;
                changeStatusButton.setText("Start Working");
            }
        });
        builder.setNegativeButton(R.string.order_confirmation_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.show();
    }
}
