package com.locations.app.home.presenter;

import android.location.Location;

import com.locations.app.model.LocationData;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomePresenter {

    public static final String NAME = "name";
    public static final String DISTANCE_FROM_CURRENT_LOCATION = "distanceFromCurrentLocation";
    public static final String ARRIVAL_TIME = "arrivalTime";

    private HomeInterfaceView mView;

    public HomePresenter(HomeInterfaceView view) {
        mView = view;
    }

    // Gets location data from db sorted by sortBy field
    public void loadLocationsData(String sortBy) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        RealmResults<LocationData> locationDataList = realm.where(LocationData.class).findAll().sort(sortBy);
        realm.commitTransaction();

        mView.onLocationsDataLoaded(locationDataList);
    }

    // Get locations data sorted by distance between current location and locations in db
    public void loadLocationsData(Location currentLocation, String sortBy) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();

        RealmResults<LocationData> locationDataList = realm.where(LocationData.class).findAll();

        for (int i = 0; i < locationDataList.size(); i++) {
            LocationData locationData = locationDataList.get(i);

            Location location = new Location("fromDb");
            location.setLatitude(locationData.getLatitude());
            location.setLongitude(locationData.getLongitude());

            float distanceFromCurrentLocation = currentLocation.distanceTo(location);

            locationData.setDistanceFromCurrentLocation(distanceFromCurrentLocation);

            realm.copyToRealmOrUpdate(locationData);
        }

        RealmResults<LocationData> updatedLocationDataList = realm.where(LocationData.class).findAll().sort(sortBy);

        realm.commitTransaction();

        mView.onLocationsDataLoaded(updatedLocationDataList);
    }
}
