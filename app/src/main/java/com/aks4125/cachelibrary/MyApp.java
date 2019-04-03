package com.aks4125.cachelibrary;

import android.app.Application;

import com.aks4125.cachelibrary.di.component.AppComponent;
import com.aks4125.cachelibrary.di.component.DaggerAppComponent;
import com.aks4125.cachelibrary.di.module.ApplicationModule;

public class MyApp extends Application {

    private AppComponent mAppComponent;

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

    }
}
