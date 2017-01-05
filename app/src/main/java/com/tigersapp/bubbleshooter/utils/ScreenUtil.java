package com.tigersapp.bubbleshooter.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Ripon on 1/5/17.
 */

public class ScreenUtil {

    public static int dip2px(Context context, float dipValue) {
        return (int) ((dipValue * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        return (int) ((pxValue / context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static int dip2px(DisplayMetrics metrics, float dipValue) {
        return (int) ((dipValue * metrics.density) + 0.5f);
    }

    public static int px2dip(DisplayMetrics metrics, float pxValue) {
        return (int) ((pxValue / metrics.density) + 0.5f);
    }

    public static boolean isLargeScreen(Context ctx) {
        int tmpMaxPixel;
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        int widthPixel = metrics.widthPixels;
        int heightPixel = metrics.heightPixels;
        if (widthPixel > heightPixel) {
            tmpMaxPixel = widthPixel;
        } else {
            tmpMaxPixel = heightPixel;
        }
        if (tmpMaxPixel >= 800) {
            return true;
        }
        return false;
    }
}
