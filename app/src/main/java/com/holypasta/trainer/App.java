package com.holypasta.trainer;

import android.app.Application;

import com.holypasta.trainer.helper.HelperFactory;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        HelperFactory.setHelper(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        HelperFactory.releaseHelper();
        super.onTerminate();
    }
}
