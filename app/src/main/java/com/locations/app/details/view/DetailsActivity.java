package com.locations.app.details.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.locations.app.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.locations.app.details.presenter.DetailsInterfaceView;
import com.locations.app.details.presenter.DetailsPresenter;
import com.locations.app.home.view.HomeActivity;
import com.locations.app.model.LocationData;

import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback, DetailsInterfaceView {

    private static final String TAG = DetailsActivity.class.getSimpleName();
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final int ZOOM_LEVEL = 15;

    private DetailsPresenter mPresenter;
    private LocationData mLocationData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        long id = intent.getLongExtra(HomeActivity.ID, -1L);
        if (id == -1L) {
            throw new RuntimeException("ID parameter is needed to launch " + TAG);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mPresenter = new DetailsPresenter(this);
        mPresenter.loadLocation(id);
    }

    @Override
    public void onLoadLocationSuccess(LocationData locationData) {
        mLocationData = locationData;

        // Setting up map fragment
        setMapFragment();

        // Set toolbar title same as location name
        setToolbarTitle(mLocationData.getName());

        try {
            // Setup data on the view
            setLocationData();
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onLoadLocationFail() {

    }

    private void setMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setToolbarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar == null) {
            Log.d(TAG, "No action bar supported");
            return;
        }

        // Setup toolbar with navigation back enabled
        actionBar.setTitle(title.trim());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    private void setLocationData() throws ParseException {

        DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern(DATE_FORMAT);
        LocalDateTime arrivalTime = LocalDateTime.parse(mLocationData.getArrivalTime(), dateTimeFormat);
        LocalDateTime currentTime = LocalDateTime.now();
        int timeDifferenceInMinutes = Minutes.minutesBetween(currentTime, arrivalTime).getMinutes();

        ((TextView) findViewById(R.id.arrival_time)).setText(String.format("Arrival Time: %s minutes", timeDifferenceInMinutes));
        ((TextView) findViewById(R.id.location)).setText(String.format("Location: %s", mLocationData.getName()));
        ((TextView) findViewById(R.id.address)).setText(String.format("Address: %s", mLocationData.getAddress()));
        ((TextView) findViewById(R.id.latitude)).setText(String.format("Latitude: %s", mLocationData.getLatitude()));
        ((TextView) findViewById(R.id.longitude)).setText(String.format("Longitude: %s", mLocationData.getLongitude()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     * Add marker on the map for the latitude and longitude from locationData
     *
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(mLocationData.getLatitude(), mLocationData.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(location).title(mLocationData.getName()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, ZOOM_LEVEL));
    }
}