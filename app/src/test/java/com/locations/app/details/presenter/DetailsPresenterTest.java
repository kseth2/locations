package com.locations.app.details.presenter;

import android.content.Context;
import android.test.mock.MockContext;

import com.locations.app.model.LocationData;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.internal.RealmCore;
import io.realm.log.RealmLog;

import static com.locations.app.home.view.HomeActivity.ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.powermock.api.mockito.PowerMockito.doCallRealMethod;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@Ignore
@RunWith(PowerMockRunner.class)
@PrepareForTest({Realm.class, RealmConfiguration.class, RealmQuery.class, RealmResults.class, RealmCore.class, RealmLog.class})
public class DetailsPresenterTest {

    private long id = 35180;
    private Realm mockRealm;
    private DetailsInterfaceView mockDetailsInterfaceView;
    private RealmResults<LocationData> locationDataFromRealm;

    @Before
    public void setup() throws Exception {
        // Setup Realm to be mocked. The order of these matters
        mockStatic(RealmCore.class);
        mockStatic(RealmLog.class);
        mockStatic(Realm.class);
        mockStatic(RealmConfiguration.class);
        Realm.init(new MockContext());

        // Create the mock
        final Realm mockRealm = PowerMockito.mock(Realm.class);
        mockDetailsInterfaceView = PowerMockito.mock(DetailsInterfaceView.class);
        final RealmConfiguration mockRealmConfig = PowerMockito.mock(RealmConfiguration.class);

        // TODO: Better solution would be just mock the RealmConfiguration.Builder class. But it seems there is some
        // problems for powermock to mock it (static inner class). We just mock the RealmCore.loadLibrary(Context) which
        // will be called by RealmConfiguration.Builder's constructor.
        PowerMockito.doNothing().when(RealmCore.class);
        RealmCore.loadLibrary(any(Context.class));

        // TODO: Mock the RealmConfiguration's constructor. If the RealmConfiguration.Builder.build can be mocked, this
        // is not necessary anymore.
        PowerMockito.whenNew(RealmConfiguration.class).withAnyArguments().thenReturn(mockRealmConfig);

        // Anytime getInstance is called with any configuration, then return the mockRealm
        PowerMockito.when(Realm.getDefaultInstance()).thenReturn(mockRealm);

        // Anytime we ask Realm to create a LocationData, return a new instance.
        PowerMockito.when(mockRealm.createObject(LocationData.class)).thenReturn(new LocationData());

        LocationData locationData = new LocationData();
        locationData.setId(35180);
        locationData.setName("Doughnut Vault Canal");
        locationData.setAddress("11 N Canal St L30 Chicago, IL 60606");
        locationData.setLatitude(41.883976);
        locationData.setLongitude(-87.639346);
        locationData.setAddress("2017-06-30T16:38:01.357");

        List<LocationData> locationDataList = Arrays.asList(locationData);

        // Create a mock RealmQuery
        RealmQuery<LocationData> locationDataQuery = mockRealmQuery();

        // When the RealmQuery performs findFirst, return the first record in the list.
        PowerMockito.when(locationDataQuery.findFirst()).thenReturn(locationDataList.get(0));

        // When the where clause is called on the Realm, return the mock query.
        PowerMockito.when(mockRealm.where(LocationData.class)).thenReturn(locationDataQuery);

        // When the RealmQuery is filtered on any string and any integer, return the mock query
        PowerMockito.when(locationDataQuery.equalTo(anyString(), anyInt())).thenReturn(locationDataQuery);

        // RealmResults is final, must mock static and also place this in the PrepareForTest annotation array.
        mockStatic(RealmResults.class);

        // Create a mock RealmResults
        RealmResults<LocationData> locationDataFromRealm = mockRealmResults();

        // When we ask Realm for all of the LocationData instances, return the mock RealmResults
        PowerMockito.when(mockRealm.where(LocationData.class).findAll()).thenReturn(locationDataFromRealm);

        // When we ask the RealmQuery for all of the LocationData objects, return the mock RealmResults
        PowerMockito.when(locationDataQuery.findAll()).thenReturn(locationDataFromRealm);

        this.mockRealm = mockRealm;
        this.locationDataFromRealm = locationDataFromRealm;
    }

    @SuppressWarnings("unchecked")
    private <T extends RealmObject> RealmQuery<T> mockRealmQuery() {
        return PowerMockito.mock(RealmQuery.class);
    }

    @SuppressWarnings("unchecked")
    private <T extends RealmObject> RealmResults<T> mockRealmResults() {
        return PowerMockito.mock(RealmResults.class);
    }

    @Test
    public void loadLocation() throws Exception {
        doCallRealMethod().when(mockRealm).executeTransaction(Mockito.any(Realm.Transaction.class));
//        LocationData expectedLocationData = new LocationData();
//        expectedLocationData.setId(id);

//        PowerMockito.when(mockRealm.where(LocationData.class).equalTo(ID, id).findFirst()).thenReturn(expectedLocationData);

        DetailsPresenter detailsPresenter = new DetailsPresenter(mockDetailsInterfaceView);

        detailsPresenter.loadLocation(id);

        Mockito.verify(mockRealm).beginTransaction();
        Mockito.verify(mockRealm).where(LocationData.class).equalTo(ID, id).findFirst();
        Mockito.verify(mockRealm).commitTransaction();
        Mockito.verify(mockDetailsInterfaceView).onLoadLocationSuccess(locationDataFromRealm.first());
    }


}