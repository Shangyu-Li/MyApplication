package com.example.dan.myapplication.display_win;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.dan.myapplication.R;

/**
 * Created by Dan on 12/13/2016.
 */

public class SetCategory extends Activity {
    private static int i;
    public static final String PREFS_NAME = "MENU_PREFS";
    private SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_win);
        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        i = settings.getInt("CateNum", 0);
    }

    public void onSendButtonClicked(View view) {
        String name;
        EditText n;

        SharedPreferences.Editor editor = settings.edit();

        n = (EditText) findViewById(R.id.name);
        name = n.getText().toString();
        editor.putString("Cate"+Integer.toString(i), name);
        n.setText("");
        editor.commit();
        i++;
    }

    @Override
    public void onPause() {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("CateNum", i);
        editor.commit();
        super.onPause();
    }

}
