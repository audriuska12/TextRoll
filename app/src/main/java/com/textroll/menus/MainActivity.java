package com.textroll.menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.textroll.classes.Instances;
import com.textroll.textroll.R;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getBooleanExtra("Exit", false)) {
            finish();
        } else {
            mAuth = FirebaseAuth.getInstance();
            Instances.user = mAuth.getCurrentUser();
            setContentView(R.layout.activity_main);
            if (Instances.user == null) {
                goToLogin();
            } else {
                TextView lia = (findViewById(R.id.textViewLia));
                lia.setText(String.format("%s %s", getString(R.string.lblLia), Instances.user.getEmail()));
                lia.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int returnCode, Intent data) {
        if (requestCode == 1) {
            if (returnCode == RESULT_CANCELED) {
                goToLogin();
            } else {
                TextView lia = (findViewById(R.id.textViewLia));
                lia.setText(String.format("%s %s", getString(R.string.lblLia), Instances.user.getEmail()));
                lia.setVisibility(View.VISIBLE);
            }
        }
    }

    public void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }
    public void startNewGameP(View view){
        Intent intent = new Intent(this, ChargenActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        mAuth.signOut();
        goToLogin();
    }
}
