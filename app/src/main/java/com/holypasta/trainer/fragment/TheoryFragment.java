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
public class TheoryFragment extends AbstractFragment {

    @FragmentArg(EXTRA_LESSON_ID)
    int ID_LESSON;

    @ViewById(R.id.webTheory)
    WebView web;

    @AfterViews
    void calledAfterViewInjection() {
        web.loadUrl("file:///android_res/raw/lesson" + (ID_LESSON+1) + ".html");
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_activity_theory) + ". " + (ID_LESSON+1) + " урок ";
    }
}