package com.locations.app.details.presenter;

import com.locations.app.home.view.HomeActivity;
import com.locations.app.model.LocationData;

import io.realm.Realm;

public class DetailsPresenter {

    private DetailsInterfaceView mView;

    public DetailsPresenter(DetailsInterfaceView view) {
        mView = view;
    }

    // Get locationData from db by id
    public void loadLocation(long id) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        LocationData locationData = realm.where(LocationData.class).equalTo(HomeActivity.ID, id).findFirst();
        realm.commitTransaction();

        if (locationData == null) {
            mView.onLoadLocationFail();
        }

        mView.onLoadLocationSuccess(locationData);
    }
}