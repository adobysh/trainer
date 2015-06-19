package com.holypasta.trainer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.holypasta.trainer.Constants;
import com.holypasta.trainer.activity.SingleActivity;
import com.holypasta.trainer.english.R;

public class TheoryFragment extends Fragment implements Constants {

    private int ID_LESSON;
    private boolean FIRST_OPEN;
    private SingleActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (SingleActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity.getSupportActionBar().setTitle(getString(R.string.title_activity_theory));
        View rootView = inflater.inflate(R.layout.fragment_theory, container, false);
        final Bundle extras = getArguments();
        ID_LESSON = extras.getInt(EXTRA_LESSON_ID);
        FIRST_OPEN = extras.getBoolean(EXTRA_FIRST_OPEN, false);
        if (!FIRST_OPEN) {
            AdView mAdView = (AdView) rootView.findViewById(R.id.adView);
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }
        WebView web = (WebView)rootView.findViewById(R.id.webTheory);
        web.loadUrl("file:///android_res/raw/lesson" + (ID_LESSON+1) + ".html");
        return rootView;
    }

}
