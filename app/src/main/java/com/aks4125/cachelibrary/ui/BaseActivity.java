package com.aks4125.cachelibrary.ui;

import android.os.Bundle;

import com.aks4125.cachelibrary.MyApp;
import com.aks4125.cachelibrary.di.component.ActivityComponent;
import com.aks4125.cachelibrary.di.component.DaggerActivityComponent;
import com.aks4125.cachelibrary.network.NetworkCheck;
import com.aks4125.cachelibrary.network.WebService;
import com.aks4125.cachelibrary.util.Utils;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    /* specific injection */
    @Inject
    protected Utils mUtils;
    @Inject
    protected NetworkCheck mNetworkCheck;
    @Inject
    protected WebService mApiService;

    private ActivityComponent mComponent;

    protected abstract int getLayoutResourceId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComponent = DaggerActivityComponent.builder()
                .appComponent(getApp().getAppComponent()).build();
        if (getLayoutResourceId() != 0)
            setContentView(getLayoutResourceId());

    }

    /* used at image animation navigation*/
    protected void doSmoothAnimation() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    protected MyApp getApp() {
        return (MyApp) getApplicationContext();
    }

    /**
     *
     * @return dagger component
     */
    protected ActivityComponent getComponent() {
        return mComponent;
    }
}
