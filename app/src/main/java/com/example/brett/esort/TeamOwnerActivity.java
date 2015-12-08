package com.example.brett.esort;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class TeamOwnerActivity extends AbstractDrawerActivity implements PopUpFragment.PopupDialogListener {

    private UserListAdapter mAdapter;
    Organization mOrg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_owner);
        InitDrawer();

        Intent orgIntent = getIntent();
        mOrg = (Organization) orgIntent.getSerializableExtra("org");
        setTitle(mOrg.getName());

        mAdapter = new UserListAdapter(this);

        ListView theListView = (ListView) findViewById(R.id.usersListView);
        theListView.setAdapter(mAdapter);

        theListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ParseObject userPO = (ParseObject)adapterView.getItemAtPosition(i);
                User user = new User(userPO);

                Intent profileIntent = new Intent(TeamOwnerActivity.this, ProfileActivity.class);
                profileIntent.putExtra("user", user);
                startActivity(profileIntent);
            }
        });

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("loading");
        pd.show();

        ParseQuery<ParseObject> innerQ = ParseQuery.getQuery("Organization");
        innerQ.whereEqualTo("objectId", mOrg.getId());

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserOrg");
        query.whereMatchesQuery("org_id", innerQ);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                pd.hide();
                if (objects != null && objects.size() > 0) {

                    for (ParseObject object : objects) {
                        ParseObject user = object.getParseObject("user_id");
                        try {
                            user.fetchIfNeeded();
                        } catch (ParseException e1) {
                            e1.printStackTrace();
                            Toast.makeText(TeamOwnerActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                        mAdapter.addItem(user);
                    }
                }
            }
        });
    }

    public void onSortClick(View view){
        //ArrayList members = mAdapter.getAllItems();

        PopUpFragment popUp = new PopUpFragment();
        Bundle typeBundle = new Bundle();

        typeBundle.putString("type", "sort");
        popUp.setArguments(typeBundle);
        popUp.show(getFragmentManager(), "");
    }

    @Override
    public void onDialogJoinTeamSuccess(ParseObject org) {

    }

    @Override
    public void onDialogJoinTeamFailure(String err) {

    }

    @Override
    public void onDialogMakeTeamSuccess(ParseObject org) {

    }

    @Override
    public void onDialogMakeTeamFailure(String err) {

    }

    @Override
    public void onDialogCancel() {

    }

    @Override
    public void onDialogSort(int numItems) {

        if(numItems <= 0 || numItems > mAdapter.getCount()) {
            Toast.makeText(TeamOwnerActivity.this, "Number of teams must be greater than zero and less than the number of members in the organization", Toast.LENGTH_LONG).show();
        }

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("loading");
        pd.show();

        ArrayList users = mAdapter.getAllItems();

        final ArrayList<ArrayList<User>> teams = new ArrayList<>();

        for(int i = 0; i < numItems; i++) {
            ArrayList<User> l = new ArrayList<>();
            teams.add(l);
        }

        for (int i = 0; i < mAdapter.getCount(); i++) {
            User u = new User((ParseUser)users.get(i));
            teams.get(i %  numItems).add(u);
        }

        final List<ParseObject> parseTeams = new ArrayList<>();
        final List<ParseObject> parseUsers = new ArrayList<>();
        ParseObject org = ParseObject.createWithoutData("Organization", mOrg.getId());

        int i = 1;
        for(ArrayList<User> team : teams) {
            ParseObject parseTeam = new ParseObject("Team");
            parseTeam.put("name", "Team " + i++);
            parseTeam.put("org", org);

            parseTeams.add(parseTeam);

            for(User user : team) {
                ParseObject parseUser = new ParseObject("UserTeam");
                parseUser.put("userId", ParseUser.createWithoutData(ParseUser.class, user.getId()));
                parseUser.put("teamId", parseTeam);
                parseUsers.add(parseUser);
            }
        }

        ParseObject.saveAllInBackground(parseTeams, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    //Save successful, save all users
                    ParseObject.saveAllInBackground(parseUsers, new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                //Save successful, go to sorted activity
                                pd.hide();
                                Intent sortIntent = new Intent(TeamOwnerActivity.this, TeamSortedActivity.class);
                                sortIntent.putExtra("org", mOrg);
                                sortIntent.putExtra("teams", teams);
                                startActivity(sortIntent);
                            } else {
                                //something went wrong
                                Toast.makeText(TeamOwnerActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                } else {
                    //something went wrong
                    Toast.makeText(TeamOwnerActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
