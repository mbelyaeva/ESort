package com.example.brett.esort;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginActivity extends AbstractActivity {


    TextView emailField;
    TextView passField;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = (TextView) findViewById(R.id.emailField);
        passField  = (TextView) findViewById(R.id.passField);

        submitButton = (Button)findViewById(R.id.loginButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                doLogin();
            }
        });
    }

    public void OnLoginClick(View view) {
        //do parse login stuff
        String email = emailField.getText().toString();
        String pass = passField.getText().toString();

        //Go To My Orgs Page
        Intent getMainIntent = new Intent(this, MyOrgsActivity.class);
        startActivity(getMainIntent);
    }

    private void doLogin() {
        String username = emailField.getText().toString().trim();
        String password = passField.getText().toString().trim();

        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder();
        if (username.length() == 0) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_username));
        }
        if (password.length() == 0) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_password));
        }

        if (validationError) {
            Toast.makeText(LoginActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up and start a progress dialog
        final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
        pd.setMessage("loading");
        pd.show();

        // Call the Parse Login method
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {

                // Dismiss the dialog
                pd.hide();

                if (e != null) {
                    // Show the error message
                    Toast.makeText(LoginActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                } else {
                    // Start an intent for the dispatch activity
                    Intent intent = new Intent(LoginActivity.this, DispatchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }
}
