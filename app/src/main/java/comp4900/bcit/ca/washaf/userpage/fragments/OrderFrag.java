package comp4900.bcit.ca.washaf.userpage.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import comp4900.bcit.ca.washaf.Admin;
import comp4900.bcit.ca.washaf.R;
import comp4900.bcit.ca.washaf.User;
import comp4900.bcit.ca.washaf.userpage.OrderConfirmPage;

public class OrderFrag extends Fragment {

    private static final String STORE = "Store: ";
    private static final String ADDRESS = "Address: ";
    private static final String PHONE = "Phone: ";


    private TextView  chooseDayText;
    private TextView  chooseDayText2;
    private TextView  chooseTimeText;
    private TextView  chooseTimeText2;
    private Button    nextButton;
    private ImageView storeIcon;
    private TextView chooseStoreText;
    private RadioButton dropOffButton;
    private RadioButton deliveryButton;
    private ImageView mapIcon;
    private EditText addressText;
    private RadioGroup serviceGroup;
    private RadioGroup pickUpGroup;
    private RadioGroup deliveryGroup;
    private Spinner   quantity;

    private User user;
    private boolean pickupDateChosen;
    private boolean deliveryDateChosen;
    private int firstDayChosen;

    private boolean pickUpTimeChosen = false;
    private boolean dropOffLocationChosen = false;
    private boolean deliveryTimeChosen = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ImageView chooseDayIcon;
        ImageView chooseDayIcon2;
        final ImageView chooseTimeIcon;
        ImageView chooseTimeIcon2;

        pickupDateChosen = false;
        deliveryDateChosen = false;
        firstDayChosen = 0;

        final View view = inflater.inflate(R.layout.frag_order, container, false);
        dropOffButton = (RadioButton) view.findViewById(R.id.drop_off);
        deliveryButton = (RadioButton) view.findViewById(R.id.delivery);
        serviceGroup = (RadioGroup) view.findViewById(R.id.service_group);
        pickUpGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        deliveryGroup = (RadioGroup) view.findViewById(R.id.radioGroup2);
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("group").child("admin");
        final FirebaseListAdapter<Admin> storeAdapter = new FirebaseListAdapter<Admin>(getActivity(), Admin.class, R.layout.store_list, ref) {
            @Override
            protected void populateView(View v, Admin model, int position) {
                ((TextView) v.findViewById(R.id.text1)).setText(STORE + model.getStoreName());
                ((TextView) v.findViewById(R.id.text2)).setText(ADDRESS + model.getAddress());
                ((TextView) v.findViewById(R.id.text3)).setText(PHONE + model.getPhoneNum());
            }
        };

