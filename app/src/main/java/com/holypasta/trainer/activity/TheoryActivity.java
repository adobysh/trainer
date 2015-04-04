package com.holypasta.trainer.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.webkit.WebView;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.english.R;

public class TheoryActivity extends ActionBarActivity implements Constants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final int ID_LESSON = getIntent().getExtras().getInt(EXTRA_LESSON_ID);
        WebView web = (WebView)findViewById(R.id.webTheory);
        web.loadUrl("file:///android_res/raw/lesson" + (ID_LESSON+1) + ".html");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                super.onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
