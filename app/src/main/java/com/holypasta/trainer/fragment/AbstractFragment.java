package com.holypasta.trainer.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import com.holypasta.trainer.Constants;

public abstract class AbstractFragment extends Fragment implements Constants {

    protected abstract String getTitle();

    private void setTitle() {
        String text = getTitle();
        ActionBarActivity activity = (ActionBarActivity) getActivity();
        if (activity != null) {
            ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(text);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle();
    }
}
