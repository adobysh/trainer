package com.holypasta.trainer.fragment;

import android.support.v4.app.Fragment;

import com.holypasta.trainer.Constants;

public abstract class AbstractFragment extends Fragment implements Constants {

    protected abstract void setTitle();

    @Override
    public void onResume() {
        super.onResume();
        setTitle();
    }
}
