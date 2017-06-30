package com.bmw.location.app.details.presenter;

import com.bmw.location.app.model.LocationData;

import io.realm.Realm;

import static com.bmw.location.app.home.view.HomeActivity.ID;

public class DetailsPresenter {

    private DetailsInterfaceView mView;

    public DetailsPresenter(DetailsInterfaceView view) {
        mView = view;
    }

    public void loadLocation(long id) {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        LocationData locationData = realm.where(LocationData.class).equalTo(ID, id).findFirst();
        realm.commitTransaction();

        if (locationData == null) {
            mView.onLoadLocationFail();
        }

        mView.onLoadLocationSuccess(locationData);
    }
}