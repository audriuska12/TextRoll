package com.textroll.menus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.firebase.database.DataSnapshot;
import com.textroll.classes.Instances;
import com.textroll.mechanics.Item;
import com.textroll.mechanics.Player;
import com.textroll.textroll.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {

    Item selectedItem;

    private static Item clone(Item item) {
        Item clone = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(item);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            clone = (Item) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateViews(false);
    }

    protected void setListeners() {
        ((ListView) findViewById(R.id.listViewShopBackpack)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = (Item) adapterView.getItemAtPosition(i);
                updateViews(false);
            }
        });
        ((ListView) findViewById(R.id.listViewShopSale)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = (Item) adapterView.getItemAtPosition(i);
                updateViews(true);
            }
        });
    }

    private void updateViews(boolean shopItem) {
        ArrayList<Item> shopItems = new ArrayList<>();
        for (DataSnapshot item : Instances.shopSnap.getChildren()) {
            try {
                shopItems.add(new Item(Instances.itemSnap.child((String) item.getValue())));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ItemArrayAdapter shopItemAdapter = new ItemArrayAdapter(this, android.R.layout.simple_list_item_1, shopItems);
        ((ListView) findViewById(R.id.listViewShopSale)).setAdapter(shopItemAdapter);
        ItemArrayAdapter backpackAdapter = new ItemArrayAdapter(this, android.R.layout.simple_list_item_1, Instances.pc.getInventory());
        ((ListView) findViewById(R.id.listViewShopBackpack)).setAdapter(backpackAdapter);
        if (selectedItem == null) {
            findViewById(R.id.constraintLayoutShopItem).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) (findViewById(R.id.textViewShopDesc))).setText(selectedItem.getDescription());
            if (shopItem) {
                ((ViewSwitcher) findViewById(R.id.viewSwitcherShopBS)).setDisplayedChild(1);
                ((TextView) findViewById(R.id.textViewShopPriceVal)).setText(String.valueOf(selectedItem.getPrice()));
            } else {
                ((ViewSwitcher) findViewById(R.id.viewSwitcherShopBS)).setDisplayedChild(0);
                ((TextView) findViewById(R.id.textViewShopPriceVal)).setText(String.valueOf(selectedItem.getPrice() / 3));
            }
            findViewById(R.id.constraintLayoutShopItem).setVisibility(View.VISIBLE);
        }
        ((TextView) findViewById(R.id.textViewShopGoldVal)).setText(String.valueOf(Instances.pc.getGold()));
    }

    public void buyItem(View view) {
        if (selectedItem != null) {
            if (Instances.pc.getGold() >= selectedItem.getPrice()) {
                Instances.pc.setGold(Instances.pc.getGold() - selectedItem.getPrice());
                Instances.pc.getInventory().add(clone(selectedItem));
                Toast.makeText(this, "Item purchased!", Toast.LENGTH_SHORT).show();
                updateViews(true);
            } else {
                Toast.makeText(this, "Not enough gold!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void sellItem(View view) {
        if (selectedItem != null) {
            Instances.pc.setGold(Instances.pc.getGold() + selectedItem.getPrice() / 3);
            Instances.pc.getInventory().remove(selectedItem);
            selectedItem = null;
            Toast.makeText(this, "Sold!", Toast.LENGTH_SHORT).show();
            updateViews(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goToTown(null);
    }

    public void goToTown(View view) {
        Intent intent = new Intent(this, TownMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
