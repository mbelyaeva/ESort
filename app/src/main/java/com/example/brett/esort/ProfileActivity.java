package com.example.brett.esort;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileActivity extends AbstractDrawerActivity {

    private User mUser;
    private TextView nameTextView;
    private TextView styleTextView;
    private TraitListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        InitDrawer();

        Intent userIntent = getIntent();
        mUser = (User)userIntent.getSerializableExtra("user");

        nameTextView = (TextView) findViewById(R.id.profileNameText);
        styleTextView = (TextView) findViewById(R.id.styleText);

        nameTextView.setText(mUser.getFullName());

        styleTextView.setText(mUser.getStyle());

        mAdapter = new TraitListAdapter(this);

        ListView theListView = (ListView) findViewById(R.id.traitsList);
        theListView.setAdapter(mAdapter);

        for(String s : mUser.getTraits()) {
            mAdapter.addItem(s);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            Intent editProfileIntent = new Intent(this, EditProfileActivity.class);
            editProfileIntent.putExtra("user", mUser);
            startActivity(editProfileIntent);
        }

        return super.onOptionsItemSelected(item);
    }
}
