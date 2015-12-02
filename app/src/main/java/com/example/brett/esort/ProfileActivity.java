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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

public class ProfileActivity extends AbstractDrawerActivity {

    private User mUser;
    private TextView nameTextView;
    private TextView styleTextView;

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
        styleTextView.setText("Relational");

    }
}
