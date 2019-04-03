package com.aks4125.cachex;

import com.aks4125.cachex.interfaces.IDataProvider;
import com.aks4125.cachex.model.DataBridge;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class FileDownloader {
    public FileDownloader() {
        /*required*/
    }

    public AsyncHttpClient get(final DataBridge mDataBridge, final IDataProvider mProvider) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(mDataBridge.getImageURL(), new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                // called before request is started
                mDataBridge.getDataObserver().onStart(mDataBridge);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                mDataBridge.setDataArray(response);
                mDataBridge.getDataObserver().onSuccess(mDataBridge);

                // This call for provider to manage it
                mProvider.markAsDone(mDataBridge);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                mDataBridge.getDataObserver().onFailure(mDataBridge, statusCode, errorResponse, e);

                // This call for provider to manage it
                mProvider.onFailure(mDataBridge);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
                mDataBridge.getDataObserver().onRetry(mDataBridge, retryNo);
            }

            @Override
            public void onCancel() {
                super.onCancel();

                // called when request is retried
                mProvider.markAsCancel(mDataBridge);
            }
        });

        return client;
    }

}
