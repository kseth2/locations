package com.locations.app;

import android.app.Application;

import com.locations.app.config.AppConfigurationManager;

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
