package com.tigersapp.bubbleshooter;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import java.util.HashMap;

/**
 * Created by Ripon on 1/4/17.
 */

public class MainApplication extends Application {
    private static volatile MainApplication mInstance;
    private HashMap<String, Object> mData = new HashMap();

    public MainApplication() {
        mInstance = this;
    }

    @SuppressLint({"NewApi"})
    public void onCreate() {
        super.onCreate();
    }

    public static MainApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        if (mInstance != null) {
            return mInstance.getApplicationContext();
        }
        return null;
    }

    public void putData(String key, Object data) {
        this.mData.put(key, data);
    }

    public Object getData(String key) {
        return this.mData.get(key);
    }

    public void removeData(String key) {
        this.mData.remove(key);
    }
}