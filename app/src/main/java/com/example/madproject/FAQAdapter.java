package com.example.madproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class FAQAdapter extends RecyclerView.Adapter<FAQAdapter.FAQViewHolder> {

    private List<FAQ> faqList;

    public FAQAdapter(List<FAQ> faqList) {
        this.faqList = faqList;
    }

    @NonNull
    @Override
    public FAQViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_list, parent, false);
        return new FAQViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAQViewHolder holder, int position) {
        FAQ faq = faqList.get(position);
        holder.question.setText(faq.getQuestion());
        holder.answer.setText(faq.getAnswer());

        holder.answer.setVisibility(faq.isExpanded() ? View.VISIBLE : View.GONE);

        int iconRes = faq.isExpanded() ? R.drawable.ic_arrow_right : R.drawable.ic_arrow_down;
        holder.expandIcon.setImageResource(iconRes);

        holder.itemView.setOnClickListener(v -> {
            faq.setExpanded(!faq.isExpanded());
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }

    public static class FAQViewHolder extends RecyclerView.ViewHolder {
        TextView question, answer;
        ImageView expandIcon;

        public FAQViewHolder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);
            expandIcon = itemView.findViewById(R.id.expand_icon);
        }
    }

}

