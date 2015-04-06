package com.holypasta.trainer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import com.holypasta.trainer.Constants;
import com.holypasta.trainer.english.R;

public class TheoryActivity extends ActionBarActivity implements Constants {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theory);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Bundle extras = getIntent().getExtras();
        final int ID_LESSON = extras.getInt(EXTRA_LESSON_ID);
        final boolean FIRST_OPEN = extras.getBoolean(EXTRA_FIRST_OPEN, false);
        if (FIRST_OPEN) {
            Button buttonFirstOpen = (Button) findViewById(R.id.button_start_lesson);
            buttonFirstOpen.setVisibility(View.VISIBLE);
            buttonFirstOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_LESSON_ID, ID_LESSON);
                    intent.putExtra(EXTRA_MODE, extras.getInt(EXTRA_MODE));
                    intent.setClass(TheoryActivity.this, LevelActivity.class);
                    startActivity(intent);
                    }
            });
        }
        WebView web = (WebView)findViewById(R.id.webTheory);
        web.loadUrl("file:///android_res/raw/lesson" + (ID_LESSON+1) + ".html");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.theory_menu, menu);
        return true;
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
