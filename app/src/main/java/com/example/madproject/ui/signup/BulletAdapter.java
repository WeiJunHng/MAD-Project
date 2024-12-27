package com.example.madproject.ui.signup;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madproject.R;

import java.util.List;

public class BulletAdapter extends RecyclerView.Adapter<BulletAdapter.BulletViewHolder> {

    private final List<String> bulletPoints;

    public BulletAdapter(List<String> bulletPoints) {
        this.bulletPoints = bulletPoints;
    }

    @NonNull
    @Override
    public BulletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bullet, parent, false);
        return new BulletViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BulletViewHolder holder, int position) {
        String text = bulletPoints.get(position);
        String wordToBold = text.split(":")[0]+":";

        SpannableString spannableString = new SpannableString(text);

        int start = text.indexOf(wordToBold);
        if (start >= 0) {
            int end = start + wordToBold.length();
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        holder.bulletDescription.setText(spannableString);
    }

    @Override
    public int getItemCount() {
        return bulletPoints.size();
    }

    public static class BulletViewHolder extends RecyclerView.ViewHolder {
        TextView bulletDescription;

        public BulletViewHolder(@NonNull View itemView) {
            super(itemView);
            bulletDescription = itemView.findViewById(R.id.bullet_description);
        }
    }
}
