package com.example.dan.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.dan.myapplication.display_win.DisplayWin;
import com.example.dan.myapplication.execute_win.ExecuteWin;
import com.example.dan.myapplication.history_win.HistoryWin;

public class MainMenu extends AppCompatActivity {
    public static final String PREFS_NAME = "MENU_PREFS";

    static final int REQUEST_CODE = 1;
    Bundle mbundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

//        settings.edit().clear().commit();
//        settings.edit().putInt("num", 0).commit();
    }

    public void onEnterButtonClicked(View view) {
        Intent intent = new Intent(this, ExecuteWin.class);
        startActivity(intent);
    }

    public void onSetButtonClicked(View view) {
        Intent intent = new Intent(this, DisplayWin.class);
        startActivity(intent);
    }

    public void onHistoryButtonClicked(View view){
        Intent intent = new Intent(this, HistoryWin.class);
        startActivity(intent);
    }
}
