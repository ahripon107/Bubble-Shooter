package com.tigersapp.bubbleshooter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.tigersapp.bubbleshooter.activity.BubbleShooterActivity;

/**
 * Created by Ripon on 1/5/17.
 */

public class DiggAPI implements DiggConstant {
    private static SharedPreferences sp;

    public static void start(AppCompatActivity act, Context ctx) {
        AppCompatActivity activity = act;
        Context context = ctx;
        long lastDplTime = DiggUtils.getLastDispalyTime(context);
        long curTime = System.currentTimeMillis();
        if (sp == null) {
            sp = activity.getSharedPreferences(BubbleShooterActivity.PREFS_NAME, 0);
        }
        int maxLevel = sp.getInt("Unlock_level", 0);
        boolean blNetWorkAvailable = DiggUtils.isNetWorkAvailable(context);
        boolean isInstalled = DiggUtils.isInstalled(context, DiggConstant.DIGG_APP_PKGNAME);
        if (blNetWorkAvailable && !isInstalled) {
            if (maxLevel >= 4 || (curTime - lastDplTime >= DiggConstant.DIGG_PERIOD_TIME && maxLevel >= 4)) {
                DiggUtils.setLastDispalyTime(context, curTime);
                Intent it = new Intent();
                it.setFlags(DriveFile.MODE_READ_ONLY);
                it.setClass(context, DiggActivity.class);
                it.putExtra(DiggConstant.IS_AUTO_CLOSE_KEY, true);
                context.startActivity(it);
            }
        }
    }

    public static void start(Activity act, Context ctx) {
        AppCompatActivity activity = (AppCompatActivity) act;
        Context context = ctx;
        long lastDplTime = DiggUtils.getLastDispalyTime(context);
        long curTime = System.currentTimeMillis();
        if (sp == null) {
            sp = activity.getSharedPreferences(BubbleShooterActivity.PREFS_NAME, 0);
        }
        int maxLevel = sp.getInt("Unlock_level", 0);
        boolean blNetWorkAvailable = DiggUtils.isNetWorkAvailable(context);
        boolean isInstalled = DiggUtils.isInstalled(context, DiggConstant.DIGG_APP_PKGNAME);
        if (blNetWorkAvailable && !isInstalled) {
            if (maxLevel >= 4 || (curTime - lastDplTime >= DiggConstant.DIGG_PERIOD_TIME && maxLevel >= 4)) {
                DiggUtils.setLastDispalyTime(context, curTime);
                Intent it = new Intent();
                it.setFlags(DriveFile.MODE_READ_ONLY);
                it.setClass(context, DiggActivity.class);
                it.putExtra(DiggConstant.IS_AUTO_CLOSE_KEY, true);
                context.startActivity(it);
            }
        }
    }

    public static void openMoreBoard(Context ctx) {
        Context context = ctx;
        Intent it = new Intent();
        it.setFlags(DriveFile.MODE_READ_ONLY);
        it.setClass(context, DiggActivity.class);
        it.putExtra(DiggConstant.IS_AUTO_CLOSE_KEY, false);
        context.startActivity(it);
    }
}