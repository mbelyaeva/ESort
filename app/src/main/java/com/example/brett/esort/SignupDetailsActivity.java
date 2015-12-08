package com.example.brett.esort;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupDetailsActivity extends AbstractActivity {

    private String mUsername;
    private String mPassword;

    private EditText firstNameText;
    private EditText lastNameText;

    private long mLastClickTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_details);

        firstNameText = (EditText) findViewById(R.id.firstNameField);
        lastNameText = (EditText) findViewById(R.id.lastNameField);
        Button submitButton = (Button) findViewById(R.id.submitButton);

        Intent i = getIntent();
        mUsername = i.getStringExtra("email");
        mPassword = i.getStringExtra("password");

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                finishSignup();
            }
        });
    }

    private void finishSignup()
    {

        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();

        // Set up and start a progress dialog
        final ProgressDialog pd = new ProgressDialog(SignupDetailsActivity.this);
        pd.setMessage("loading");
        pd.show();

        // Set up a new Parse user
        ParseUser user = new ParseUser();
        user.setUsername(mUsername);
        user.setPassword(mPassword);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("setupProfile", false);

        // Call the Parse signup method
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

                // Dismiss the dialog
                pd.hide();

                if (e != null) {
                    // Show the error message
                    Toast.makeText(SignupDetailsActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                } else {
                    // Start an intent for the dispatch activity
                    Intent intent = new Intent(SignupDetailsActivity.this, DispatchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });
    }

}
