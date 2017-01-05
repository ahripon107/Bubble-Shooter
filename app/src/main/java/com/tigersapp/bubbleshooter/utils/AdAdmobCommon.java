package com.tigersapp.bubbleshooter.utils;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.tigersapp.bubbleshooter.MainApplication;
import com.tigersapp.bubbleshooter.R;

/**
 * Created by Ripon on 1/4/17.
 */

public class AdAdmobCommon {

    public static String TAG = "ADS";
    private static AppCompatActivity mActivity;
    private static boolean mIsKeyBack;

    class AnonymousClass2 implements Runnable {
        private final /* synthetic */ AdView val$adView;
        private final /* synthetic */ boolean val$bHide;

        AnonymousClass2(boolean z, AdView adView) {
            this.val$bHide = z;
            this.val$adView = adView;
        }

        public void run() {
            if (this.val$bHide) {
                this.val$adView.setVisibility(4);
            } else {
                this.val$adView.setVisibility(0);
            }
        }
    }

    class AnonymousClass1 extends AdListener {
        private final /* synthetic */ InterstitialAd val$interstitialAd;

        AnonymousClass1(InterstitialAd interstitialAd) {
            this.val$interstitialAd = interstitialAd;
        }

        public void onAdClosed() {
            AdAdmobCommon.requestNewInterstitial(this.val$interstitialAd);
        }
    }

    public static void setBanner(AdView adView) {
        adView.loadAd(new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice("TEST_DEVICE_ID").build());
    }

    public static void setIsKeyBack(boolean isKeyBack) {
        mIsKeyBack = isKeyBack;
    }

    public static void setInterstitialAd(InterstitialAd interstitialAd, AppCompatActivity activity) {
        mActivity = activity;
        interstitialAd.setAdUnitId(MainApplication.getInstance().getResources().getString(R.string.ads_id_intersial));
        interstitialAd.setAdListener(new AnonymousClass1(interstitialAd));
        requestNewInterstitial(interstitialAd);
    }

    public static void setHideAdView(boolean bHide, AdView adView, Activity act) {
        act.runOnUiThread(new AnonymousClass2(bHide, adView));
    }

    public static void requestNewInterstitial(InterstitialAd interstitialAd) {
        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID").build());
    }

    public static void showInterstitial(InterstitialAd interstitialAd) {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    public static void onLoadButtonClick() {
        String toastStr = "Loading Interstitial";
        if (Chartboost.hasInterstitial(CBLocation.LOCATION_LEADERBOARD)) {
            toastStr = "Loading Interstitial From Cache";
        }
        Log.i(TAG, toastStr);
        Chartboost.showInterstitial(CBLocation.LOCATION_LEADERBOARD);
    }

    public static void onMoreButtonClick() {
        String toastStr = "Loading More Apps";
        if (Chartboost.hasMoreApps(CBLocation.LOCATION_GAME_SCREEN)) {
            toastStr = "Loading More Apps From Cache";
        }
        Log.i(TAG, toastStr);
        Chartboost.showMoreApps(CBLocation.LOCATION_GAME_SCREEN);
    }

    public static void onPreloadClick() {
        Log.i(TAG, "Preloading Interstitial Ad");
        Chartboost.cacheInterstitial(CBLocation.LOCATION_LEADERBOARD);
    }

    public static void onPreloadMoreAppsClick() {
        Log.i(TAG, "Preloading More apps Ad");
        Chartboost.cacheMoreApps(CBLocation.LOCATION_GAME_SCREEN);
    }

    public static void onLoadVideoClick() {
        String toastStr = "Loading Rewarded Interstitial";
        if (Chartboost.hasRewardedVideo(CBLocation.LOCATION_ACHIEVEMENTS)) {
            toastStr = "Loading Rewarded Interstitial From Cache";
        }
        Log.i(TAG, toastStr);
        Chartboost.showRewardedVideo(CBLocation.LOCATION_ACHIEVEMENTS);
    }

    public static void onPreloadVideoClick() {
        Log.i(TAG, "Preloading Rewarded Interstitial Ad");
        Chartboost.cacheRewardedVideo(CBLocation.LOCATION_ACHIEVEMENTS);
    }
}
