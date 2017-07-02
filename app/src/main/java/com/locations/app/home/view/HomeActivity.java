package com.locations.app.home.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.bmw.location.app.R;
import com.locations.app.details.view.DetailsActivity;
import com.locations.app.home.adapter.LocationsAdapter;
import com.locations.app.home.presenter.HomeInterfaceView;
import com.locations.app.home.presenter.HomePresenter;
import com.locations.app.model.LocationData;

import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity implements HomeInterfaceView, LocationsAdapter.OnRecyclerClickListener {

    public static final String ID = "id";
    private HomePresenter mPresenter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mPresenter = new HomePresenter(this);
        mPresenter.loadLocationsData(HomePresenter.NAME);
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
            //sort by distance
            return true;
        }

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
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(ID, id);
        startActivity(intent);
    }
}