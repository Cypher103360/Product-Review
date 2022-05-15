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
import com.pr.productkereview.models.BuyingGuides.BuyingGuidesModel;
import com.pr.productkereview.models.TopBrands.TopBrandsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuyingGuidesAdapter extends RecyclerView.Adapter<BuyingGuidesAdapter.ViewHolder> {
    List<BuyingGuidesModel> buyingGuidesModelList = new ArrayList<>();
    Context context;
    BuyingGuidesClickInterface buyingGuidesClickInterface;

    public BuyingGuidesAdapter(Context context, BuyingGuidesClickInterface buyingGuidesClickInterface) {
        this.context = context;
        this.buyingGuidesClickInterface = buyingGuidesClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rectangular_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BuyingGuidesModel buyingGuidesModel = buyingGuidesModelList.get(position);
        Glide.with(context).load(buyingGuidesModel.getImage()).into(holder.itemImage);
        holder.itemTitle.setText(buyingGuidesModel.getTitle());
        holder.itemView.setOnClickListener(v -> {
            buyingGuidesClickInterface.OnBuyingGuidesClicked(buyingGuidesModelList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return buyingGuidesModelList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<BuyingGuidesModel> buyingGuidesModels){
        buyingGuidesModelList.clear();
        buyingGuidesModelList.addAll(buyingGuidesModels);
        Collections.reverse(buyingGuidesModelList);
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
