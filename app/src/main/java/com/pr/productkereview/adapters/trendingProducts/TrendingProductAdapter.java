package com.pr.productkereview.adapters.trendingProducts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.pr.productkereview.R;
import com.pr.productkereview.databinding.AdLayoutBinding;
import com.pr.productkereview.models.AllProducts.ProductModel;
import com.pr.productkereview.utils.ApiWebServices;
import com.pr.productkereview.utils.CommonMethods;
import com.pr.productkereview.utils.Prevalent;
import com.pr.productkereview.utils.ShowAds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;

public class TrendingProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW = 0;
    private static final int AD_VIEW = 1;
    private static final int ITEM_FEED_COUNT = 3;
    List<ProductModel> trendingProductModelList = new ArrayList<>();
    Activity context;
    TrendingProductInterface trendingProductInterface;
    ShowAds showAds = new ShowAds();

    public TrendingProductAdapter(Activity context, TrendingProductInterface trendingProductInterface) {
        this.context = context;
        this.trendingProductInterface = trendingProductInterface;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rectangular_layout, parent, false));

        } else if (viewType == AD_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.ad_layout, parent, false);
            return new AdViewHolder(view);
        } else return null;
    }

    @Override
    public int getItemViewType(int position) {
        if ((position + 1) % ITEM_FEED_COUNT == 0) {
            return AD_VIEW;
        }
        return ITEM_VIEW;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
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

        if (holder.getItemViewType() == ITEM_VIEW) {
            int position = pos - Math.round(pos / ITEM_FEED_COUNT);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    250
            );
            params.setMargins(10, 10, 10, 10);
            ((ViewHolder) holder).rectangleLayoutCard.setLayoutParams(params);

            ((ViewHolder) holder).itemTitle.setText(HtmlCompat.fromHtml(trendingProductModelList.get(position).getProductTitle(), HtmlCompat.FROM_HTML_MODE_LEGACY));
            Glide.with(context).load(ApiWebServices.base_url + "all_products_images/" +
                            trendingProductModelList.get(position).getProductImage())
                    .placeholder(CommonMethods.setShimmer(holder.itemView.getContext()))
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(((ViewHolder) holder).itemImg);
            holder.itemView.setOnClickListener(view ->
                    trendingProductInterface.OnTrendingProductClicked(trendingProductModelList.get(position)));

        } else if (holder.getItemViewType() == AD_VIEW) {

            ((AdViewHolder) holder).bindAdData();
        }

    }


    @Override
    public int getItemCount() {
        if (trendingProductModelList.size() > 0) {
            return trendingProductModelList.size() + Math.round(trendingProductModelList.size() / ITEM_FEED_COUNT);
        }
        return 0;
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

    public class AdViewHolder extends RecyclerView.ViewHolder {
        AdLayoutBinding binding;

        public AdViewHolder(@NonNull View itemAdView2) {
            super(itemAdView2);
            binding = AdLayoutBinding.bind(itemAdView2);
        }

        private void bindAdData() {
            if (Objects.equals(Paper.book().read(Prevalent.nativeAdsType), "Native")) {
                showAds.showNativeAds(context, binding.adLayout);
            } else if (Objects.equals(Paper.book().read(Prevalent.nativeAdsType), "MREC")) {
                showAds.showMrec(context, binding.adLayout);
            }

        }


    }

}
