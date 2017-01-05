package com.tigersapp.bubbleshooter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Ripon on 1/5/17.
 */

public class DiggUtils {

    private static final String DIGG_PREFERENCES = "digg.preferences";

    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean isInstalled(Context ctx, String packageName) {
        SharedPreferences sp = ctx.getSharedPreferences(DIGG_PREFERENCES, 0);
        if (sp.contains(packageName)) {
            return true;
        }
        try {
            ctx.getPackageManager().getPackageInfo(packageName, 1);
            sp.edit().putInt(packageName, 1).commit();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean setLastDispalyTime(Context context, long value) {
        return context.getSharedPreferences(DIGG_PREFERENCES, 0).edit().putLong("last_dig_time", value).commit();
    }

    public static long getLastDispalyTime(Context context) {
        return context.getSharedPreferences(DIGG_PREFERENCES, 0).getLong("last_dig_time", -1);
    }
}
