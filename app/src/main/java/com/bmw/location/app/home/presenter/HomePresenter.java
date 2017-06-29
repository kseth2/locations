package com.bmw.location.app.home.presenter;

import com.bmw.location.app.model.LocationData;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomePresenter {

    public static final String NAME = "name";
    private HomeInterfaceView mView;

    public HomePresenter(HomeInterfaceView view) {
        mView = view;
    }

    public void loadLocationsData() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        RealmResults<LocationData> locationDataList = realm.where(LocationData.class).findAll().sort(NAME);
        realm.commitTransaction();

        mView.onLocationsDataLoaded(locationDataList);
    }
}
