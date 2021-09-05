package com.holypasta.trainer.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.app.Fragment;
//import android.app.Fragment;
//import android.support.v7.app.ActionBarActivity;

import com.holypasta.trainer.Constants;

public abstract class AbstractFragment extends Fragment implements Constants {

    protected abstract String getTitle();

    private void setTitle() {
        String text = getTitle();
        Activity activity = getActivity();
        if (activity != null) {
            ActionBar actionBar = activity.getActionBar();
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
