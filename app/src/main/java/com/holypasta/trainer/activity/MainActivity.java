package com.holypasta.trainer.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.MakeScore;
import com.holypasta.trainer.util.ViewHolder;


public class MainActivity extends ActionBarActivity implements Constants, AdapterView.OnItemClickListener {

    ListView lv1main;
    MyAdapter adapter;
    String[] parts;
    int darkColor = 0;
    int greenColor = 0;

    int[] myScores;
    int activeLvl;

    boolean voiceIsOn;

    SharedPreferences sPref;
    int complete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv1main = (ListView) findViewById(R.id.lv1main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myScores = new int[16];
        sPref = getSharedPreferences(Constants.SCORES, Context.MODE_PRIVATE);
        complete = 0;
        for (int i = 0; i < myScores.length; i++) {
            myScores[i] = sPref.getInt(Constants.SCORE_0_15 + i, 0);
            if (myScores[i] >= Constants.PASS_SCORE) {
                complete++;
            }
        }

        Resources res = getResources();
        parts = res.getStringArray(R.array.contents);
        adapter = new MyAdapter();
        lv1main.setAdapter(adapter);
        lv1main.setOnItemClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sPref = getSharedPreferences(Constants.SCORES, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt(Constants.VOICE, (voiceIsOn == true)?1:0);
        ed.commit();
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return parts.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final ViewHolder holder;
            if (convertView == null) {
                view = getLayoutInflater().inflate(R.layout.item_main,
                        parent, false);
                holder = new ViewHolder();
                holder.textNumber = (TextView) view.findViewById(R.id.textNumber);
                if (greenColor == 0) {
                    greenColor = holder.textNumber.getCurrentTextColor();
                }
                holder.textHard = (TextView) view.findViewById(R.id.textHard);
                holder.textScore = (TextView) view.findViewById(R.id.textScore);
                if (darkColor == 0) {
                    darkColor = holder.textScore.getCurrentTextColor();
                }
                holder.textTitle = (TextView) view.findViewById(R.id.textTitle);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.textNumber.setText((position + 1) + "");
            holder.textScore.setText(MakeScore.make(myScores[position]));
            if (position <= complete) {
                holder.textTitle.setText(parts[position]);

                holder.textNumber.setTextColor(greenColor);
                holder.textHard.setTextColor(greenColor);
                holder.textScore.setTextColor(darkColor);
                holder.textTitle.setTextColor(darkColor);
            } else {
                if (position < Constants.COMPLETE) {
                    holder.textTitle.setText(parts[position]);

                    holder.textNumber.setTextColor(darkColor);
                    holder.textHard.setTextColor(darkColor);
                    holder.textScore.setTextColor(darkColor);
                    holder.textTitle.setTextColor(darkColor);
                } else {
                    holder.textTitle.setText("В разработке");

                    holder.textNumber.setTextColor(Color.LTGRAY);
                    holder.textHard.setTextColor(Color.LTGRAY);
                    holder.textScore.setTextColor(Color.LTGRAY);
                    holder.textTitle.setTextColor(Color.LTGRAY);
                }
            }
            return view;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (arg2 < Constants.COMPLETE) {
            if (arg2 <= complete) {
                activeLvl = arg2; // запоминаем номер открываемого урока, чтобы не сохранять его
                                    // очки перед разрушением активити
                Intent intent = new Intent();
                intent.putExtra(EXTRA_LESSON_ID, arg2);
                intent.setClass(this, LevelActivity.class);
                startActivity(intent);
            } else {
                final AlertDialog aboutDialog = new AlertDialog.Builder(
                        MainActivity.this)
                        .setMessage("Вам необходимо завершить предыдущие уроки")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
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
        if (sPref.getInt(Constants.VOICE, 0) == 1) {
            voiceIsOn = true;
            menu.findItem(R.id.action_voice).setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
		}
        return super.onOptionsItemSelected(item);
    }

}
