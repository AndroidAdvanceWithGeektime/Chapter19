package com.sample.battery;


import android.app.Application;
import android.content.Context;

public class SampleApplication extends Application {
    private static Context mCtx;

    public SampleApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static Context getCtx() {
        return mCtx;
    }

    @Override
    protected void attachBaseContext(final Context base) {
        mCtx = base;
        super.attachBaseContext(base);
    }
}
