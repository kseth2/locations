package com.locations.app.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.locations.app.model.LocationData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface LocationsService {
    String BASE_URL = "http://localsearch.azurewebsites.net/api/";

    @GET("Locations")
    Call<List<LocationData>> getLocations();

    class Factory {
        public static LocationsService create() {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(LocationsService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            return retrofit.create(LocationsService.class);
        }
    }
}