package com.bmw.location.app.home.presenter;

import com.bmw.location.app.model.LocationData;

import io.realm.RealmResults;

public interface HomeInterfaceView {

    /**
     * Callback to return to the view all locations data in database
     *
     * @param locationDataList List that contains locations
     */
    void onLocationsDataLoaded(RealmResults<LocationData> locationDataList);
}