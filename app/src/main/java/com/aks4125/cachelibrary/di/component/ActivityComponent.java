package com.aks4125.cachelibrary.di.component;

import com.aks4125.cachelibrary.di.scope.ActivityScope;
import com.aks4125.cachelibrary.ui.loadmore.LoadMoreActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class)
public interface ActivityComponent extends AppComponent {
    // inject only if desired component is required to utilize.
    void inject(LoadMoreActivity loadMoreActivity); // di is used only in unit test case and load more activity
}