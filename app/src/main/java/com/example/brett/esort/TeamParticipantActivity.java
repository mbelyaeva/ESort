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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class TeamParticipantActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private UserListAdapter mAdapter;
    Organization mOrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_participant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //
        Intent orgIntent = getIntent();
        mOrg = (Organization) orgIntent.getSerializableExtra("org");
        setTitle(mOrg.getName());

        mAdapter = new UserListAdapter(this);

        // ListViews display data in a scrollable list
        ListView theListView = (ListView) findViewById(R.id.usersListView);

        // Tells the ListView what data to use
        theListView.setAdapter(mAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ParseObject userPO = (ParseObject)adapterView.getItemAtPosition(i);
                User user = new User(userPO);

                Intent profileIntent = new Intent(TeamParticipantActivity.this, ProfileActivity.class);
                profileIntent.putExtra("user", user);
                startActivity(profileIntent);
            }
        });

        ParseQuery<ParseObject> innerQ = ParseQuery.getQuery("Organization");
        innerQ.whereEqualTo("objectId", mOrg.getId());

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserOrg");
        query.whereMatchesQuery("org_id", innerQ);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects != null && objects.size() > 0) {

                    for (ParseObject object : objects) {
                        ParseObject user = object.getParseObject("user_id");
                        try {
                            user.fetchIfNeeded();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                            Toast.makeText(TeamParticipantActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                        mAdapter.addItem(user);
                    }
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            doLogout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void doLogout() {
        ParseUser.logOut();
        Intent intent = new Intent(TeamParticipantActivity.this, DispatchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
