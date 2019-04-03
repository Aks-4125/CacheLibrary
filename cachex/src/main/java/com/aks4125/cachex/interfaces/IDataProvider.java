package com.aks4125.cachex.interfaces;

import com.aks4125.cachex.model.DataBridge;

public interface IDataProvider {
    public void markAsDone(DataBridge mBridge);
    public void onFailure(DataBridge mBridge);
    public void markAsCancel(DataBridge mBridge);
}
