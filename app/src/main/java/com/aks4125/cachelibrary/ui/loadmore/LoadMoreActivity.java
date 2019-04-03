package com.aks4125.cachelibrary.ui.loadmore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.aks4125.cachelibrary.R;
import com.aks4125.cachelibrary.adapter.ImageAdapter;
import com.aks4125.cachelibrary.ui.BaseActivity;
import com.aks4125.cachelibrary.ui.FullScreenImageActivity;
import com.aks4125.cachelibrary.util.Utils;
import com.aks4125.cachex.DownloadUtils;

import org.jetbrains.annotations.NotNull;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.processors.PublishProcessor;
import io.reactivex.schedulers.Schedulers;

public class LoadMoreActivity extends BaseActivity {

    private static final int VISIBLE_THRESHOLD = 4;// minimum items visibility for load more to trigger next page
    RecyclerView mUserFeed;
    ProgressBar progressBar;

    private boolean isLoading = false;
    private int pageNumber = 1;
    private int[] lastVisibleItem;
    private int totalItemCount;
    private StaggeredGridLayoutManager mLayoutManager;
    private CompositeDisposable mDisposable;
    private PublishProcessor<Integer> mPaginator;
    private ImageAdapter mAdapter;
    private List<Object> mListItems;
    private DownloadUtils mProvider;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_load_more;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getComponent().inject(this); // can use dependencies
        setupResources();
        registerLoadMore();
    }


    private void setupResources() {
        mUserFeed = findViewById(R.id.mUserFeed);
        progressBar = findViewById(R.id.progressBar);
        mLayoutManager = new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL);
        mUserFeed.setLayoutManager(mLayoutManager);
        mListItems = new ArrayList<>();
        mDisposable = new CompositeDisposable();
        mPaginator = PublishProcessor.create();
        mProvider = DownloadUtils.getInstance();
        mAdapter = new ImageAdapter(mListItems, (imageView, imageUrl, pos) -> {
            Intent intent = new Intent(this, FullScreenImageActivity.class);
            intent.putExtra(Utils.IMAGE_URL, imageUrl);
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(LoadMoreActivity.this, imageView, "image");
            startActivity(intent, options.toBundle());
            doSmoothAnimation();
        });
        mUserFeed.setAdapter(mAdapter);
    }

    // register recyclerview scroll listener for pagination
    private void registerLoadMore() {
        mUserFeed.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NotNull RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = mLayoutManager.getItemCount();
                lastVisibleItem = mLayoutManager.findLastVisibleItemPositions(new int[mLayoutManager.getSpanCount()]);
                if (!isLoading
                        && totalItemCount <= (lastVisibleItem[0] + VISIBLE_THRESHOLD)) {
                    pageNumber++;
                    mPaginator.onNext(pageNumber);
                    isLoading = true;
                }
            }
        });
        subscribeApi();
    }

    private void subscribeApi() {
        mDisposable.add(mPaginator
                .onBackpressureDrop()
                .concatMap((Function<Integer, Publisher<List<String>>>) page -> {
                    isLoading = true;
                    progressBar.setVisibility(View.VISIBLE);
                    return mockAPI(page);
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(items -> {
                    mListItems.addAll(items);
                    mAdapter.notifyDataSetChanged();
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                }));

        mPaginator.onNext(pageNumber); // next page request
    }


    /**
     * alternative method without MockApi call
     */
    @SuppressWarnings("unused")
    protected Flowable<List<String>> apiResponse(final int page) {
        return Flowable.just(true)
                .delay(3, TimeUnit.SECONDS)
                .map(value -> {
                    List<String> items = new ArrayList<>();
                    for (int i = 1; i <= 10; i++) {
                        items.add("https://picsum.photos/600/400?image=" + (page * 10 + i));
                    }
                    return items;
                });
    }

    /**
     * call api with retrofit
     */
    protected Flowable<List<String>> mockAPI(final int page) {
        if(mNetworkCheck.isConnected()) {
            return mApiService.getImageURL("http://www.mocky.io/v2/5ca0d6da3700004c008991c1") // Mock API concept add ?mocky-delay=100ms for delay
                    .subscribeOn(Schedulers.io())
                    .map(response -> {
                        if (response.body() != null) {
                            return response.body().get("url").getAsString();
                        } else {
                            return "https://picsum.photos/600/400?image="; // in case if mock api is expired
                        }
                    })
                    .map(value -> { //build unique image URLs
                        List<String> items = new ArrayList<>();
                        for (int i = 1; i <= 10; i++) {
                            items.add(value + (page * 10 + i));
                        }
                        return items;
                    });
        }else{
            return apiResponse(page);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mProvider.resetCache(); // preserve cache per screen only
    }
}
