package com.example.brett.esort;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

/**
 * Created by Maria on 11/19/2015.
 */
public class popUp extends DialogFragment {

    private String popUp;

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        if (popUp.equals("make")){

        }else if (popUp.equals("join")){

        }
        return null;
    }
}
