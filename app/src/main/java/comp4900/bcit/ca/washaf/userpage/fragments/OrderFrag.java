package comp4900.bcit.ca.washaf.userpage.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import comp4900.bcit.ca.washaf.R;
import comp4900.bcit.ca.washaf.User;

public class OrderFrag extends Fragment {

    private ImageView chooseDayIcon;
    private TextView  chooseDayText;
    private ImageView chooseTimeIcon;
    private TextView  chooseTimeText;
    private Button    nextButton;
    private Spinner   quantity;

    private User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.frag_order, container, false);
        chooseDayIcon = (ImageView) view.findViewById(R.id.choose_day_icon);
        chooseDayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog();
            }
        });
        chooseDayText = (TextView) view.findViewById(R.id.choose_day_text);
        chooseTimeIcon = (ImageView) view.findViewById(R.id.choose_time_icon);
        chooseTimeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimeDialog();
            }
        });
        chooseTimeText = (TextView) view.findViewById(R.id.choose_time_text);
        nextButton = (Button) view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        user = (User) getArguments().getSerializable("user");
        quantity = (Spinner) view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, createAdapter());
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        quantity.setAdapter(adapter);
        return view;

    }

    private ArrayList<String> createAdapter() {
        Log.d("createAdapter", user.getNumOfBags() + "");
        long limit = user.getNumOfBags();
        ArrayList<String> list = new ArrayList<>();
        for (long i = 1; i <= limit; i++) {
            list.add(i + "");
        }
        return list;
    }

    public void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.icon_schedule);
        builder.setTitle("Select A Day");
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
                final String strName = adapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
                builderInner.setMessage(strName);
                builderInner.setTitle("Your Selected Day is");
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

    public void createTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.clock2);
        builder.setTitle("Select A Time Frame");
        String[] timeArray = getResources().getStringArray(R.array.time_frame);
        final ArrayAdapter<String>adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, timeArray);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strName = adapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
                builderInner.setMessage(strName);
                builderInner.setTitle("Your Selected Time is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        chooseTimeText.setText(strName);
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builder.show();
    }
}
