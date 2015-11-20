package com.example.brett.esort;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;

/**
 * Created by Maria on 11/19/2015.
 */
public class PopUpFragment extends DialogFragment {

    private String type;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Bundle typeBundle = getArguments();
        String type = typeBundle.getString("type");
        int layout_id;

        if(type == null) {
            layout_id = R.layout.popup_join;
        }
        else if(type.equals("join")) {
            layout_id = R.layout.popup_join;
        } else {
            layout_id = R.layout.popup_make;
        }


        builder.setView(inflater.inflate(layout_id, null));


                // Add action buttons
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                //
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                PopUpFragment.this.getDialog().cancel();
            }
        });


        return builder.create();
    }
}
