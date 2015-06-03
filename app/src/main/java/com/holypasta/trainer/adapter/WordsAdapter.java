package com.holypasta.trainer.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.english.R;

import java.util.List;

public class WordsAdapter extends BaseAdapter implements Constants {

    private class ViewHolder {
        public TextView text;
    }

    private LayoutInflater layoutInflater;
    private List<String> words;

    public WordsAdapter(List<String> words, Activity activity) {
        this.words = words;
        this.layoutInflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public Object getItem(int position) {
        return words.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.item_dictionary, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) view.findViewById(R.id.text);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.text.setText(words.get(position));
        return view;
    }
}
