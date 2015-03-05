package com.holypasta.trainer.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.MakeScore;
import com.holypasta.trainer.util.ViewHolder;

import java.util.List;

/**
 * Created by q1bot on 24.01.15.
 */
public class LevelsAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<Integer> myScores;
    private String[] parts;
    private int complete;
    private int darkColor = 0;
    private int greenColor = 0;

    public LevelsAdapter(String[] parts, Activity activity, List<Integer> myScores, int complete) {
        this.parts = parts;
        this.layoutInflater = activity.getLayoutInflater();
        this.myScores = myScores;
        this.complete = complete;
    }

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
            view = layoutInflater.inflate(R.layout.item_main,
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
        holder.textScore.setText(MakeScore.make(myScores.get(position)));
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
