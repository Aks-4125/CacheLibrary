package com.aks4125.cachex;


import android.util.Log;



/**
 * TO handle all the types of logs.
 */
public class AppLogger {

    private static final String TAG =  "CacheLibrary";

    private AppLogger() {
    }

    public static void i(String className, String msg) {
        Log.i(TAG, className + ":" + msg);
    }

    public static void e(String className, String msg, Throwable e) {
        Log.e(TAG, className + ":" + msg + ":" + Log.getStackTraceString(e));
    }

    public static void e(String className, String msg) {
        Log.e(TAG, className + ":" + msg);
    }

    public static void d(String className, String msg) {
        Log.d(TAG, className + ":" + msg);
    }

}
