package com.holypasta.trainer.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.activity.SingleActivity;
import com.holypasta.trainer.adapter.LevelsAdapter;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.JesusSaves;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EFragment(R.layout.fragment_main)
public class MainFragment extends AbstractFragment implements AdapterView.OnItemClickListener {

    @ViewById
    ListView lv1main;
    @ViewById
    Button buttonRepeatLessons;
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
        if (JesusSaves.getInstance(getActivity()).getLastOpenLessonId() == 0) {
            buttonRepeatLessons.setVisibility(View.GONE);
        }
        mode = JesusSaves.getInstance(getActivity()).getMode();
    }

    @Override
    protected String getTitle() {
        return getString(R.string.app_name);
    }

    @Override
    public void onResume() {
        super.onResume();
        String[] parts = getResources().getStringArray(R.array.contents);
        scores = JesusSaves.getInstance(getActivity()).getScores();
        adapter = new LevelsAdapter(parts, activity, scores);
        lv1main.setAdapter(adapter);
        lv1main.setOnItemClickListener(this);
    }

    @Click
    void buttonRepeatLessons() {
        openLesson(REPEAT_LESSONS_LESSON);
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, final int lessonId, long arg3) {
        if (lessonId < Constants.COMPLETE) {
            int score = scores.get(lessonId);
            if (score > -1) {
                openLesson(lessonId);
            } else {
                final AlertDialog aboutDialog = new AlertDialog.Builder(activity)
                        .setMessage("Вам необходимо завершить предыдущие уроки")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Разблокировать", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                JesusSaves.getInstance(getActivity()).unlockUntoLesson(lessonId);
                                openLesson(lessonId);
                            }
                        }).create();
                aboutDialog.show();
            }
        }
    }

    private void openLesson(int lessonId) {
        SingleActivity singleActivity = (SingleActivity) getActivity();
        if (singleActivity == null) return;

        Bundle arguments = new Bundle();
        arguments.putInt(EXTRA_LESSON_ID, lessonId);
        AbstractLevelFragment levelFragment = mode == MODE_EASY ? new LevelEasyFragment() : new LevelHardFragment();
        singleActivity.openFragment(levelFragment, arguments);
    }

}
