package com.aks4125.cachex;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.aks4125.cachex.interfaces.IDataBridge;
import com.aks4125.cachex.interfaces.IDataProvider;
import com.aks4125.cachex.model.DataBridge;
import com.loopj.android.http.AsyncHttpClient;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

public class DownloadUtils {
    private static final String TAG = DownloadUtils.class.getSimpleName();
    private static DownloadUtils mInstance;
    private HashMap<String, LinkedList<DataBridge>> allRequestsByKey = new HashMap<>();
    private HashMap<String, AsyncHttpClient> allRequestsClient = new HashMap<>();
    private CacheManager mCacheManager;

    private DownloadUtils() {
        mCacheManager = CacheManager.getInstance();
    }

    public static DownloadUtils getInstance() {
        if (mInstance == null) {
            mInstance = new DownloadUtils();
        }
        return mInstance;
    }


    public void getRequest(final DataBridge mDownloadDataType) {
        final String mKey = mDownloadDataType.getFileKey();
        // Check if exist in the cache
        DataBridge mDownloadDataTypeFromCache = mCacheManager.getDataByKey(mKey);
        if (mDownloadDataTypeFromCache != null) {
            mDownloadDataTypeFromCache.source = "Cache";
            // call interface
            IDataBridge imDownloadDataType = mDownloadDataType.getDataObserver();
            imDownloadDataType.onStart(mDownloadDataTypeFromCache);
            imDownloadDataType.onSuccess(mDownloadDataTypeFromCache);
            return;
        }

        // This will run if two request come for same url
        if (allRequestsByKey.containsKey(mKey)) {
            mDownloadDataType.source = "Cache";
            allRequestsByKey.get(mKey).add(mDownloadDataType);
            return;
        }

        // Put it in the allRequestsByKey to manage it after done or cancel
        if (allRequestsByKey.containsKey(mKey)) {
            allRequestsByKey.get(mKey).add(mDownloadDataType);
        } else {
            LinkedList<DataBridge> lstMDDataType = new LinkedList<>();
            lstMDDataType.add(mDownloadDataType);
            allRequestsByKey.put(mKey, lstMDDataType);
        }

        // Create new MDownloadDataType by clone it from the parameter
        DataBridge newMDownloadDataType = mDownloadDataType.getClone(new IDataBridge() {
            @Override
            public void onStart(DataBridge mDownloadDataType) {
                for (DataBridge m : allRequestsByKey.get(mDownloadDataType.getFileKey())) {
                    m.setDataArray(mDownloadDataType.getDataArray());
                    m.getDataObserver().onStart(m);
                }
            }

            @Override
            public void onSuccess(DataBridge mDownloadDataType) {
                for (DataBridge m : Objects.requireNonNull(allRequestsByKey.get(mDownloadDataType.getFileKey()))) {
                    m.setDataArray(mDownloadDataType.getDataArray());
                    AppLogger.d(TAG, "Source-> " + m.source);
                    m.getDataObserver().onSuccess(m);
                    if (m.getTargetView() != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(m.getDataArray() , 0, m.getDataArray().length);
                        m.getTargetView().setImageBitmap(bitmap);

                    }
                }
                allRequestsByKey.remove(mDownloadDataType.getFileKey());
            }

            @Override
            public void onFailure(DataBridge mDownloadDataType, int statusCode, byte[] errorResponse, Throwable e) {
                for (DataBridge m : Objects.requireNonNull(allRequestsByKey.get(mDownloadDataType.getFileKey()))) {
                    m.setDataArray(mDownloadDataType.getDataArray());
                    m.getDataObserver().onFailure(m, statusCode, errorResponse, e);
                }
                allRequestsByKey.remove(mDownloadDataType.getFileKey());
            }

            @Override
            public void onRetry(DataBridge mDownloadDataType, int retryNo) {
                for (DataBridge m : Objects.requireNonNull(allRequestsByKey.get(mDownloadDataType.getFileKey()))) {
                    m.setDataArray(mDownloadDataType.getDataArray());
                    m.getDataObserver().onRetry(m, retryNo);
                }
            }
        });

        // Get from download manager
        final FileDownloader mDownloadAsyncManager = new FileDownloader();
        AsyncHttpClient client = mDownloadAsyncManager.get(newMDownloadDataType, new IDataProvider() {
            @Override
            public void markAsDone(DataBridge mDownloadDataType) {
                // put in the cache when mark as done
                mCacheManager.putDataInCache(mDownloadDataType.getFileKey(), mDownloadDataType);
                AppLogger.d(TAG, "Cache Status->" + "ADDED");
                allRequestsClient.remove(mDownloadDataType.getFileKey());
            }

            @Override
            public void onFailure(DataBridge mDownloadDataType) {
                allRequestsClient.remove(mDownloadDataType.getFileKey());
            }

            @Override
            public void markAsCancel(DataBridge mDownloadDataType) {
                allRequestsClient.remove(mDownloadDataType.getFileKey());
            }
        });

        allRequestsClient.put(mKey, client);
    }
    public void resetCache(){
        mCacheManager.resetCache();
    }
    public void clearCacheForKey(String key){
        mCacheManager.clearCacheForKey(key);
    }


}
