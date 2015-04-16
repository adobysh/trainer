package com.holypasta.trainer.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.english.R;
import com.holypasta.trainer.util.ViewHolder;

import java.util.List;

/**
 * Created by q1bot on 24.01.15.
 */
public class LevelsAdapter extends BaseAdapter implements Constants {

    private LayoutInflater layoutInflater;
    private List<Integer> scores;
    private String[] parts;
    private Drawable levelComplete;
    private Drawable levelOpened;
    private Drawable levelClosed;

    public LevelsAdapter(String[] parts, Activity activity, List<Integer> scores) {
        this.parts = parts;
        this.layoutInflater = activity.getLayoutInflater();
        this.scores = scores;
        Resources resources = layoutInflater.getContext().getResources();
        levelComplete = resources.getDrawable(R.drawable.item_level_complete);
        levelOpened = resources.getDrawable(R.drawable.item_level_opened);
        levelClosed = resources.getDrawable(R.drawable.item_level_closed);
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
            holder.textTitle = (TextView) view.findViewById(R.id.textTitle);
            holder.image = (ImageView) view.findViewById(R.id.image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.textNumber.setText((position + 1) + "");
        int itemScore = scores.get(position);
        if (itemScore > -1 && position < Constants.COMPLETE) {
            if (itemScore == MAX_SCORE) {
                holder.image.setImageDrawable(levelComplete);
            } else {
                holder.image.setImageDrawable(levelOpened);
            }
            holder.textTitle.setText(parts[position]);
            holder.textTitle.setTextColor(Color.BLACK);
        } else {
            if (position < Constants.COMPLETE) {
                holder.textTitle.setText(parts[position]);
            } else {
                holder.textTitle.setText("В разработке");
            }
            holder.image.setImageDrawable(levelClosed);
            holder.textTitle.setTextColor(Color.LTGRAY);
        }
        return view;
    }
}
