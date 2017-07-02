package com.locations.app.details.presenter;


import com.locations.app.model.LocationData;

public interface DetailsInterfaceView {

    void onLoadLocationSuccess(LocationData locationData);

    void onLoadLocationFail();
}