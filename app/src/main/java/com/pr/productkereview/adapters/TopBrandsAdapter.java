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
import com.pr.productkereview.models.TopBrands.TopBrandsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopBrandsAdapter extends RecyclerView.Adapter<TopBrandsAdapter.ViewHolder> {
    List<TopBrandsModel> topBrandsModelList = new ArrayList<>();
    Context context;
    TopBrandsClickInterface topBrandsClickInterface;

    public TopBrandsAdapter(Context context, TopBrandsClickInterface topBrandsClickInterface) {
        this.context = context;
        this.topBrandsClickInterface = topBrandsClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.top_brands_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TopBrandsModel topBrandsModel = topBrandsModelList.get(position);
        Glide.with(context).load(topBrandsModel.getImage()).into(holder.itemImage);
        holder.itemTitle.setText(topBrandsModel.getTitle());
        holder.itemView.setOnClickListener(v -> {
            topBrandsClickInterface.OnTopBrandClicked(topBrandsModelList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return topBrandsModelList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<TopBrandsModel> topBrandsModels){
        topBrandsModelList.clear();
        topBrandsModelList.addAll(topBrandsModels);
        Collections.reverse(topBrandsModelList);
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
