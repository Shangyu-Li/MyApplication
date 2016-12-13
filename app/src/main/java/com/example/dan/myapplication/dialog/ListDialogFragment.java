package com.example.dan.myapplication.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Created by Dan on 12/9/2016.
 */

public class ListDialogFragment extends android.support.v4.app.DialogFragment {
    private SharedPreferences mSettings;

    public ListDialogFragment(SharedPreferences settings){
        super();
        mSettings = settings;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setTitle("Statistic")
                .setAdapter(new DialogListAdapter(getContext(), mSettings), null);
        return builder.create();
    }
}
