package com.aks4125.cachex;

import com.aks4125.cachex.model.DataBridge;

import androidx.collection.LruCache;

public class CacheManager {
    private static CacheManager mInstance;
    private int mCacheSize;
    private LruCache<String, DataBridge> mDownloadDataBridge;


    public CacheManager() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        mCacheSize = maxMemory / 8;
        mDownloadDataBridge = new LruCache<>(mCacheSize);
    }

    //single instance
    public static CacheManager getInstance() {
        if (mInstance == null) {
            mInstance = new CacheManager();
        }
        return mInstance;
    }

    public DataBridge getDataByKey(String uniqueKey) {
        return mDownloadDataBridge.get(uniqueKey);
    }
    public boolean putDataInCache(String key, DataBridge mData) {
        return mDownloadDataBridge.put(key, mData) != null;
    }

    public void resetCache(){
        mDownloadDataBridge.evictAll();
    }
    public void clearCacheForKey(String key){
        mDownloadDataBridge.remove(key);
    }


}
