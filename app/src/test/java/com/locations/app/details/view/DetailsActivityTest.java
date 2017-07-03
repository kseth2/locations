package com.locations.app.details.view;

import android.content.Intent;

import com.locations.app.BuildConfig;
import com.locations.app.details.presenter.DetailsPresenter;
import com.locations.app.home.view.HomeActivity;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Ignore
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class DetailsActivityTest {

    private ActivityController<DetailsActivity> activityController;
    private Intent intent = new Intent();

    @Before
    public void setup() {

    }

    @Test
    public void onCreate_throwsException_forInvalidId() throws Exception {
        intent.putExtra(HomeActivity.ID, "1L");
        activityController = Robolectric.buildActivity(DetailsActivity.class, intent);
        try {
            activityController.create().get();
        } catch (Exception exception) {
            assertThat(exception.getClass()).isInstanceOf(RuntimeException.class);
        }
    }

    @Test
    public void onCreate_withValidId() throws Exception {
        intent.putExtra(HomeActivity.ID, 1L);
        activityController = Robolectric.buildActivity(DetailsActivity.class, intent);

        activityController.create().start().resume().get();

        DetailsPresenter mockDetailsPresenter = mock(DetailsPresenter.class);

        verify(mockDetailsPresenter).loadLocation(1L);
    }

    @Test
    public void onLoadLocationSuccess() throws Exception {


    }

    @Test
    public void onLoadLocationFail() throws Exception {

    }

    @Test
    public void onSupportNavigateUp() throws Exception {

    }

    @Test
    public void onMapReady() throws Exception {
//        GoogleMapUtility mockGoogleMapUtility = mock(GoogleMapUtility.class);
//        GoogleMap googleMap = activity.getMap;
//
//        activity.onMapReady(googleMap);
//
//        verify(mockGoogleMapUtility).addMarker(any(LatLng.class), anyString());
//        verify(mockGoogleMapUtility).moveCamera(any(LatLng.class), anyFloat());
    }

}