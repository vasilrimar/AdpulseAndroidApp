package dps924.adpulse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import dps924.adpulse.R;
import dps924.adpulse.models.Creative;

public class CreativeSpinnerAdapter extends ArrayAdapter<Creative> {
    public CreativeSpinnerAdapter(Context context, List<Creative> creatives) {
        super(context, 0, creatives);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Creative creative = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.creative_spinner_item, parent, false);
        }

        TextView creativeName = (TextView) convertView.findViewById(R.id.creativeName);
        creativeName.setText(creative.Name);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        Creative creative = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.creative_spinner_item, parent, false);
        }

        TextView creativeName = (TextView) convertView.findViewById(R.id.creativeName);
        creativeName.setText(creative.Name);

        return convertView;
    }
}
