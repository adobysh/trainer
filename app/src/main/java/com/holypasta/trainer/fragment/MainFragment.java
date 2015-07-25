package com.holypasta.trainer.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.activity.SingleActivity;
import com.holypasta.trainer.adapter.LevelsAdapter;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.SharedPreferencesUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.fragment_main)
public class MainFragment extends AbstractFragment implements AdapterView.OnItemClickListener {

    @ViewById
    protected ListView lv1main;
    private LevelsAdapter adapter;
    private List<Integer> scores;
    private int mode;
    private SingleActivity activity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (SingleActivity)activity;
    }

    @AfterViews
    void calledAfterViewInjection() {
        setHasOptionsMenu(true);
        mode = SharedPreferencesUtil.getInstance(getActivity()).getMode();
    }

    @Override
    protected void setTitle() {
        activity.getSupportActionBar().setTitle(getString(R.string.app_name));
    }

    @Override
    public void onResume() {
        super.onResume();
        String[] parts = getResources().getStringArray(R.array.contents);
        scores = SharedPreferencesUtil.getInstance(getActivity()).getScores();
        adapter = new LevelsAdapter(parts, activity, scores);
        lv1main.setAdapter(adapter);
        lv1main.setOnItemClickListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferencesUtil.getInstance(getActivity()).saveMode(mode);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int levelId, long arg3) {
        if (levelId < Constants.COMPLETE) {
            int score = scores.get(levelId);
            if (score > -1) {
                Bundle arguments = new Bundle();
                arguments.putInt(EXTRA_LESSON_ID, levelId);
                AbstractLevelFragment levelFragment;
                switch (mode) {
                    case MODE_HARD:
                        levelFragment = new LevelHardFragment();
                        break;
                    case MODE_EASY:
                        levelFragment = new LevelEasyFragment();
                        break;
                    default:
                        return;
                }
                levelFragment.setArguments(arguments);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, levelFragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                final AlertDialog aboutDialog = new AlertDialog.Builder(activity)
                        .setMessage("Вам необходимо завершить предыдущие уроки")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) { }
                        }).create();
                aboutDialog.show();
            }
        }
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.main_menu, menu);
//        if (mode == MODE_HARD) {
//            menu.findItem(R.id.action_hardcore_mode).setChecked(true);
//        }
//        super.onCreateOptionsMenu(menu,inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_hardcore_mode:
//                if (item.isChecked() == true) {
//                    item.setChecked(false);
//                    mode = MODE_EASY;
//                } else {
//                    item.setChecked(true);
//                    mode = MODE_HARD;
//                }
//                break;
//		}
//        return super.onOptionsItemSelected(item);
//    }

}
