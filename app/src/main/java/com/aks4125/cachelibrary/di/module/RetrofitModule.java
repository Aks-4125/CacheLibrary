package com.aks4125.cachelibrary.di.module;

import android.content.Context;

import com.aks4125.cachelibrary.BuildConfig;
import com.aks4125.cachelibrary.network.WebService;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Aks4125 on 1/6/2019.
 */

@Module
public class RetrofitModule {

    /**
     * @param context context
     * @return singleton retrofit object to use in entire project.
     */
    @Provides
    @Singleton
    public WebService apiService(Context context) {
        String mBaseUrl = BuildConfig.JSON_URL;

        int cacheSize = 4 * 1024 * 1024; // 4 MB cache
        Cache cache = new Cache(context.getCacheDir(), cacheSize);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .cache(cache)
                .build();

        return new Retrofit.Builder().baseUrl(mBaseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // RxJava2 support
                .build().create(WebService.class);

    }

}

