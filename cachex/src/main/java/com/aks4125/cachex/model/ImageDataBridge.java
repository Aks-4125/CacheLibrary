package com.aks4125.cachex.model;

import android.widget.ImageView;

import com.aks4125.cachex.interfaces.IDataBridge;

public class ImageDataBridge extends DataBridge{

    public ImageDataBridge(ImageView mView, String mUrl, IDataBridge mDataBridge) {
        super(mView,mUrl, FileTypeEnum.IMAGE, mDataBridge);
    }

    @Override
    public DataBridge getClone(IDataBridge mDataBridge) {
        return new ImageDataBridge(getTargetView(),getImageURL(),mDataBridge);
    }



}
