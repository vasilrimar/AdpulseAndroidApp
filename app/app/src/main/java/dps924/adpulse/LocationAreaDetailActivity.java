package dps924.adpulse;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import dps924.adpulse.adapters.CreativeSpinnerAdapter;
import dps924.adpulse.models.AdLocation;
import dps924.adpulse.models.Creative;
import dps924.adpulse.models.LocationArea;

public class LocationAreaDetailActivity extends ActionBarActivity
        implements View.OnClickListener {

    TextView expectedCreative;
    Button updateArea;
    Toolbar toolbar;
    Spinner creativeSpinner;
    LocationArea locationArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_area_detail);

        String id = Long.toString(getIntent().getLongExtra("AreaId", 0));
        locationArea = LocationArea.findById(LocationArea.class, Long.parseLong(id));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(locationArea.Board);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        expectedCreative = (TextView) findViewById(R.id.expectedCreative);
        expectedCreative.setText(locationArea.ExpectedCreative.Name);

        updateArea = (Button) findViewById(R.id.areaUpdate);
        creativeSpinner = (Spinner) findViewById(R.id.creative_spinner);

        if (locationArea.IsComplete) {
            creativeSpinner.setEnabled(false);
            creativeSpinner.setClickable(false);

            updateArea.setEnabled(false);
            updateArea.getBackground().setAlpha(128);
            updateArea.setText("Already Complete");
        }
        List<Creative> list = Creative.find(Creative.class, "is_filler = ?", "1");

        creativeSpinner.setAdapter(new CreativeSpinnerAdapter(this, Creative.find(Creative.class, "is_filler = ?", "1")));
        updateArea.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        AdLocation location = locationArea.AdLocation;
        location.TotalCompleted += 1;
        location.save();

        locationArea.IsComplete = true;
        locationArea.save();

        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.pull_in_left,R.anim.push_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        onBackPressed();
        return true;
    }
}
