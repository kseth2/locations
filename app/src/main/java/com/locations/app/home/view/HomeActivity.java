package com.locations.app.home.view;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.locations.app.R;
import com.locations.app.details.view.DetailsActivity;
import com.locations.app.home.adapter.LocationsAdapter;
import com.locations.app.home.presenter.HomeInterfaceView;
import com.locations.app.home.presenter.HomePresenter;
import com.locations.app.model.LocationData;

import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity implements
        HomeInterfaceView,
        LocationsAdapter.OnRecyclerClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    public static final String ID = "id";
    public static final String TAG = HomeActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final static int PERMISSION_ACCESS_COARSE_LOCATION = 1;

    private HomePresenter mPresenter;
    private RecyclerView mRecyclerView;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mLocation;

    @SuppressWarnings("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // Setting listener
        mPresenter = new HomePresenter(this);

        // Sets data on presenter sorted by name
        mPresenter.loadLocationsData(HomePresenter.NAME);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_LOW_POWER)
                .setInterval(5 * 1000)
                .setFastestInterval(5 * 1000);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_by_name) {
            mPresenter.loadLocationsData(HomePresenter.NAME);
            return true;
        }

        if (id == R.id.sort_by_distance) {

            /* Shows Toast if location is not turned on, otherwise sort by distance between
             * current location of device and locations from locations API
             */
            if (mLocation == null) {
                CharSequence text = getResources().getString(R.string.error_fetching_current_location);
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
            } else {
                mPresenter.loadLocationsData(mLocation, HomePresenter.DISTANCE_FROM_CURRENT_LOCATION);
            }
            return true;
        }

        // Sort by arrival time returned from locations API
        if (id == R.id.sort_by_arrival_time) {
            mPresenter.loadLocationsData(HomePresenter.ARRIVAL_TIME);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationsDataLoaded(RealmResults<LocationData> locationDataList) {
        LocationsAdapter adapter = new LocationsAdapter(this, locationDataList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onLocationClick(long id) {
        // Starts DetailsActivity when a location is clicked and displays details for that location
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(ID, id);
        startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // checks if app has access to Location, otherwise request for it
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission();
        } else {
            // Assigns last location to mLocation
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLocation == null) {
                // Request for location updates if last location is not present
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }
        }
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_ACCESS_COARSE_LOCATION);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
    }
}