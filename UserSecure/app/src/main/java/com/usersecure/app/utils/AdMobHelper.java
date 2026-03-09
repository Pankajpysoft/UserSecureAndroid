package com.usersecure.app.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Helper class for initializing Google AdMob and loading banner ads.
 * Uses Google's test ad unit IDs by default — replace with real IDs before publishing.
 */
public class AdMobHelper {

    private static final String TAG = "AdMobHelper";

    /**
     * Initialize the AdMob SDK. Call once in Application or MainActivity.onCreate().
     */
    public static void initialize(Context context) {
        MobileAds.initialize(context, initializationStatus -> {
            Log.d(TAG, "AdMob initialized: " + initializationStatus.toString());
        });
    }

    /**
     * Load a banner ad into the given AdView.
     *
     * @param adView the AdView to load the banner into
     */
    public static void loadBanner(AdView adView) {
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }
}
