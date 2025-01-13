package com.example.madproject.ui.discussion;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.madproject.R;
import com.example.madproject.databinding.ReportReasonListItemBinding;

// Custom Adapter
class ReportItemAdapter extends BaseAdapter {
    private String[] items;
    private ReportReasonListItemBinding binding;

    ReportItemAdapter(String[] items) {
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
        binding = ReportReasonListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        if (convertView == null) {
            convertView = binding.getRoot();
        }

        // Bind data to views
        TextView TVReason = binding.TVReason;

        // Set text
        TVReason.setText(items[position]);

        return convertView;
    }
}

