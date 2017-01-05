package com.tigersapp.bubbleshooter.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Ripon on 1/5/17.
 */

public class ViewUtils {

    public static View inflate(Context pContext, int pLayoutID) {
        return LayoutInflater.from(pContext).inflate(pLayoutID, null);
    }
}
