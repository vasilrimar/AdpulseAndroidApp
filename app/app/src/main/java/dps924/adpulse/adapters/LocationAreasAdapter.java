package dps924.adpulse.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import dps924.adpulse.R;
import dps924.adpulse.adapters.advadapters.NFRolodexArrayAdapter;
import dps924.adpulse.models.LocationArea;

public class LocationAreasAdapter extends NFRolodexArrayAdapter<String, LocationArea> {
    static final int UPDATE_LOCATION_AREA_REQUEST  = 1;

    public LocationAreasAdapter(Context context, List<LocationArea> areas) {
        super(context, areas);
    }

    @Override
    public String createGroupFor(LocationArea childItem) {
        return childItem.Area;
    }

    @Override
    public View getChildView(LayoutInflater inflater, int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.location_area_list_item, parent, false);
        }
        final LocationArea locationArea = getChild(groupPosition, childPosition);
        TextView boardType = (TextView) convertView.findViewById(R.id.boardName);
        TextView isComplete = (TextView) convertView.findViewById(R.id.isComplete);
        //TextView boardName = (TextView) convertView.findViewById(R.id.isComplete2);

        //boardName.setText(locationArea.CurrentCreative.Name);
        boardType.setText(locationArea.Board);
        if (locationArea.IsComplete) {
            isComplete.setText("Complete");
            isComplete.setTextColor(Color.parseColor("#23cd8b"));
        }
        else {
            isComplete.setText("Incomplete");
            isComplete.setTextColor(Color.parseColor("#ed244f"));
        }

        return convertView;
    }

    @Override
    public View getGroupView(LayoutInflater inflater, int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.location_area_list_group, parent, false);
        }

        String locationAreaGroup = getGroup(groupPosition);
        TextView groupName = (TextView) convertView.findViewById(R.id.areaGroupName);
        groupName.setText(locationAreaGroup);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
