package com.locations.app.home.presenter;

import com.locations.app.model.LocationData;

import io.realm.RealmResults;

public interface HomeInterfaceView {

    /**
     * Callback to return to the view all locations data in database
     *
     * @param locationDataList List that contains locations
     */
    void onLocationsDataLoaded(RealmResults<LocationData> locationDataList);
}