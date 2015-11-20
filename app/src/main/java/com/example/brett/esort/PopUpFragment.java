package com.example.brett.esort;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Maria on 11/19/2015.
 */
public class PopUpFragment extends DialogFragment {

    private String type;
    EditText inputText;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        Bundle typeBundle = getArguments();
        final String type = typeBundle.getString("type");
        int layout_id;

        if(type == null) {
            //close dialog
            layout_id = R.layout.popup_join;
            Toast.makeText(getActivity().getApplicationContext(), "Something went wrong",
                    Toast.LENGTH_LONG).show();
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
                // 1. Read Class Name from edit text
                // 2. Generate a (Unique) Join Code
                // 3. Make Organization Class with Name and Join Code
                // 4. Tell Parse
                // 5. Exit Dialogue
                // 6. Show Join code
                if(type.equals("make")) {
                    make(dialog);
                }
                else if(type.equals("join")) {
                    join(dialog);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Something went wrong",
                            Toast.LENGTH_LONG).show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                PopUpFragment.this.getDialog().cancel();
            }
        });


        return builder.create();
    }

    public void make(DialogInterface dialog){
        inputText = (EditText) ( (AlertDialog)dialog).findViewById(R.id.orgName);
        ParseObject org = new ParseObject("Organization");
        org.put("name", inputText.getText().toString());
        org.put("code", 1234);
        org.saveInBackground();
    }

    public void join(DialogInterface dialog){
        inputText = (EditText) ( (AlertDialog)dialog ).findViewById(R.id.joinCode);
        //check that this passcode exists
        System.out.println("blah");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Organization");
        query.whereEqualTo("code", Integer.valueOf(inputText.getText().toString()));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                System.out.println("doneeeeee");
                if(objects != null && objects.size() > 0){
                    //add person to the class
                    ParseObject myOrg = objects.get(0);


//                    Toast.makeText(getActivity().getApplicationContext(), "cats :3 meowwww",
//                            Toast.LENGTH_LONG).show();
                }
                else {
                    //Toast.makeText(getActivity().getApplicationContext(), "Sorry, that doesn't match anything we have.",
                    //        Toast.LENGTH_LONG).show();
                }
//                listener.getTagsByNameCallback(tagname, objects);
            }
        });



    }
}
