package com.holypasta.trainer.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.adapter.MyAdapter;
import com.holypasta.trainer.english.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements Constants, AdapterView.OnItemClickListener {

    private ListView lv1main;
    private MyAdapter adapter;
    private List<Integer> myScores;
    private boolean voiceIsOn;
    private SharedPreferences sPref;
    private int complete;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv1main = (ListView) findViewById(R.id.lv1main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myScores = new ArrayList<Integer>();
        sPref = getSharedPreferences(Constants.SCORES, Context.MODE_PRIVATE);
        String[] parts = getResources().getStringArray(R.array.contents);
        complete = 0;
        for (int i = 0; i < parts.length; i++) {
            int score = sPref.getInt(Constants.SCORE_0_15 + i, 0);
            myScores.add(score);
            if (score >= Constants.PASS_SCORE) {
                complete++;
            }
        }
        adapter = new MyAdapter(parts, this, myScores, complete);
        lv1main.setAdapter(adapter);
        lv1main.setOnItemClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sPref = getSharedPreferences(Constants.SCORES, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt(Constants.PREF_VOICE, (voiceIsOn == true)?1:0);
        ed.putInt(Constants.PREF_HARDCORE_MODE, mode);
        ed.commit();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (arg2 < Constants.COMPLETE) {
            if (arg2 <= complete) {
                Intent intent = new Intent();
                intent.putExtra(EXTRA_LESSON_ID, arg2);
                intent.putExtra(EXTRA_MODE, mode);
                intent.setClass(this, LevelActivity.class);
                startActivity(intent);
            } else {
                final AlertDialog aboutDialog = new AlertDialog.Builder(
                        MainActivity.this)
                        .setMessage("Вам необходимо завершить предыдущие уроки")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).create();
                aboutDialog.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        sPref = getSharedPreferences(Constants.SCORES, Context.MODE_PRIVATE);
        if (sPref.getInt(Constants.PREF_VOICE, 0) == 1) {
            voiceIsOn = true;
            menu.findItem(R.id.action_voice).setChecked(true);
        }
        mode = sPref.getInt(Constants.PREF_HARDCORE_MODE, 0);
        if (mode == 1) {
            menu.findItem(R.id.action_hardcore_mode).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_voice:
                if (item.isChecked() == true) {
                    item.setChecked(false);
                    voiceIsOn = false;
                } else {
                    item.setChecked(true);
                    voiceIsOn = true;
                }
                break;
            case R.id.action_hardcore_mode:
                if (item.isChecked() == true) {
                    item.setChecked(false);
                    mode = MODE_EASY;
                } else {
                    item.setChecked(true);
                    mode = MODE_HARD;
                }
                break;
		}
        return super.onOptionsItemSelected(item);
    }

}
