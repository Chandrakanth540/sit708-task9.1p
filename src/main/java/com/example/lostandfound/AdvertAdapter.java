package com.example.lostandfound;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdvertAdapter extends RecyclerView.Adapter<AdvertAdapter.ViewHolder> {
    private List<AdvertItem> advertItems;

    public AdvertAdapter(List<AdvertItem> advertItems) {
        this.advertItems = advertItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_advert, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdvertItem item = advertItems.get(position);
        holder.postTextView.setText(item.getPostType() + " - " + item.getDescription());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), Detailed.class);
            intent.putExtra("id",item.getId());
            intent.putExtra("location", item.getLocation());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("postType", item.getPostType());
            intent.putExtra("phone", item.getPhone());
            intent.putExtra("date", item.getDate());
            intent.putExtra("name", item.getName());
            // You can pass more data if needed
            holder.itemView.getContext().startActivity(intent);
        });
    }







    @Override
    public int getItemCount() {
        return advertItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, phoneTextView, descriptionTextView, dateTextView, locationTextView,postTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            postTextView=itemView.findViewById(R.id.postTextView);


        }
    }

}

