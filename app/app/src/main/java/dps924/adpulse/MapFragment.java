package dps924.adpulse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;

import dps924.adpulse.adapters.LocationsAdapter;
import dps924.adpulse.models.AdLocation;

public class MapFragment extends Fragment
        implements GoogleMap.OnMarkerClickListener{

    LocationsAdapter adapter;
    private GoogleMap mMap;
    MapView mMapView;
    EditText searchField;
    public MapFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpMapIfNeeded();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_layout, container, false);
        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMap = mMapView.getMap();

        final List<AdLocation> locations = AdLocation.listAll(AdLocation.class);
        final HashMap<String,Long> locationId = new HashMap<>();

        for( int i = 0; i < locations.size() - 1; i++) {

            float color;

            if (locations.get(i).TotalCompleted == locations.get(i).TotalLocations)
            {
                color= BitmapDescriptorFactory.HUE_GREEN;

            }
            else if(locations.get(i).TotalCompleted < locations.get(i).TotalLocations && locations.get(i).TotalCompleted > 0)
            {
                color= BitmapDescriptorFactory.HUE_YELLOW;
            }
            else
            {
                color= BitmapDescriptorFactory.HUE_RED;
            }

            Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(locations.get(i).Latitude, locations.get(i).Longitude))
                            .title(locations.get(i).LocationName)
                            .snippet(locations.get(i).FormattedAddress)
                            .icon(BitmapDescriptorFactory.defaultMarker(color))

            );


            locationId.put(marker.getTitle(),locations.get(i).getId());


            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override

                public void onInfoWindowClick(Marker m) {

                    if (locationId.containsKey(m.getTitle())) {

                        Intent intent = new Intent(getActivity(), LocationAreaListActivity.class);
                        intent.putExtra("LocationId", locationId.get(m.getTitle()));
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                    }
                }
            });

        }


        LatLng start = new LatLng(locations.get(0).Latitude,locations.get(0).Longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 15)); // change a map's camera

        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
       /* // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }*/
    }

    private void setUpMap() {
       /* List<AdLocation> locations = AdLocation.listAll(AdLocation.class);

        for(int i = 0; i < locations.size() - 1; i++) {
            mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(locations.get(i).Latitude, locations.get(i).Longitude))
                            .title(locations.get(i).LocationName)
                            .snippet(locations.get(i).Address)
            );

        }
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom((LatLng) (latLngTM.get(latLngTM.firstKey())), 15)); // change a map's camera
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker m) {
            //add click
            }
        });
        LatLng start = new LatLng(locations.get(0).Latitude,locations.get(0).Longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 15)); // change a map's camera

        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);*/


    }


    @Override
    public boolean onMarkerClick(Marker marker) {


        return true;
    }


}
