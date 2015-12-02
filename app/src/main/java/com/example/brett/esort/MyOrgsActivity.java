package com.example.brett.esort;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MyOrgsActivity extends AbstractDrawerActivity
        implements NavigationView.OnNavigationItemSelectedListener, PopUpFragment.PopupDialogListener{

    private OrgsListAdapter mAdapter;
    private long mLastClickTime = 0;

    public String popUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orgs);
        InitDrawer();

        mAdapter = new OrgsListAdapter(this);
        ListView theListView = (ListView) findViewById(R.id.myOrgsList);
        theListView.setAdapter(mAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();

                ParseObject orgPO = (ParseObject)adapterView.getItemAtPosition(i);
                final Organization org = new Organization(orgPO);
                ParseQuery<ParseObject> query = ParseQuery.getQuery("UserOrg");
                query.whereEqualTo("org_id", orgPO);
                query.whereEqualTo("user_id", ParseUser.getCurrentUser());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (objects != null && objects.size() > 0) {
                            ParseObject obj = objects.get(0);

                            if (obj.getBoolean("is_owner")) {
                                Intent classDetailsIntent = new Intent(MyOrgsActivity.this, TeamOwnerActivity.class);
                                classDetailsIntent.putExtra("org", org);
                                startActivity(classDetailsIntent);
                            } else {
                                Intent classDetailsIntent = new Intent(MyOrgsActivity.this, TeamParticipantActivity.class);
                                classDetailsIntent.putExtra("org", org);
                                startActivity(classDetailsIntent);
                            }
                        }
                    }
                });


            }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserOrg");
        query.whereEqualTo("user_id", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects != null && objects.size() > 0) {

                    for (ParseObject object : objects) {
                        ParseObject org = object.getParseObject("org_id");
                        try {
                            org.fetchIfNeeded();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                            Toast.makeText(MyOrgsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                        mAdapter.addItem(org);
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_orgs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        PopUpFragment popUp = new PopUpFragment();
        Bundle typeBundle = new Bundle();


        if (id == R.id.action_Join) {
            typeBundle.putString("type", "join");
            popUp.setArguments(typeBundle);
            popUp.show(getFragmentManager(), "");

        } else if (id == R.id.action_Make){
            typeBundle.putString("type", "make");
            popUp.setArguments(typeBundle);
            popUp.show(getFragmentManager(), "");
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDialogJoinTeamSuccess(ParseObject org) {
        mAdapter.addItem(org);
    }

    @Override
    public void onDialogJoinTeamFailure(String err) {
        Toast.makeText(MyOrgsActivity.this, err, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDialogMakeTeamSuccess(ParseObject org) {
        mAdapter.addItem(org);
    }

    @Override
    public void onDialogMakeTeamFailure(String err) {
        Toast.makeText(MyOrgsActivity.this, err, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDialogCancel() {
        //Don't need to do anything.
    }
}
