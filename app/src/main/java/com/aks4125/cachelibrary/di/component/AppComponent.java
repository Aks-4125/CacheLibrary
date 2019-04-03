package com.aks4125.cachelibrary.di.component;

import com.aks4125.cachelibrary.di.module.ApplicationModule;
import com.aks4125.cachelibrary.di.module.RetrofitModule;
import com.aks4125.cachelibrary.network.NetworkCheck;
import com.aks4125.cachelibrary.network.WebService;
import com.aks4125.cachelibrary.util.Utils;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, RetrofitModule.class})
public interface AppComponent {
    // api
    WebService apiService();

    //network status
    NetworkCheck networkCheck();

    // useful method
    Utils utils();

    // may require to convert from/to model/jsonString
    Gson getGson();

}