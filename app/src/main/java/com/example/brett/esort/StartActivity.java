package com.example.brett.esort;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

    }

    public void OnLoginClick(View view) {
        Intent getLoginIntent = new Intent(this, LoginActivity.class);
        startActivity(getLoginIntent);
    }

    public void OnSignUpClick(View view) {
        Intent getSignupIntent = new Intent(this, SignupActivity.class);
        startActivity(getSignupIntent);
    }
}
