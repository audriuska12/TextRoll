package com.textroll.menus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.textroll.mechanics.Ability;

import java.util.List;

public class AbilityArrayAdapter extends ArrayAdapter {
    AbilityArrayAdapter(@NonNull Context context, int resource, List<? extends Ability> abilities) {
        super(context, resource, abilities);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Ability ability = (Ability) getItem(position);
        TextView view = (TextView) super.getView(position, convertView, parent);
        if (ability != null) {
            view.setText(ability.getStatName());
        }
        return view;
    }
}
