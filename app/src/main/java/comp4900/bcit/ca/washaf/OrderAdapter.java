package comp4900.bcit.ca.washaf;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Michael on 2017-05-09.
 */

public class OrderAdapter extends BaseAdapter {
    private final ArrayList mData;

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
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.cust_current_order_process, parent, false);
        } else {
            result = convertView;
        }

        Map.Entry<String, CurrentOrder> item = getItem(position);

        // TODO replace findViewById by ViewHolder
        ((TextView) result.findViewById(android.R.id.text1)).setText(item.getValue().getServiceType());
        ((TextView) result.findViewById(android.R.id.text2)).setText(item.getValue().getRequestedTime());

        return result;
    }
}
