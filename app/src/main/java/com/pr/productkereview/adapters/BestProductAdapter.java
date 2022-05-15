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
import com.pr.productkereview.models.BestProducts.BestProductModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BestProductAdapter extends RecyclerView.Adapter<BestProductAdapter.ViewHolder> {
    private final int limit = 6;
    List<BestProductModel> bestProductModelList = new ArrayList<>();
    Context context;
    BestProductClickInterface bestProductClickInterface;


    public BestProductAdapter(Context context, BestProductClickInterface bestProductClickInterface) {
        this.context = context;
        this.bestProductClickInterface = bestProductClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.best_product_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BestProductModel bestProductModel = bestProductModelList.get(position);
        Glide.with(context).load(bestProductModel.getImage()).into(holder.itemImage);
        holder.itemTitle.setText(bestProductModel.getTitle());
        holder.itemView.setOnClickListener(v -> {
            bestProductClickInterface.OnBestProductClicked(bestProductModelList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(bestProductModelList.size(), limit);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<BestProductModel> bestProductModels) {
        bestProductModelList.clear();
        bestProductModelList.addAll(bestProductModels);
        Collections.reverse(bestProductModelList);
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
