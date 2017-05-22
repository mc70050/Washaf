package comp4900.bcit.ca.washaf;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Michael on 2017-05-09.
 * A custom adapter that contains a HashMap to fill a listview.
 */

public class OrderAdapter extends BaseAdapter {
    private final ArrayList mData;

    public OrderAdapter() {
        mData = new ArrayList();
    }

    public OrderAdapter(Map<String, CurrentOrder> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, CurrentOrder> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View result;

        if (convertView == null) {
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_assigned_order, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<String, CurrentOrder> item = getItem(position);

        // TODO replace findViewById by ViewHolder
        ((TextView) result.findViewById(R.id.text1)).setText(item.getValue().getServiceType());
        ((TextView) result.findViewById(R.id.text2)).setText(item.getValue().getRequestedTime());
        ((TextView) result.findViewById(R.id.text3)).setText(item.getValue().getStatus().toString());
        ((Button) result.findViewById(R.id.update)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return result;
    }
}
