package com.textroll.menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
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
            Instances.mDatabase.keepSynced(true);
            mAuth = FirebaseAuth.getInstance();
            Instances.user = mAuth.getCurrentUser();
            setContentView(R.layout.activity_main);
            if (Instances.user == null) {
                goToLogin();
            } else {
                updateNameTag();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int returnCode, Intent data) {
        if (requestCode == 1) {
            if (returnCode == RESULT_CANCELED) {
                goToLogin();
            } else {
                updateNameTag();
            }
        }
        if (requestCode == 2) {
            if (returnCode == RESULT_CANCELED) {
                goToDisplayNameSelect();
            } else {
                updateNameTag();
            }
        }
    }

    public void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, 1);
    }

    public void goToDisplayNameSelect() {
        Intent intent = new Intent(this, DisplayNameActivity.class);
        startActivityForResult(intent, 2);
    }
    public void startNewGameP(View view){
        Intent intent = new Intent(this, ChargenActivity.class);
        startActivity(intent);
    }

    public void logout(View view) {
        mAuth.signOut();
        goToLogin();
    }

    private void updateNameTag() {
        final TextView lia = (findViewById(R.id.textViewLia));
        Instances.mDatabase.child("users").child(Instances.user.getUid()).child("displayName").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            goToDisplayNameSelect();
                        }
                        lia.setText(String.format("%s %s", getString(R.string.lblLia), dataSnapshot.getValue()));
                        lia.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }
}
