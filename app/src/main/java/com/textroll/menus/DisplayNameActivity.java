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
        if (name.matches("^[A-Za-z ]{3,15}$")) {
            Instances.mDatabase.child("users").child(Instances.user.getUid()).child("displayName").setValue(name);
            Instances.displayName = name;
            finish();
        } else {
            Toast.makeText(this, "Name must be letters and spaces, 3-15 letters long", Toast.LENGTH_SHORT).show();
        }
    }
}
