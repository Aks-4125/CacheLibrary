package com.aks4125.cachelibrary.network;

import com.google.gson.JsonObject;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface WebService {
    // retrofit is only used for load more activity.
    @GET
    io.reactivex.Flowable<Response<JsonObject>> getImageURL(@Url String url);
}