package com.pr.productkereview.adapters.trendingProducts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.pr.productkereview.R;
import com.pr.productkereview.adapters.categories.CategoryInterface;
import com.pr.productkereview.models.AllProducts.ProductModel;
import com.pr.productkereview.models.categories.CatModel;
import com.pr.productkereview.utils.ApiWebServices;
import com.pr.productkereview.utils.CommonMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrendingProductAdapter extends RecyclerView.Adapter<TrendingProductAdapter.ViewHolder> {

    List<ProductModel> trendingProductModelList = new ArrayList<>();
    Activity context;
    TrendingProductInterface trendingProductInterface;

    public TrendingProductAdapter(Activity context, TrendingProductInterface trendingProductInterface) {
        this.context = context;
        this.trendingProductInterface = trendingProductInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rectangular_layout, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /*
        Glide 3.x & 4.x: DiskCacheStrategy.NONE caches nothing, as discussed
        Glide 4.x: DiskCacheStrategy.DATA, Glide 3.x: DiskCacheStrategy.SOURCE caches only the original
                    full-resolution image. In our example above that would be the 1000x1000 pixel one
        Glide 4.x: DiskCacheStrategy.RESOURCE Glide 3.x: DiskCacheStrategy.RESULT caches only the final image,
                    after reducing the resolution (and possibly transformations) (default behavior of Glide 3.x)
        Glide 4.x only: DiskCacheStrategy.AUTOMATIC intelligently chooses a cache strategy based on the
                    resource (default behavior of Glide 4.x)
        Glide 3.x & 4.x: DiskCacheStrategy.ALL caches all versions of the image

        */

        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                250
        );
        params.setMargins(10, 10, 10, 10);
        holder.rectangleLayoutCard.setLayoutParams(params);

        holder.itemTitle.setText(Html.fromHtml(trendingProductModelList.get(position).getProductTitle(),Html.FROM_HTML_MODE_LEGACY));
        Glide.with(context).load(ApiWebServices.base_url + "all_products_images/"+
                        trendingProductModelList.get(position).getProductImage())
                .placeholder(CommonMethods.setShimmer(holder.itemView.getContext()))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.itemImg);
        holder.itemView.setOnClickListener(view ->
                trendingProductInterface.OnTrendingProductClicked(trendingProductModelList.get(position)));
    }

    @Override
    public int getItemCount() {
        return trendingProductModelList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateTrendingList(List<ProductModel> productModels) {
        trendingProductModelList.clear();
        trendingProductModelList.addAll(productModels);
        Collections.reverse(trendingProductModelList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImg;
        TextView itemTitle;
        MaterialCardView rectangleLayoutCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.item_img);
            itemTitle = itemView.findViewById(R.id.item_title);
            rectangleLayoutCard = itemView.findViewById(R.id.rectangle_layout_card);
        }
    }
}
