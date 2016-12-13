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

public class SetWin extends Activity {
    private static int i;
    public static final String PREFS_NAME = "MENU_PREFS";
    private SharedPreferences settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_win);
        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        i = settings.getInt("num", 0);
    }

    public void onSendButtonClicked(View view) {
        String name;
        int price;
        EditText n, p;

        SharedPreferences.Editor editor = settings.edit();

        n = (EditText) findViewById(R.id.name);
        p = (EditText) findViewById(R.id.price);
        name = n.getText().toString();
        price = Integer.parseInt(p.getText().toString());
        editor.putString("name" + ((Integer) i).toString(), name);
        editor.putInt(name, price);
        n.setText("");
        p.setText("");
        editor.commit();
        i++;
    }

    @Override
    public void onPause() {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("num", i);
        editor.commit();
        super.onPause();
    }
}
