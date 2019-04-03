package com.aks4125.cachex.model;

import com.aks4125.cachex.interfaces.IDataBridge;

public class FileDataBridge extends DataBridge {
    public FileDataBridge(String mUrl, IDataBridge mDataBridge) {
        super(mUrl, FileTypeEnum.JSON, mDataBridge);
    }

    @Override
    public DataBridge getClone(IDataBridge mDataBridge) {
        return new FileDataBridge(getImageURL(), mDataBridge);
    }


}
