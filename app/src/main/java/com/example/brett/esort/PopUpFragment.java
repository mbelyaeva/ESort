package com.example.brett.esort;

import android.app.Activity;
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
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * Created by Maria on 11/19/2015.
 */
public class PopUpFragment extends DialogFragment {

    private String type;
    EditText inputText;
    final int MAX_JOIN_CODE = 999999;
    final int MIN_JOIN_CODE = 100000;

    private PopupDialogListener mListener;

    public interface PopupDialogListener {
        void onDialogJoinTeamSuccess(ParseObject org);
        void onDialogJoinTeamFailure(String err);
        void onDialogMakeTeamSuccess(ParseObject org);
        void onDialogMakeTeamFailure(String err);
        void onDialogCancel();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PopupDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement PopupDialogListener");
        }
    }


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

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
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
                mListener.onDialogCancel();
                PopUpFragment.this.getDialog().cancel();
            }
        });


        return builder.create();
    }

    public void make(DialogInterface dialog){
        inputText = (EditText) ( (AlertDialog)dialog).findViewById(R.id.orgName);
        final ParseObject org = new ParseObject("Organization");
        org.put("name", inputText.getText().toString());
        org.put("code", generateJoinCode());
        org.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Saved successfully.
                    ParseObject user_org = new ParseObject("UserOrg");
                    user_org.put("user_id", ParseUser.getCurrentUser());
                    user_org.put("org_id", org);
                    user_org.put("is_owner", true);
                    user_org.saveInBackground();

                    //Add this user to the org
                    mListener.onDialogMakeTeamSuccess(org);
                } else {
                    // The save failed.
                    mListener.onDialogMakeTeamFailure("Sorry, we couldn't create that team for you right now, try again later");
                }
            }
        });
    }

    public void join(DialogInterface dialog) {
        inputText = (EditText) ( (AlertDialog)dialog ).findViewById(R.id.joinCode);
        //check that this passcode exists
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Organization");
        query.whereEqualTo("code", Integer.valueOf(inputText.getText().toString()));
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects != null && objects.size() > 0) {
                    //Get user and organization
                    final ParseObject org = objects.get(0);
                    final ParseUser user = ParseUser.getCurrentUser();

                    //Check if user is already in this org
                    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("UserOrg");
                    query2.whereEqualTo("org_id", org);
                    query2.whereEqualTo("user_id", user);

                    query2.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (objects != null && objects.size() > 0) {
                                mListener.onDialogJoinTeamFailure("You've already joined that team");
                            } else {
                                //Add user to org
                                ParseObject user_org = new ParseObject("UserOrg");
                                user_org.put("user_id", user);
                                user_org.put("org_id", org);
                                user_org.put("false", true);
                                user_org.saveInBackground();
                                mListener.onDialogJoinTeamSuccess(org);
                            }
                        }
                    });
                } else {
                    mListener.onDialogJoinTeamFailure("Sorry, we couldn't add you to this team right now, try again later");
                }
            }
        });
    }

    private int generateJoinCode() {
        Random randomGenerator = new Random();
        boolean isUnique = false;
        int tries = 0;
        int code = 0;

        while(!isUnique) {
            code = randomGenerator.nextInt(MAX_JOIN_CODE - MIN_JOIN_CODE) + MIN_JOIN_CODE;

            ParseQuery<ParseObject> query = ParseQuery.getQuery("Organization");
            query.whereEqualTo("code", code);
            try {
                List<ParseObject> objects = query.find();
                if (objects == null || objects.size() == 0) {
                    isUnique = true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return code;
    }
}
