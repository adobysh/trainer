package com.holypasta.trainer.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.holypasta.trainer.english.R;

public class TheoryFragment extends AbstractFragment {

    private int ID_LESSON;
    private WebView web;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_theory, container, false);
        web = (WebView) rootView.findViewById(R.id.webTheory);
        // --- after views -------
        web.loadUrl("file:///android_res/raw/lesson" + (ID_LESSON+1) + ".html");
        final Bundle extras = getArguments();
        ID_LESSON = extras.getInt(EXTRA_LESSON_ID);
        return rootView;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.title_activity_theory) + ". " + (ID_LESSON+1) + " урок ";
    }
}