package com.pr.productkereview.adapters.topBrands;

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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.card.MaterialCardView;
import com.pr.productkereview.R;
import com.pr.productkereview.models.TopBrands.BrandsModel;
import com.pr.productkereview.utils.ApiWebServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopBrandsAdapter extends RecyclerView.Adapter<TopBrandsAdapter.ViewHolder> {
    private final boolean shouldShowAllItems;
    List<BrandsModel> topBrandsModelList = new ArrayList<>();
    Context context;
    TopBrandsClickInterface topBrandsClickInterface;

    public TopBrandsAdapter(Context context, TopBrandsClickInterface topBrandsClickInterface, boolean shouldShowAllItems) {
        this.context = context;
        this.topBrandsClickInterface = topBrandsClickInterface;
        this.shouldShowAllItems = shouldShowAllItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.top_brands_layout, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(1000) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.6f) //the alpha of the underlying children
                .setHighlightAlpha(0.8f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();

        // This is the placeholder for the imageView
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);

        BrandsModel topBrandsModel = topBrandsModelList.get(position);
        if (shouldShowAllItems) {
           // holder.imageCard.setLayoutParams(new ViewGroup.LayoutParams(210, 210));
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    235,
                    235
            );
            params.setMargins(10, 10, 10, 10);
            holder.imageCard.setLayoutParams(params);
        }
        Glide.with(context).load(ApiWebServices.base_url + "top_brands_images/" +
                topBrandsModel.getLogo())
                .placeholder(shimmerDrawable)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.itemImage);
        holder.itemTitle.setText(Html.fromHtml(topBrandsModel.getTitle(),Html.FROM_HTML_MODE_LEGACY));
        holder.itemView.setOnClickListener(v -> {
            topBrandsClickInterface.OnTopBrandClicked(topBrandsModelList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        if (shouldShowAllItems) {
            return topBrandsModelList.size();
        } else {
            int limit = 7;
            return Math.min(topBrandsModelList.size(), limit);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<BrandsModel> topBrandsModels) {
        topBrandsModelList.clear();
        topBrandsModelList.addAll(topBrandsModels);
        Collections.reverse(topBrandsModelList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTitle;
        MaterialCardView imageCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.item_img);
            itemTitle = itemView.findViewById(R.id.item_title);
            imageCard = itemView.findViewById(R.id.top_brand_card);
        }
    }
}
