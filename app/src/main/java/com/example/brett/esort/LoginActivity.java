package com.example.brett.esort;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {


    TextView emailField;
    TextView passField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = (TextView) findViewById(R.id.emailField);
        passField  = (TextView) findViewById(R.id.passField);
    }

    public void OnLoginClick(View view) {
        //do parse login stuff
        String email = emailField.getText().toString();
        String pass = passField.getText().toString();

        //Go To My Orgs Page
        Intent getMainIntent = new Intent(this, MyOrgsActivity.class);
        startActivity(getMainIntent);
    }
}
