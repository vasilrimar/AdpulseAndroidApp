package dps924.adpulse.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import dps924.adpulse.R;
import dps924.adpulse.adapters.advadapters.AbsArrayAdapter;
import dps924.adpulse.models.AdLocation;

public class LocationsAdapter extends AbsArrayAdapter<AdLocation> implements Filterable {
    public LocationsAdapter(Context context, List<AdLocation> locations) {
        super(context, locations);
    }
    @Override
    public View getView(LayoutInflater inflater, int position, View convertView, ViewGroup parent) {
        AdLocation location = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.location_list_item, parent, false);

            holder = new ViewHolder();
            holder.locationName = (TextView) convertView.findViewById(R.id.locationName);
            holder.locationAddress = (TextView) convertView.findViewById(R.id.locationAddress);
            holder.totalCompleted = (TextView) convertView.findViewById(R.id.totalCompleted);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.locationName.setText(location.LocationName);
        holder.locationAddress.setText(location.Address);
        holder.totalCompleted.setText(location.TotalCompleted + "/" + location.TotalLocations);

        GradientDrawable bgShape = (GradientDrawable)holder.totalCompleted.getBackground();

        if (location.TotalCompleted == 0) {
            bgShape.setColor(Color.parseColor("#ed244f"));
        }
        else if (location.TotalCompleted > 0 && location.TotalCompleted < location.TotalLocations) {
            bgShape.setColor(Color.parseColor("#edd324"));
        }
        else {
            bgShape.setColor(Color.parseColor("#23cd8b"));
        }
        return convertView;
    }

    static class ViewHolder {
        TextView locationName;
        TextView locationAddress;
        TextView totalCompleted;
    }

    static class nameAscendingComparator implements Comparator<AdLocation> {
        @Override
        public int compare(AdLocation a, AdLocation b) {
            return a.TotalCompleted < b.TotalCompleted ? -1 : a.TotalCompleted == b.TotalCompleted ? 0 : 1;
        }
    }

    @Override
    protected boolean isFilteredOut(AdLocation item, CharSequence constraint) {
        return !item.LocationName.toLowerCase(Locale.US).contains(
                constraint.toString().toLowerCase(Locale.US));
        //return !(item.TotalCompleted > 0 && item.TotalCompleted < item.TotalLocations);
    }

}
