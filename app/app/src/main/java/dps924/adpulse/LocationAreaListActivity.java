package dps924.adpulse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import dps924.adpulse.adapters.LocationAreasAdapter;
import dps924.adpulse.models.AdLocation;
import dps924.adpulse.models.LocationArea;

public class LocationAreaListActivity extends ActionBarActivity
        implements ExpandableListView.OnChildClickListener {

    static final int UPDATE_LOCATION_AREA_REQUEST  = 1;

    Toolbar toolbar;
    TextView detailsAddress;
    TextView detailsPhone;
    TextView detailsContact;
    ExpandableListView listView;
    LinearLayout extrasView;
    LocationAreasAdapter adapter;

    long locationId;
    AdLocation location;
    List<LocationArea> locationAreas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_area_layout);
        long l = getIntent().getLongExtra("LocationId",0);
        location = AdLocation.findById(AdLocation.class, getIntent().getLongExtra("LocationId",0));
        locationAreas = LocationArea.find(LocationArea.class, "ad_location = ?", location.getId().toString());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(location.LocationName);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        detailsAddress = (TextView) findViewById(R.id.detailsAddress);
        detailsPhone = (TextView) findViewById(R.id.detailsPhone);
        detailsContact = (TextView) findViewById(R.id.detailsContact);
        extrasView = (LinearLayout) findViewById(R.id.locationExtrasView);
        extrasView.setVisibility(View.GONE);

        detailsAddress.setText(location.Address + " , " + location.City);
        detailsPhone.setText(location.formattedNumber());
        detailsContact.setText(location.ContactName);

        adapter = new LocationAreasAdapter(this, locationAreas);
        adapter.setOnChildClickListener(this);

        listView = (ExpandableListView) findViewById(R.id.expandableBoardList);
        listView.setAdapter(adapter);
        listView.expandGroup(0);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        LocationArea selectedArea = (LocationArea) adapter.getChild(groupPosition, childPosition);

        Intent intent = new Intent(getApplicationContext(), LocationAreaDetailActivity.class);
        intent.putExtra("AreaId", selectedArea.getId());
        startActivityForResult(intent, UPDATE_LOCATION_AREA_REQUEST);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        return false;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.pull_in_left,R.anim.push_out_right);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case UPDATE_LOCATION_AREA_REQUEST:
                if (resultCode == RESULT_OK) {
                    locationAreas = LocationArea.find(LocationArea.class, "ad_location = ?", location.getId().toString());
                    adapter.clear();
                    adapter.addAll(locationAreas);
                    listView.expandGroup(0);

                    LocalBroadcastManager.getInstance(this).sendBroadcast(
                            new Intent("REFRESH_LIST_DATA"));
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_locations_area, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch(id) {
       /* case R.id.toggleExtraView:
            extrasView.setVisibility(extrasView.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            //getSupportActionBar().hide();
            //Animation pullFromBottom = AnimationUtils.loadAnimation(this, R.anim.pull_from_bottom);
            //extrasView.startAnimation(pullFromBottom);
            break; */

        case android.R.id.home :
            onBackPressed();
            break;
        }
        return true;
    }

}
