package com.example.madproject.ui.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.madproject.R;

// Custom Adapter
class SettingsAdapter extends BaseAdapter {
    private String[] items;

    SettingsAdapter(String[] items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.settings_list, parent, false);
        }

        // Bind data to views
        TextView textView = convertView.findViewById(R.id.item_text);
        ImageView icon = convertView.findViewById(R.id.item_icon);
        ImageView arrow = convertView.findViewById(R.id.item_arrow);

        // Set text
        textView.setText(items[position]);

        // Set icons dynamically (optional)
        int[] icons = {
                R.drawable.ic_lock,  // Replace with actual drawable
                R.drawable.ic_message,
                R.drawable.ic_bell,
                R.drawable.ic_accessibility,
                R.drawable.ic_sign_out,
                R.drawable.ic_trash
        };
        icon.setImageResource(icons[position]);
        arrow.setImageResource(R.drawable.ic_arrow_right); // Arrow for all rows

        return convertView;
    }
}

