package com.aks4125.cachex.model;

import android.widget.ImageView;

import com.aks4125.cachex.interfaces.IDataBridge;


public abstract class DataBridge {
    // User For Just For Test
    public String source = "Network";
    private String imageURL;
    private byte[] mDataArray;
    private FileTypeEnum mFileType;
    private IDataBridge mDataObserver;
    private ImageView mImageView;
    private String mFileKey;

    /*for Image*/
    DataBridge(ImageView mView, String mUrl, FileTypeEnum mFileType, IDataBridge mDataObserver) {
        this.mImageView = mView;
        this.imageURL = mUrl;
        this.mFileType = mFileType;
        this.mDataObserver = mDataObserver;
        this.mFileKey = getFileHash(imageURL);
    }

    /*for file*/
    DataBridge(String mUrl, FileTypeEnum mFileType, IDataBridge mDataObserver) {
        this.imageURL = mUrl;
        this.mFileType = mFileType;
        this.mDataObserver = mDataObserver;
        this.mFileKey = getFileHash(imageURL);
    }

    public ImageView getTargetView() {
        return mImageView;
    }

    public FileTypeEnum getFileType() {
        return mFileType;
    }

    public String getFileKey() {
        return mFileKey;
    }

    public String getImageURL() {
        return imageURL;
    }

    public byte[] getDataArray() {
        return mDataArray;
    }

    public void setDataArray(byte[] mDataArray) {
        this.mDataArray = mDataArray;
    }

    public IDataBridge getDataObserver() {
        return mDataObserver;
    }

    private String getFileHash(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte anArray : array) {
                sb.append(Integer.toHexString((anArray & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException ignored) {
        }
        return null;
    }

    public abstract DataBridge getClone(IDataBridge iDataBridge);

}
