package com.bmw.location.app.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.bmw.location.app.R;
import com.bmw.location.app.config.AppConfigurationManager;
import com.bmw.location.app.home.view.HomeActivity;

public class SplashActivity extends AppCompatActivity implements AppConfigurationManager.ResponseListener {

    private AppConfigurationManager mManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        // toast and retry button
        Context context = getApplicationContext();
        CharSequence text = getResources().getString(R.string.error_fetching_data);
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
