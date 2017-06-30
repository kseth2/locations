package com.bmw.location.app.config;

import com.bmw.location.app.api.LocationsService;
import com.bmw.location.app.model.LocationData;

import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class AppConfigurationManager {

    /**
     * Instance member variable
     */
    private static AppConfigurationManager sInstance;

    /**
     * Callback listener
     */
    private ResponseListener mListener;

    // private constructor required
    private AppConfigurationManager() {
    }

    public static void initialize() {
        sInstance = new AppConfigurationManager();
    }

    public synchronized static AppConfigurationManager getInstance() throws IllegalStateException {
        if (sInstance == null) {
            throw new IllegalStateException("You need to initialize instance from LocationsApplication class");
        }
        return sInstance;
    }

    public void setOnConfigurationListener(ResponseListener listener) {
        mListener = listener;
    }

    public void getAppConfiguration() {

        final Realm realm = Realm.getDefaultInstance();

        //do we need it here?
        realm.beginTransaction();
        final LocationData locationData = realm.where(LocationData.class).findFirst();
        realm.commitTransaction();

        if (locationData != null) {
            mListener.onConfigurationComplete();
            return;
        }

        final LocationsService service = LocationsService.Factory.create();

        service.getLocations().enqueue(new Callback<List<LocationData>>() {
            @Override
            public void onResponse(Call<List<LocationData>> call, Response<List<LocationData>> response) {
                if (response.isSuccessful()) {
                    List<LocationData> locationDataList = response.body();

                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(locationDataList);
                    realm.commitTransaction();

                    mListener.onConfigurationComplete();
                } else {
                    mListener.onConfigurationFail();
                }
            }

            @Override
            public void onFailure(Call<List<LocationData>> call, Throwable t) {
                mListener.onConfigurationFail();
                t.printStackTrace();
            }
        });
    }

    public interface ResponseListener {
        /**
         * This callback is called when configuration is successfully completed.
         */
        void onConfigurationComplete();

        /**
         * This callback is called when configuration fails.
         */
        void onConfigurationFail();
    }
}
