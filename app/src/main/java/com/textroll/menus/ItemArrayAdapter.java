package com.textroll.menus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.Item;

import java.util.List;

/**
 * Created by audri on 2017-12-23.
 */

class ItemArrayAdapter extends ArrayAdapter<Item> {
    public ItemArrayAdapter(@NonNull Context context, int resource, List<Item> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText((getItem(position)).getName());
        return view;
    }
}
