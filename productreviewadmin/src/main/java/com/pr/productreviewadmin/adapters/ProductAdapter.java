package com.pr.productreviewadmin.adapters;

import static com.pr.productreviewadmin.activities.ShowProducts.key;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pr.productreviewadmin.R;
import com.pr.productreviewadmin.models.ApiWebServices;
import com.pr.productreviewadmin.models.ProductModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    List<ProductModel> productModels = new ArrayList<>();
    Activity context;
    ProductInterface productInterface;

    public ProductAdapter(Activity context, ProductInterface productInterface) {
        this.context = context;
        this.productInterface = productInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.title.setText(HtmlCompat.fromHtml(productModels.get(position).getProductTitle(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        if (Objects.equals(key, "latest")) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(Boolean.parseBoolean(productModels.get(position).getLatestProduct()));

        } else if (Objects.equals(key, "best")) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(Boolean.parseBoolean(productModels.get(position).getBestProduct()));

        } else if (Objects.equals(key, "trending")) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.checkBox.setChecked(Boolean.parseBoolean(productModels.get(position).getTrendingProduct()));
        }
        Glide.with(context).load(ApiWebServices.base_url + "all_products_images/" + productModels.get(position).getProductImage()).into(holder.img);
        holder.itemView.setOnClickListener(view -> productInterface.productClicked(productModels.get(position)));

    }

    public void updateCategoryList(List<ProductModel> productModelList) {
        productModels.clear();
        productModels.addAll(productModelList);
        Collections.reverse(productModels);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.item_img);
            title = itemView.findViewById(R.id.item_title);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
