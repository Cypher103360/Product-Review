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

import com.bumptech.glide.Glide;
import com.pr.productkereview.R;
import com.pr.productkereview.models.AllProducts.ProductModel;
import com.pr.productkereview.models.BestProducts.BestProductModel;
import com.pr.productkereview.utils.ApiWebServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BestProductAdapter extends RecyclerView.Adapter<BestProductAdapter.ViewHolder> {
    private final boolean shouldShowAllItems;
    List<ProductModel> bestProductModelList = new ArrayList<>();
    Context context;
    BestProductClickInterface bestProductClickInterface;


    public BestProductAdapter(Context context, BestProductClickInterface bestProductClickInterface, boolean shouldShowAllItems) {
        this.context = context;
        this.bestProductClickInterface = bestProductClickInterface;
        this.shouldShowAllItems = shouldShowAllItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.best_product_layout, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel bestProductModel = bestProductModelList.get(position);
        Glide.with(context).load(ApiWebServices.base_url + "all_products_images/"
                + bestProductModel.getProductImage()).into(holder.itemImage);
        holder.itemTitle.setText(Html.fromHtml(bestProductModel.getProductTitle(),Html.FROM_HTML_MODE_LEGACY));
        holder.itemView.setOnClickListener(v -> {
            bestProductClickInterface.OnBestProductClicked(bestProductModelList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        if (shouldShowAllItems) {
            return bestProductModelList.size();
        } else {
            int limit = 6;
            return Math.min(bestProductModelList.size(), limit);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<ProductModel> bestProductModels) {
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
