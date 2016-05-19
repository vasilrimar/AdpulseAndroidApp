package dps924.adpulse;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import dps924.adpulse.adapters.LocationsAdapter;
import dps924.adpulse.models.AdLocation;

public class LocationListFragment extends ListFragment {

    LocationsAdapter adapter;
    List<AdLocation> locations;
    EditText searchField;

    public LocationListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                broadcastReceiver,
                new IntentFilter("REFRESH_LIST_DATA"));
        new initLocationDataAsync().execute();
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            locations = AdLocation.listAll(AdLocation.class);
            adapter.clear();
            adapter.addAll(locations);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.locations_layout, container, false);

        searchField = (EditText) rootView.findViewById(R.id.searchField);
        searchField.setVisibility(View.GONE);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getFilter().filter(s);
            }
        });
        //searchField.animate().translationY(-searchField.getMinHeight());
        return rootView;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);

        AdLocation location = (AdLocation)adapter.getItem(position);

        Intent intent = new Intent(getActivity(), LocationAreaListActivity.class);
        intent.putExtra("LocationId", location.getId());
        getActivity().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.toggleSearch) {
            if (searchField.getVisibility() == View.VISIBLE) {
                searchField.setText("");
                searchField.setVisibility(View.GONE);
                adapter.getFilter().filter("");

            }
            else { searchField.setVisibility(View.VISIBLE); }
        }
        else if (id != 0) {
            switch(id) {
                case R.id.menuSortAll:
                    locations = AdLocation.listAll(AdLocation.class);
                    break;
                case R.id.menuSortComplete:
                    locations = AdLocation.findWithQuery(AdLocation.class,
                            "Select * from Ad_Location where total_completed = total_locations");
                    break;
                case R.id.menuSortIncomplete:
                    locations = AdLocation.findWithQuery(AdLocation.class,
                            "Select * from Ad_Location where total_completed = ?", "0");
                    break;
                case R.id.menuSortPending:
                    locations = AdLocation.findWithQuery(AdLocation.class,
                            "Select * from Ad_Location where total_completed < total_locations and total_completed > 0");
                    break;
            }
            adapter.clear();
            adapter.addAll(locations);
        }

        return super.onOptionsItemSelected(item);
    }

    public class initLocationDataAsync extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected  void onPreExecute() {}

        @Override
        protected Void doInBackground(Void... params) {
            locations = AdLocation.listAll(AdLocation.class);
            adapter = new LocationsAdapter(getActivity(),locations);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(adapter);
        }
    }

}
