package com.bmw.location.app;

import android.app.Application;

import com.bmw.location.app.config.AppConfigurationManager;

import io.realm.Realm;

public class LocationsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initializing manager
        AppConfigurationManager.initialize();

        //Initializing REALM
        Realm.init(this);
    }
}
