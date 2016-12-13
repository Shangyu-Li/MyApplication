package com.example.dan.myapplication.display_win;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.dan.myapplication.R;

/**
 * Created by Dan on 10/21/2016.
 */

public class SetDetail extends Activity {
    private static int i;
    private static final String PREFS_NAME = "MENU_PREFS";
    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_detail);
        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        i = settings.getInt(getIntent().getStringExtra("item") + "num", 0);
    }

    public void onSendButtonClicked(View view) {
        String name;
        EditText n;

        SharedPreferences.Editor editor = settings.edit();

        n = (EditText) findViewById(R.id.name);
        name = n.getText().toString();
        editor.putString(getIntent().getStringExtra("item") + ((Integer) i).toString(), name);
        n.setText("");
        editor.commit();
        i++;
    }

    @Override
    public void onPause() {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(getIntent().getStringExtra("item") + "num", i);
        editor.commit();
        super.onPause();
    }
}

