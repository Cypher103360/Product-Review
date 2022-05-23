package com.pr.productkereview.adapters.Products;

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

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private final boolean shouldShowAllItems;
    List<ProductModel> productModelList = new ArrayList<>();
    Context context;
    ProductsClickInterface productsClickInterface;

    public ProductsAdapter(Context context, ProductsClickInterface productsClickInterface, boolean shouldShowAllItems) {
        this.context = context;
        this.productsClickInterface = productsClickInterface;
        this.shouldShowAllItems = shouldShowAllItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sub_cat_layout, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(holder.itemView.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        ProductModel productModel = productModelList.get(position);
        Glide.with(context).load(ApiWebServices.base_url + "all_products_images/" + productModel.getProductImage())
                .placeholder(circularProgressDrawable)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder.itemImage);
        holder.itemTitle.setText(Html.fromHtml(productModel.getProductTitle(),Html.FROM_HTML_MODE_LEGACY));
        holder.itemView.setOnClickListener(v -> {
            productsClickInterface.OnProductClicked(productModelList.get(position));
        });
    }


    @Override
    public int getItemCount() {
        if (shouldShowAllItems) {
            return productModelList.size();
        } else {
            int limit = 7;
            return Math.min(productModelList.size(), limit);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<ProductModel> productModels) {
        productModelList.clear();
        productModelList.addAll(productModels);
        Collections.reverse(productModelList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.cat_image);
            itemTitle = itemView.findViewById(R.id.cat_title);
        }
    }
}
