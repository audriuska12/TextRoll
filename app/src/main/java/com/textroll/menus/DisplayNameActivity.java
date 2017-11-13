package com.textroll.menus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.textroll.classes.Instances;
import com.textroll.textroll.R;

public class DisplayNameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_name);
    }

    public void selectName(View view) {
        String name = ((TextView) findViewById(R.id.editTextCDN)).getText().toString();
        if (name.matches("^[A-Z][a-z]{3,15}$")) {
            Instances.mDatabase.child("users").child(Instances.user.getUid()).child("displayName").setValue(name);
            setResult(RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Name must start with a capital letter and be 4-16 letters long", Toast.LENGTH_SHORT).show();
        }
    }
}
