package com.textroll.menus;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.textroll.classes.Instances;
import com.textroll.textroll.R;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        String email = ((TextView) findViewById(R.id.boxEmail)).getText().toString();
        String password = ((TextView) findViewById(R.id.boxPassword)).getText().toString();
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Must enter username and password", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Instances.user = mAuth.getCurrentUser();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                FirebaseException e = (FirebaseException) task.getException();
                                Toast.makeText(LoginActivity.this, "Failed Login", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_CANCELED);
                            }
                        }
                    });
        }
    }

    public void register(View view) {
        String email = ((TextView) findViewById(R.id.boxEmail)).getText().toString();
        String password = ((TextView) findViewById(R.id.boxPassword)).getText().toString();
        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Must enter username and password", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Instances.user = mAuth.getCurrentUser();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            } else {
                                FirebaseException e = (FirebaseException) task.getException();
                                Toast.makeText(LoginActivity.this, "Failed Registration: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                setResult(RESULT_CANCELED);
                            }
                        }
                    });
        }
    }

    public void quit(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Exit", true);
        startActivity(intent);
        finish();
    }
}
