package com.du.de.rasandummy.util;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.du.de.rasandummy.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class AdUtils {

    public static AdUtils instance;
    private static boolean isMobileAddInitialized = false;

    private final AdRequest adRequest = new AdRequest.Builder().build();
    private InterstitialAd adInterstitial;

    private AdUtils() {
    }

    public static AdUtils getInstance() {
        if (instance == null) {
            instance = new AdUtils();
        }
        return instance;
    }

    /**
     * Function to init mobile ad at application class.
     * Should be called before loadBannerAd() or loadInterstitial()
     *
     * @param context
     */
    public void initMobileAds(Context context) {
        MobileAds.initialize(context, initializationStatus -> isMobileAddInitialized = true);
    }

    /**
     * Function to load banner ad
     *
     * @param adView AdView
     */
    public void loadBannerAd(AdView adView) {
        if (isMobileAddInitialized && adView != null)
            adView.loadAd(adRequest);
    }

    /**
     * Function to load interstitial ad
     *
     * @param context Context
     */
    public void loadInterstitial(Context context) {
        if (isMobileAddInitialized) {
            String adInterstitialId = context.getResources().getString(R.string.interstitial_id);
            InterstitialAd.load(context, adInterstitialId, adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    adInterstitial = interstitialAd;
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    adInterstitial = null;
                }
            });
        }
    }

    public void showInterstitialAd(Activity activity) {
        if (adInterstitial != null) {
            adInterstitial.show(activity);
        } else {
            loadInterstitial(activity);
        }
    }
}
