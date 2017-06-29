package com.bmw.location.app.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bmw.location.app.R;
import com.bmw.location.app.model.LocationData;

import io.realm.Realm;

import static com.bmw.location.app.home.view.HomeActivity.ID;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        long id = intent.getLongExtra(ID, -1L);
        if (id != -1L) {

            Realm realm = Realm.getDefaultInstance();

            LocationData locationData;

            realm.beginTransaction();
            locationData = realm.where(LocationData.class).equalTo(ID, id).findFirst();
            realm.commitTransaction();

            TextView textView = (TextView) findViewById(R.id.text_detail);
            textView.setText(locationData.getName());

        }
    }
}