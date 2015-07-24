package com.holypasta.trainer.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.holypasta.trainer.Constants;
import com.holypasta.trainer.activity.SingleActivity;
import com.holypasta.trainer.english.R;

import org.androidannotations.annotations.*;

@EFragment(R.layout.fragment_theory)
public class TheoryFragment extends Fragment implements Constants {

    @FragmentArg(EXTRA_LESSON_ID)
    protected int ID_LESSON;

    @ViewById(R.id.webTheory)
    protected WebView web;

    private SingleActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (SingleActivity)activity;
    }

    @AfterViews
    protected void calledAfterViewInjection() {
        ActionBar actionBar = activity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(getString(R.string.title_activity_theory));
        }
        web.loadUrl("file:///android_res/raw/lesson" + (ID_LESSON+1) + ".html");
    }
}