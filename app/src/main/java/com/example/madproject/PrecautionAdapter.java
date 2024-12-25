package com.example.madproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PrecautionAdapter extends RecyclerView.Adapter<PrecautionAdapter.PrecautionViewHolder> {

    private List<PrecautionItem> precautionList;

    public PrecautionAdapter(List<PrecautionItem> precautionList) {
        this.precautionList = precautionList;
    }

    @NonNull
    @Override
    public PrecautionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_precaution_detail, parent, false);
        return new PrecautionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrecautionViewHolder holder, int position) {
        PrecautionItem precautionItem = precautionList.get(position);
        holder.title.setText(precautionItem.getTitle());
        holder.description.setText(precautionItem.getDescription());
        holder.icon.setImageResource(precautionItem.getImageResId());
    }

    @Override
    public int getItemCount() {
        return precautionList.size();
    }

    static class PrecautionViewHolder extends RecyclerView.ViewHolder {

        TextView title, description;
        ImageView icon;

        public PrecautionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tvTitle);
            description = itemView.findViewById(R.id.tvDescription);
            icon = itemView.findViewById(R.id.ivPrecautionImage);
        }
    }
}

