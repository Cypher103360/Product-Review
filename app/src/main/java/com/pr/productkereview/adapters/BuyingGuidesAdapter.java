package com.pr.productkereview.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pr.productkereview.R;
import com.pr.productkereview.db.entity.Products;
import com.pr.productkereview.utils.ApiWebServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuyingGuidesAdapter extends RecyclerView.Adapter<BuyingGuidesAdapter.ViewHolder> {
    List<Products> buyingGuidesModelList = new ArrayList<>();
    Context context;
    BuyingGuidesClickInterface buyingGuidesClickInterface;

    public BuyingGuidesAdapter(Context context, BuyingGuidesClickInterface buyingGuidesClickInterface) {
        this.context = context;
        this.buyingGuidesClickInterface = buyingGuidesClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rectangular_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Products buyingGuidesModel = buyingGuidesModelList.get(position);
        Glide.with(context).load(ApiWebServices.base_url + "all_products_images/" + buyingGuidesModel.getProductImage()).into(holder.itemImage);
        holder.itemTitle.setText(HtmlCompat.fromHtml(buyingGuidesModel.getProductTitle(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        holder.itemView.setOnClickListener(v -> {
            buyingGuidesClickInterface.OnBuyingGuidesClicked(buyingGuidesModelList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return buyingGuidesModelList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Products> buyingGuidesModels) {
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
