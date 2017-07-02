package com.locations.app.home.presenter;

import com.locations.app.model.LocationData;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomePresenter {

    public static final String NAME = "name";
    public static final String ARRIVAL_TIME = "arrivalTime";

    private HomeInterfaceView mView;

    public HomePresenter(HomeInterfaceView view) {
        mView = view;
    }

    public void loadLocationsData(String sortBy) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        RealmResults<LocationData> locationDataList = realm.where(LocationData.class).findAll().sort(sortBy);
        realm.commitTransaction();

        mView.onLocationsDataLoaded(locationDataList);
    }
}
