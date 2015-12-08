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
import com.terlici.dragndroplist.DragNDropAdapter;
import com.terlici.dragndroplist.DragNDropListView;
import com.terlici.dragndroplist.DragNDropSimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class EditProfileActivity extends AbstractDrawerActivity {

    User mUser;
    TextView nameTextView;
    HashMap<String, String> map;
    ArrayList<HashMap<String, String>> aList;
    DragNDropAdapter mAdapter;

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

        aList = new ArrayList<>();

        for(String n : mUser.getTraits()) {
            map = new HashMap<>();
            map.put("trait_name", n);
            aList.add(map);
        }

        DragNDropListView list = (DragNDropListView)findViewById(R.id.traitsList);
        mAdapter = new DragNDropSimpleAdapter(this, aList, R.layout.dragable_row, new String[] {"trait_name"}, new int[] {R.id.text}, R.id.handler);

        list.setDragNDropAdapter(mAdapter);
    }

    public void onSaveChangesPressed(View view)
    {
        Spinner mySpinner=(Spinner) findViewById(R.id.editStyle);
        String style = mySpinner.getSelectedItem().toString();

        ArrayList<String> traits = new ArrayList<>();
        for(int i = 0; i < mAdapter.getCount(); i++) {
            traits.add((String)((HashMap)mAdapter.getItem(i)).get("trait_name"));
        }


        ParseUser user = ParseUser.getCurrentUser();
        user.put("style", style);
        user.put("traits", traits);
        user.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(EditProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Changes saved successfully", Toast.LENGTH_LONG).show();
                    Intent profileIntent = new Intent();
                    profileIntent.putExtra("updated_user", new User(ParseUser.getCurrentUser()));
                    setResult(RESULT_OK, profileIntent);
                    finish();
                }
            }
        });
    }

    public void onCancelPressed(View view)
    {
        Intent profileIntent = new Intent();
        setResult(RESULT_CANCELED, profileIntent);
        finish();
    }
}
