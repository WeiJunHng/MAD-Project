package com.example.madproject.ui.news;

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
import com.example.madproject.databinding.HeadListItemBinding;
import com.example.madproject.ui.ViewModelFactory;
import com.example.madproject.ui.news.Models.NewsHeadlines;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsItemAdapter extends RecyclerView.Adapter<NewsItemAdapter.CustomViewHolder> {
    private final FragmentActivity activity;
    private final Context context;
    private final List<NewsHeadlines> headlines;
    private NewsViewModel newsViewModel;

    public NewsItemAdapter(FragmentActivity activity, Context context, List<NewsHeadlines> headlines){
        this.activity = activity;
        this.context = context;
        this.headlines = headlines;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.head_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        ViewModelFactory factory = new ViewModelFactory(context);
        newsViewModel = new ViewModelProvider(activity,factory).get(NewsViewModel.class);
        
        NewsHeadlines headline = headlines.get(position);

        holder.ETTitle.setText(headline.getTitle());
        holder.ETSource.setText(headline.getSource().getName());

        if(headline.getUrlToImage()!=null) {
            Picasso.get().load(headline.getUrlToImage()).into(holder.IVHeadline);
        }

        holder.root.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if(adapterPosition != RecyclerView.NO_POSITION) {
                newsViewModel.setHeadline(headline);
            }
        });
    }

    @Override
    public int getItemCount() {
        return headlines.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        final TextView ETTitle, ETSource;
        final ImageView IVHeadline;
        final CardView root;
        private final HeadListItemBinding binding;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = HeadListItemBinding.bind(itemView);

            ETTitle = binding.ETTitle;
            ETSource = binding.ETSource;
            IVHeadline = binding.IVHeadline;

            root = binding.getRoot();
        }
    }
}
