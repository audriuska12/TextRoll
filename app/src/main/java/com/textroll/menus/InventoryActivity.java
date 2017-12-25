package com.textroll.menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.textroll.classes.Instances;
import com.textroll.mechanics.Item;
import com.textroll.textroll.R;

import java.util.ArrayList;
import java.util.Arrays;

public class InventoryActivity extends AppCompatActivity {

    Item selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        setListeners();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateViews(false);
    }

    private void setListeners() {
        ListView backpack = findViewById(R.id.listViewBackpack);
        backpack.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = (Item) adapterView.getItemAtPosition(i);
                updateViews(false);
            }
        });
        ListView equipped = findViewById(R.id.listViewEquipped);
        equipped.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = (Item) adapterView.getItemAtPosition(i);
                updateViews(true);
            }
        });
    }

    private void updateViews(boolean equipped) {
        ListView backpack = findViewById(R.id.listViewBackpack);
        ItemArrayAdapter adapterBackpack = new ItemArrayAdapter(InventoryActivity.this, android.R.layout.simple_list_item_1, Instances.pc.getInventory());
        backpack.setAdapter(adapterBackpack);
        ListView equippedItems = findViewById(R.id.listViewEquipped);
        ItemArrayAdapter adapterEquipped = new ItemArrayAdapter(InventoryActivity.this, android.R.layout.simple_list_item_1, new ArrayList<>(Instances.pc.getEquippedItems().values()));
        equippedItems.setAdapter(adapterEquipped);
        if (selectedItem == null) {
            findViewById(R.id.constraintLayoutInventoryDesc).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) (findViewById(R.id.textViewInventoryDesc))).setText(selectedItem.getDescription());
            if (equipped) {
                ((ViewSwitcher) findViewById(R.id.viewSwitcherInventoryEq)).setDisplayedChild(1);
            } else {
                ((ViewSwitcher) findViewById(R.id.viewSwitcherInventoryEq)).setDisplayedChild(0);
            }
            findViewById(R.id.constraintLayoutInventoryDesc).setVisibility(View.VISIBLE);
        }
    }

    public void goToTown(View view) {
        Intent intent = new Intent(this, TownMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void equipClick(View view) {
        Instances.pc.equipItem(selectedItem);
        updateViews(true);
    }

    public void unequipClick(View view) {
        Instances.pc.unequipItem(selectedItem);
        updateViews(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToTown(null);
    }
}
