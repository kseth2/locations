package com.bmw.location.app.home.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bmw.location.app.R;
import com.bmw.location.app.details.DetailsActivity;
import com.bmw.location.app.home.adapter.LocationsAdapter;
import com.bmw.location.app.home.presenter.HomeInterfaceView;
import com.bmw.location.app.home.presenter.HomePresenter;
import com.bmw.location.app.model.LocationData;

import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity implements HomeInterfaceView, LocationsAdapter.OnRecyclerClickListener {

    public static final String ID = "id";
    private HomePresenter mPresenter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        setSupportActionBar(myToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mPresenter = new HomePresenter(this);
        mPresenter.loadLocationsData();
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