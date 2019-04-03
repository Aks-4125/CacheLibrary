package com.aks4125.cachelibrary.di.module;

import android.content.Context;


import com.aks4125.cachelibrary.MyApp;
import com.aks4125.cachelibrary.network.NetworkCheck;
import com.aks4125.cachelibrary.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private MyApp mApp;

    public ApplicationModule(MyApp app) {
        mApp = app;
    }

    /**
     * @return application context
     */
    @Provides
    @Singleton
    Context appContext() {
        return mApp;
    }

    /**
     * @return util object to use further
     */
    @Provides
    @Singleton
    Utils utils() {
        return new Utils(mApp);
    }

    /**
     * @return use .connected() to check network status
     */
    @Provides
    @Singleton
    NetworkCheck networkCheck() {
        return new NetworkCheck(mApp);
    }

    /**
     * @return gson object
     */
    @Provides
    @Singleton
    Gson getGson() {
        GsonBuilder sGsonBuilder = new GsonBuilder();
        return sGsonBuilder.create();
    }

}