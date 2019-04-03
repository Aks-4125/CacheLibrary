package com.aks4125.cachex.interfaces;

import com.aks4125.cachex.model.DataBridge;

public interface IDataBridge {

    public void onStart(DataBridge mData);

    public void onSuccess(DataBridge mData);

    public void onFailure(DataBridge mData, int statusCode, byte[] errorResponse, Throwable e);

    public void onRetry(DataBridge mData, int retryNumber);
}