        chooseDayIcon = (ImageView) view.findViewById(R.id.choose_day_icon);
        chooseDayIcon2 = (ImageView) view.findViewById(R.id.choose_day_icon2);
        chooseDayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChooseDayDialog();
            }
        });
        chooseDayIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pickupDateChosen)
                    Toast.makeText(view.getContext(), R.string.choose_pickup_day_message, Toast.LENGTH_LONG).show();
                else {
                    Log.d("create dialog", firstDayChosen + "");
                    createDeliveryDayDialog(firstDayChosen);
                }
            }
        });
        chooseDayText = (TextView) view.findViewById(R.id.choose_day_text);
        chooseDayText2 = (TextView) view.findViewById(R.id.choose_day_text2);
        chooseTimeIcon = (ImageView) view.findViewById(R.id.choose_time_icon);
        chooseTimeIcon2 = (ImageView) view.findViewById(R.id.choose_time_icon2);
        chooseTimeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimeDialog(0);
            }
        });
        chooseTimeIcon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimeDialog(1);
            }
        });
        chooseTimeText = (TextView) view.findViewById(R.id.choose_time_text);
        chooseTimeText2 = (TextView) view.findViewById(R.id.choose_time_text2);
        storeIcon = (ImageView) view.findViewById(R.id.choose_store_icon);
        chooseStoreText = (TextView) view.findViewById(R.id.choose_store_text);
        storeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createStoreDialog(storeAdapter);
            }
        });
        dropOffButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    storeIcon.setVisibility(View.VISIBLE);
                    chooseStoreText.setVisibility(View.VISIBLE);
                    chooseDayText.setText("No need to choose a date.");
                    chooseTimeText.setText("No need to choose a time.");
                    pickupDateChosen = true;
                    chooseDayIcon.setOnClickListener(null);
                    chooseTimeIcon.setOnClickListener(null);
                } else {
                    storeIcon.setVisibility(View.GONE);
                    chooseStoreText.setVisibility(View.GONE);
                    chooseDayText.setText(R.string.choose_pickup_day);
                    chooseTimeText.setText(R.string.choose_pickup_time);
                    pickupDateChosen = false;
                    chooseDayIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createChooseDayDialog();
                        }
                    });
                    chooseTimeIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            createTimeDialog(0);
                        }
                    });
                }
            }
        });
        mapIcon = (ImageView) view.findViewById(R.id.choose_map_icon);
        addressText = (EditText) view.findViewById(R.id.choose_address_text);
        deliveryButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mapIcon.setVisibility(View.VISIBLE);
                    addressText.setVisibility(View.VISIBLE);
                } else {
                    mapIcon.setVisibility(View.GONE);
                    addressText.setVisibility(View.GONE);
                }
            }
        });
        user = (User) getArguments().getSerializable("user");
        mapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressText.setText(user.getAddress());
            }
        });
        nextButton = (Button) view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllFieldsFilled()) {
                    Intent intent = new Intent(getActivity(), OrderConfirmPage.class);
                    intent.putExtras(getAllInfo());
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                } else {
                    Toast.makeText(view.getContext(), "One or more fields are not filled in", Toast.LENGTH_LONG).show();
                }
            }
        });
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

    public void createChooseDayDialog() {
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
        final ArrayAdapter<String>adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dateStringArray);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which > firstDayChosen) {
                    chooseDayText2.setText(R.string.choose_pickup_day2);
                }
                firstDayChosen = which;
                final String strName = adapter.getItem(which);
                AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
                builderInner.setMessage(strName);
                builderInner.setTitle("Your Selected Day is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        chooseDayText.setText(strName);
                        pickupDateChosen = true;
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builder.show();
    }

    public void createDeliveryDayDialog(int dayChosen) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.icon_schedule);
        builder.setTitle("Select A Day");
        SimpleDateFormat curFormater = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
        GregorianCalendar date = new GregorianCalendar();
        date.roll(Calendar.DAY_OF_YEAR, true);
        String[] dateStringArray = new String[7];
        for (int i = 0; i <= dayChosen; i++) {
            date.roll(Calendar.DAY_OF_YEAR, true);
        }
        for (int day = 0; day < 7; day++) {
            dateStringArray[day] = curFormater.format(date.getTime());
            date.roll(Calendar.DAY_OF_YEAR, true);
        }
        final ArrayAdapter<String>adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dateStringArray);
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
                        chooseDayText2.setText(strName);
                        deliveryDateChosen = true;
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builder.show();
    }

    public void createTimeDialog(final int number) {
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
                        if (number == 0) {
                            chooseTimeText.setText(strName);
                            pickUpTimeChosen = true;
                        } else {
                            chooseTimeText2.setText(strName);
                            deliveryTimeChosen = true;
                        }
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builder.show();
    }

    public void createStoreDialog(final FirebaseListAdapter<Admin> adapter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.store);
        builder.setTitle("Select A Store");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String strName = adapter.getItem(which).getStoreName();
                AlertDialog.Builder builderInner = new AlertDialog.Builder(getActivity());
                builderInner.setMessage(strName);
                builderInner.setTitle("Your Selected Store is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int which) {
                        chooseStoreText.setText(strName);
                        dropOffLocationChosen = true;
                        dialog.dismiss();
                    }
                });
                builderInner.show();
            }
        });
        builder.show();
    }

    public boolean isAllFieldsFilled() {
        if (deliveryDateChosen && deliveryTimeChosen) {
            if (dropOffButton.isChecked()) {
                if (!dropOffLocationChosen) {
                    return false;
                }
            }  else if (!dropOffButton.isChecked()) {
                if (pickupDateChosen == false || pickUpTimeChosen == false) {
                    return false;
                }
            } else if (deliveryButton.isChecked()) {
                if (addressText.getText().toString().isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public Bundle getAllInfo() {
        Bundle bun = new Bundle();
        bun.putString("service", getRadioGroupText(serviceGroup));
        bun.putString("pick up service", getRadioGroupText(pickUpGroup));
        bun.putString("pick up day", chooseDayText.getText().toString());
        bun.putString("pick up time", chooseTimeText.getText().toString());
        if (dropOffButton.isChecked()) {
            bun.putString("drop off location", chooseStoreText.getText().toString());
        }
        bun.putString("quantity", quantity.getSelectedItem().toString());
        bun.putString("delivery service", getRadioGroupText(deliveryGroup));
        bun.putString("delivery day", chooseDayText2.getText().toString());
        bun.putString("delivery time", chooseTimeText2.getText().toString());
        if (deliveryButton.isChecked()) {
            bun.putString("delivery location", addressText.getText().toString());
        }
        bun.putSerializable("user", user);
        return bun;
    }

    private String getRadioGroupText(RadioGroup rg1) {
        String selection = "";
        if(rg1.getCheckedRadioButtonId()!=-1){
            int id= rg1.getCheckedRadioButtonId();
            View radioButton = rg1.findViewById(id);
            int radioId = rg1.indexOfChild(radioButton);
            RadioButton btn = (RadioButton) rg1.getChildAt(radioId);
            selection = (String) btn.getText();
        }
        return selection;
    }
}
