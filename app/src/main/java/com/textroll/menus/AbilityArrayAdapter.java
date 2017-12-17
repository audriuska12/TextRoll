package com.textroll.menus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.textroll.mechanics.Ability;
import com.textroll.mechanics.ActiveAbility;

import java.util.List;

/**
 * Created by audri on 2017-12-17.
 */
public class AbilityArrayAdapter<T extends Ability> extends ArrayAdapter {
    public AbilityArrayAdapter(@NonNull Context context, int resource, List<T> abilities) {
        super(context, resource, abilities);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(((ActiveAbility) getItem(position)).getStatName());
        return view;
    }
}
