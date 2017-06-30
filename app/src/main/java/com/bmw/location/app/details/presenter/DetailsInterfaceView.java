package com.bmw.location.app.details.presenter;


import com.bmw.location.app.model.LocationData;

public interface DetailsInterfaceView {

    void onLoadLocationSuccess(LocationData locationData);

    void onLoadLocationFail();
}