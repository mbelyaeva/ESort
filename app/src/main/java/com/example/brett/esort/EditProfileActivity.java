package com.example.brett.esort;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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
}
