package com.example.brett.esort;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, getString(R.string.ApplicationId), getString(R.string.ClientKey));
    }
}
