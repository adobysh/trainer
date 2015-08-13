package com.holypasta.trainer.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class DonateFragment extends AbstractFragment {

    private InterstitialAd interstitialAd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        interstitialAd = new InterstitialAd(getActivity());
        interstitialAd.setAdUnitId("ca-app-pub-3369297231287003/1456297972");
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                interstitialAd.show();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                try {
                    getActivity().onBackPressed();
                } catch (RuntimeException e) {
                    System.out.println("!!! crash :(");
                    /* support library bug https://code.google.com/p/android/issues/detail?id=19917 */
                }
            }
//            @Override
//            public void onAdClosed() {
//                requestNewInterstitial();
//            }
        });
        requestNewInterstitial();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder() // .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();
        interstitialAd.loadAd(adRequest);
    }

    @Override
    protected String getTitle() {
        return "Поддержать";
    }
}
