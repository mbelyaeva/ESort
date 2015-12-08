package com.example.brett.esort;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeamSortedActivity extends AbstractDrawerActivity {

    Organization mOrg;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<List<User>> mTeams;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_sorted);

        InitDrawer();

        Intent orgIntent = getIntent();
        mOrg = (Organization) orgIntent.getSerializableExtra("org");
        setTitle(mOrg.getName());

        mTeams = (List<List<User>>)orgIntent.getSerializableExtra("teams");

        expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        int i = 1;
        for(List<User> teamUsers : mTeams) {
            String teamName = "Team " + i++;
            listDataHeader.add(teamName);
            List<String> names = new ArrayList<>();
            for(User user : teamUsers) {
                names.add(user.getFullName());
            }
            listDataChild.put(teamName, names); // Header, Child data
        }
    }

    public void onContactPressed(View view) {

        final ArrayList<String> emails = new ArrayList<>();


        ParseQuery<ParseObject> innerQ = ParseQuery.getQuery("Team");
        innerQ.whereEqualTo("org", ParseObject.createWithoutData("Organization", mOrg.getId()));

        ParseQuery<ParseObject> query = ParseQuery.getQuery("UserTeam");
        query.whereEqualTo("userId", ParseUser.getCurrentUser());
        query.whereMatchesQuery("teamId", innerQ);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects != null && objects.size() > 0) {
                    ParseObject userTeam = objects.get(0);
                    ParseObject myTeam = userTeam.getParseObject("teamId");
                    try {
                        myTeam.fetchIfNeeded();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("UserTeam");
                    query.whereEqualTo("teamId", ParseObject.createWithoutData("Team", myTeam.getObjectId()));
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (objects != null && objects.size() > 0) {
                                for (ParseObject obj : objects) {
                                    ParseObject user = obj.getParseObject("userId");
                                    try {
                                        user.fetchIfNeeded();
                                    } catch (ParseException e1) {
                                        e1.printStackTrace();
                                        Toast.makeText(TeamSortedActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    }

                                    emails.add((String) user.get("username"));
                                }

                                String[] strArr = new String[emails.size()];
                                strArr = emails.toArray(strArr);

                                Intent i = new Intent(Intent.ACTION_SEND);
                                i.setType("message/rfc822");
                                i.putExtra(Intent.EXTRA_EMAIL, strArr );
                                try {
                                    startActivity(Intent.createChooser(i, "Send mail..."));
                                } catch (android.content.ActivityNotFoundException ex) {
                                    Toast.makeText(TeamSortedActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(TeamSortedActivity.this, "Something went wrong here.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }

}
