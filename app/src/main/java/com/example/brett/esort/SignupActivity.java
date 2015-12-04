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

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

public class SignupActivity extends AbstractActivity {

    EditText emailField;
    EditText passField;
    EditText confirmPassField;
    Button submitButton;

    private long mLastClickTime = 0;

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
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                doSignup();
            }
        });

    }

    private void doSignup() {

        final String username = emailField.getText().toString().trim();
        final String password = passField.getText().toString().trim();
        final String passwordAgain = confirmPassField.getText().toString().trim();

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

        final ProgressDialog pd = new ProgressDialog(SignupActivity.this);
        pd.setMessage("loading");
        pd.show();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", username);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            public void done(ParseUser object, ParseException e) {
                pd.hide();
                if (e == null) {
                    Toast.makeText(SignupActivity.this, "Email already in use", Toast.LENGTH_LONG)
                            .show();
                } else {
                    if (e.getCode() == ParseException.OBJECT_NOT_FOUND) {
                        Intent detailsIntent = new Intent(SignupActivity.this, SignupDetailsActivity.class);
                        detailsIntent.putExtra("email", username);
                        detailsIntent.putExtra("password", password);
                        startActivity(detailsIntent);
                    } else {
                        Toast.makeText(SignupActivity.this, "An unknown error has occurred", Toast.LENGTH_LONG)
                                .show();
                    }
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
