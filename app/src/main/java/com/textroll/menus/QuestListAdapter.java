package com.textroll.menus;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.textroll.mechanics.ActiveAbility;
import com.textroll.mechanics.QuestNode;

import java.util.List;

/**
 * Created by audri on 2017-12-17.
 */

public class QuestListAdapter extends ArrayAdapter<QuestNode> {
    public QuestListAdapter(@NonNull Context context, int resource, List<QuestNode> abilities) {
        super(context, resource, abilities);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(getItem(position).getName());
        return view;
    }
}
