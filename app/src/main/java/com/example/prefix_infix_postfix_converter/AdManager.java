package com.example.prefix_infix_postfix_converter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.work.Configuration;
import androidx.work.WorkManager;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.concurrent.atomic.AtomicBoolean;

public final class AdManager {

    private static final long AD_COOLDOWN_MS = 60_000L;
    private static final AtomicBoolean sInitialized = new AtomicBoolean(false);
    private static final Handler MAIN_HANDLER = new Handler(Looper.getMainLooper());

    private static InterstitialAd sInterstitialAd;
    private static boolean sIsLoading = false;
    private static long sLastAdShownRealtimeMs = 0L;

    private AdManager() {
    }

    public static void initialize(@NonNull Context context) {
        final Context appContext = context.getApplicationContext();
        if (sInitialized.compareAndSet(false, true)) {
            new Thread(() -> {
                ensureWorkManagerInitialized(appContext);
                MobileAds.initialize(appContext, initializationStatus ->
                        MAIN_HANDLER.post(() -> loadInterstitial(appContext)));
            }, "AdMob-Init").start();
        }
    }

    private static void ensureWorkManagerInitialized(@NonNull Context appContext) {
        try {
            if (!WorkManager.isInitialized()) {
                WorkManager.initialize(appContext, new Configuration.Builder().build());
            }
        } catch (Throwable ignored) {
        }
    }

    public static void loadInterstitial(@NonNull Context context) {
        final Context appContext = context.getApplicationContext();
        if (Looper.myLooper() != Looper.getMainLooper()) {
            MAIN_HANDLER.post(() -> loadInterstitial(appContext));
            return;
        }
        if (sInterstitialAd != null || sIsLoading) {
            return;
        }
        sIsLoading = true;
        AdRequest adRequest = new AdRequest.Builder().build();
        String adUnitId = appContext.getString(R.string.admob_interstitial_id);

        InterstitialAd.load(appContext, adUnitId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                sInterstitialAd = interstitialAd;
                sIsLoading = false;
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                sInterstitialAd = null;
                sIsLoading = false;
            }
        });
    }

    public static void showInterstitialIfReady(@NonNull Activity activity, @NonNull Runnable onComplete) {
        final Context appContext = activity.getApplicationContext();
        long now = SystemClock.elapsedRealtime();
        boolean cooldownActive = sLastAdShownRealtimeMs > 0L
                && (now - sLastAdShownRealtimeMs) < AD_COOLDOWN_MS;

        if (sInterstitialAd != null && !cooldownActive && !activity.isFinishing() && !activity.isDestroyed()) {
            InterstitialAd adToShow = sInterstitialAd;
            sInterstitialAd = null;
            adToShow.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdShowedFullScreenContent() {
                    sLastAdShownRealtimeMs = SystemClock.elapsedRealtime();
                }

                @Override
                public void onAdDismissedFullScreenContent() {
                    loadInterstitial(appContext);
                    onComplete.run();
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    loadInterstitial(appContext);
                    onComplete.run();
                }
            });
            adToShow.show(activity);
        } else {
            if (sInterstitialAd == null) {
                loadInterstitial(appContext);
            }
            onComplete.run();
        }
    }
}
