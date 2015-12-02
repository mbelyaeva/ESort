package com.example.brett.esort;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupActivity extends AppCompatActivity {

    EditText emailField;
    EditText passField;
    EditText confirmPassField;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailField = (EditText)findViewById(R.id.emailField);
        passField = (EditText)findViewById(R.id.passField);
        confirmPassField = (EditText)findViewById(R.id.confirmPassField);
        submitButton = (Button)findViewById(R.id.loginButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                doSignup();
            }
        });

    }

    private void doSignup() {

        String username = emailField.getText().toString().trim();
        String password = passField.getText().toString().trim();
        String passwordAgain = confirmPassField.getText().toString().trim();

        boolean validationError = false;
        StringBuilder validationErrorMessage = new StringBuilder();

        if (username.length() == 0) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_username));
        }

        if (!isValidEmail(username)) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_invalid_email));
        }

        if (password.length() == 0) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_password));
        }

        if (passwordAgain.length() == 0) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_blank_confirm));
        }

        if (!password.equals(passwordAgain)) {
            validationError = true;
            validationErrorMessage.append(getString(R.string.error_mismatched_passwords));
        }

        if (validationError) {
            Toast.makeText(SignupActivity.this, validationErrorMessage.toString(), Toast.LENGTH_LONG)
                    .show();
            return;
        }

        // Set up and start a progress dialog
        final ProgressDialog pd = new ProgressDialog(SignupActivity.this);
        pd.setMessage("loading");
        pd.show();

        // Set up a new Parse user
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        // Call the Parse signup method
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {

                // Dismiss the dialog
                pd.hide();

                if (e != null) {
                    // Show the error message
                    Toast.makeText(SignupActivity.this, e.getMessage(),
                            Toast.LENGTH_LONG).show();
                } else {
                    // Start an intent for the dispatch activity
                    Intent intent = new Intent(SignupActivity.this, DispatchActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
