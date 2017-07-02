package com.locations.app.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.bmw.location.app.R;
import com.locations.app.config.AppConfigurationManager;
import com.locations.app.home.view.HomeActivity;
import com.locations.app.model.LocationData;

import io.realm.Realm;

public class SplashActivity extends AppCompatActivity implements AppConfigurationManager.ResponseListener {

    private AppConfigurationManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Getting AppConfigurationManager instance
        mManager = AppConfigurationManager.getInstance();

        // setting listener
        mManager.setOnConfigurationListener(this);
        mManager.getAppConfiguration();
    }

    @Override
    public void onConfigurationComplete() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    @Override
    public void onConfigurationFail() {
        Realm realm = Realm.getDefaultInstance();

        realm.beginTransaction();
        final LocationData locationData = realm.where(LocationData.class).findFirst();
        realm.commitTransaction();
        if (locationData != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
            return;
        }


        Context context = getApplicationContext();
        CharSequence text = getResources().getString(R.string.error_fetching_data);
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
        toast.getView().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);
    }
}
