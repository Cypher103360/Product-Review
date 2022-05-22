package com.pr.productkereview.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pr.productkereview.R;
import com.pr.productkereview.models.AllProducts.ProductModel;
import com.pr.productkereview.utils.ApiWebServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LatestProductAdapter extends RecyclerView.Adapter<LatestProductAdapter.ViewHolder> {
    private final boolean shouldShowAllItems;
    List<ProductModel> latestProductModelList = new ArrayList<>();
    Context context;
    LatestProductClickInterface latestProductClickInterface;

    public LatestProductAdapter(Context context, LatestProductClickInterface latestProductClickInterface, boolean shouldShowAllItems) {
        this.context = context;
        this.latestProductClickInterface = latestProductClickInterface;
        this.shouldShowAllItems = shouldShowAllItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(holder.itemView.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        ProductModel latestProductModel = latestProductModelList.get(position);
        Glide.with(context).load(ApiWebServices.base_url + "all_products_images/"
                        + latestProductModel.getProductImage())
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.itemImage);
        holder.itemTitle.setText(Html.fromHtml(latestProductModel.getProductTitle(),Html.FROM_HTML_MODE_LEGACY));
        holder.itemView.setOnClickListener(v -> {
            latestProductClickInterface.OnLatestProductClicked(latestProductModelList.get(position));
        });
    }


    @Override
    public int getItemCount() {
        if (shouldShowAllItems) {
            return latestProductModelList.size();
        } else {
            int limit = 7;
            return Math.min(latestProductModelList.size(), limit);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<ProductModel> latestProductModels) {
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
