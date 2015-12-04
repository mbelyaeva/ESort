package com.example.brett.esort;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EditProfileActivity extends AbstractDrawerActivity {

    User mUser;
    TextView nameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        InitDrawer();

        Intent i = getIntent();
        mUser = (User)i.getSerializableExtra("user");

        nameTextView = (TextView) findViewById(R.id.profileNameText);
        nameTextView.setText(mUser.getFullName());

        Spinner spinner = (Spinner) findViewById(R.id.editStyle);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.styles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(adapter.getPosition(mUser.getStyle()));
    }

    public void onSaveChangesPressed(View view)
    {
        Spinner mySpinner=(Spinner) findViewById(R.id.editStyle);
        String style = mySpinner.getSelectedItem().toString();

        ParseUser user = ParseUser.getCurrentUser();
        user.put("style", style);
        user.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Changes saved successfully", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });


    }

    public void onCancelPressed(View view)
    {
        finish();
    }
}
