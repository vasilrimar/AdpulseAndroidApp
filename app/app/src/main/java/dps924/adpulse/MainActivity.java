package dps924.adpulse;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Arrays;

import dps924.adpulse.api.ApiRequest;
import dps924.adpulse.database.DatabaseManager;
import dps924.adpulse.models.AdLocation;
import dps924.adpulse.models.Creative;

public class MainActivity extends ActionBarActivity {
    static final int UPDATE_LOCATION_AREA_REQUEST  = 1;

    static ProgressDialog progress;
    DrawerLayout drawerLayout;
    ListView drawerList;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    static Context context;

    static String baseUrl = "http://adpulse.ca/api/";
    String[] drawerListItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = getApplicationContext();

        setContentView(R.layout.main_layout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerListItems = getResources().getStringArray(R.array.drawer_items);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.drawer_list_item, drawerListItems));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if (databaseList().length == 0){
            loadFromAPI();
        }

        if (savedInstanceState == null) {
            selectDrawerItem(0);
        }
    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            drawerLayout.closeDrawer(drawerList);
            drawerLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectDrawerItem(position);
                }
            }, 350);
        }
    }

    private void selectDrawerItem(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (position == 0) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new LocationListFragment())
                    .commit();
        }
        else if (position == 1) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new MapFragment())
                    .commit();
        }

        drawerList.setItemChecked(position, true);
        setTitle(drawerListItems[position]);
        drawerLayout.closeDrawer(drawerList);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    public void loadFromAPI() {
        progress = ProgressDialog.show(this, "Status",
                "Fetching locations from API...", true);

        ApiRequest<AdLocation[]> getRequest = new ApiRequest<AdLocation[]>(
                baseUrl + "Location",
                Request.Method.GET,
                AdLocation[].class,
                new Response.Listener<AdLocation[]>() {
                    @Override
                    public void onResponse(AdLocation[] locations) {
                        new SaveLocationsAsync().execute(locations);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Volley Error:", error.toString());
                    }
                }
        );
        ApplicationController.getInstance().addToRequestQueue(getRequest);
    }

    public class SaveLocationsAsync extends AsyncTask<AdLocation, Void, Void>
    {
        @Override
        protected  void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(AdLocation... locations) {
            final int length = locations.length;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.setMessage("Saving " + length + " locations to database...");
                }
            });
            DatabaseManager.saveLocations(Arrays.asList(locations));
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progress.setMessage("Fetching campaigns from API...");
            ApiRequest<Creative[]> getRequest = new ApiRequest<Creative[]>(
                    baseUrl + "Creative",
                    Request.Method.GET,
                    Creative[].class,
                    new Response.Listener<Creative[]>() {
                        @Override
                        public void onResponse(Creative[] creatives) {
                            new SaveCreativesAsync().execute(creatives);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Volley Error:", error.toString());
                        }
                    }
            );
            ApplicationController.getInstance().addToRequestQueue(getRequest);
        }
    }

    public class SaveCreativesAsync extends AsyncTask<Creative, Void, Void>
    {
        @Override
        protected Void doInBackground(Creative... creatives) {
            final int length = creatives.length;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.setMessage("Saving " + length + " creatives to database...");
                }
            });
            DatabaseManager.saveCreatives(Arrays.asList(creatives));
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progress.hide();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, new LocationListFragment())
                    .commit();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (progress!=null) {
            if (progress.isShowing()) {
                progress.dismiss();
            }
        }
    }
}

