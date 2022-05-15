package com.pr.productkereview.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pr.productkereview.R;
import com.pr.productkereview.models.LatestProduct.LatestProductModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LatestProductAdapter extends RecyclerView.Adapter<LatestProductAdapter.ViewHolder> {
    List<LatestProductModel> latestProductModelList = new ArrayList<>();
    Context context;
    LatestProductClickInterface latestProductClickInterface;

    public LatestProductAdapter(Context context, LatestProductClickInterface latestProductClickInterface) {
        this.context = context;
        this.latestProductClickInterface = latestProductClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LatestProductModel latestProductModel = latestProductModelList.get(position);
        Glide.with(context).load(latestProductModel.getImage()).into(holder.itemImage);
        holder.itemTitle.setText(latestProductModel.getTitle());
        holder.itemView.setOnClickListener(v -> {
            latestProductClickInterface.OnLatestProductClicked(latestProductModelList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return latestProductModelList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<LatestProductModel> latestProductModels){
        latestProductModelList.clear();
        latestProductModelList.addAll(latestProductModels);
        Collections.reverse(latestProductModelList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTitle;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_img);
            itemTitle = itemView.findViewById(R.id.item_title);
        }
    }
}
