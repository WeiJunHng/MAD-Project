package com.example.madproject.ui.precaution;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;
import com.example.madproject.databinding.PrecautionListItemBinding;
import com.example.madproject.ui.ViewModelFactory;
import com.example.madproject.ui.news.NewsViewModel;

import java.util.List;

public class PrecautionAdapter extends RecyclerView.Adapter<PrecautionAdapter.PrecautionViewHolder> {

    private final FragmentActivity activity;
    private final Context context;
    private final List<PrecautionItem> precautionList;
    private PrecautionViewModel precautionViewModel;

    public PrecautionAdapter(FragmentActivity activity, Context context, List<PrecautionItem> precautionItems) {
        this.activity = activity;
        this.context = context;
        this.precautionList = precautionItems;
    }

    @NonNull
    @Override
    public PrecautionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PrecautionViewHolder(LayoutInflater.from(context).inflate(R.layout.precaution_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PrecautionViewHolder holder, int position) {
        ViewModelFactory factory = new ViewModelFactory(context);
        precautionViewModel = new ViewModelProvider(activity,factory).get(PrecautionViewModel.class);

        PrecautionItem precautionItem = precautionList.get(position);

        holder.TVTitle.setText(precautionItem.getTitle());
        holder.TVDescription.setText(precautionItem.getDescription());
        holder.IVPrecaution.setImageResource(precautionItem.getImageResId());

        holder.root.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if(adapterPosition != RecyclerView.NO_POSITION) {
                precautionViewModel.setPrecaution(precautionItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return precautionList.size();
    }

    public static class PrecautionViewHolder extends RecyclerView.ViewHolder {
        private CardView root;
        private TextView TVTitle, TVDescription;
        private ImageView IVPrecaution;
        private final PrecautionListItemBinding binding;

        public PrecautionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = PrecautionListItemBinding.bind(itemView);

            TVTitle = binding.TVTitle;
            TVDescription = binding.TVDescription;
            IVPrecaution = binding.IVPrecaution;

            root = binding.getRoot();
        }
    }
}

