package com.pr.productkereview.adapters.topBrands;

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
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.google.android.material.card.MaterialCardView;
import com.pr.productkereview.R;
import com.pr.productkereview.databinding.AdLayoutBinding;
import com.pr.productkereview.models.TopBrands.BrandsModel;
import com.pr.productkereview.utils.ApiWebServices;
import com.pr.productkereview.utils.Prevalent;
import com.pr.productkereview.utils.ShowAds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;

public class TopBrandsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_VIEW = 0;
    private static final int AD_VIEW = 1;
    private static final int ITEM_FEED_COUNT = 4;
    private final boolean shouldShowAllItems;
    ShowAds showAds = new ShowAds();
    List<BrandsModel> topBrandsModelList = new ArrayList<>();
    Activity context;
    TopBrandsClickInterface topBrandsClickInterface;

    public TopBrandsAdapter(Activity context, TopBrandsClickInterface topBrandsClickInterface, boolean shouldShowAllItems) {
        this.context = context;
        this.topBrandsClickInterface = topBrandsClickInterface;
        this.shouldShowAllItems = shouldShowAllItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (shouldShowAllItems) {
            if ((position + 1) % ITEM_FEED_COUNT == 0) {
                return AD_VIEW;
            }
            return ITEM_VIEW;
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (shouldShowAllItems) {
            if (viewType == ITEM_VIEW) {
                View view = LayoutInflater.from(context).inflate(R.layout.top_brands_layout, parent, false);
                return new ViewHolder(view);
            } else if (viewType == AD_VIEW) {
                View view = LayoutInflater.from(context).inflate(R.layout.ad_layout, parent, false);
                final ViewGroup.LayoutParams lp = view.getLayoutParams();
                if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams glp = (StaggeredGridLayoutManager.LayoutParams) lp;
                    glp.setFullSpan(true);
                }
                return new AdViewHolder(view);
            } else return null;
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.top_brands_layout, parent, false);
            return new ViewHolder(view);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {

        if (shouldShowAllItems) {
            if (holder.getItemViewType() == ITEM_VIEW) {
                int position = pos - Math.round(pos / ITEM_FEED_COUNT);
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
                    ((ViewHolder) holder).imageCard.setLayoutParams(params);
                }
                Glide.with(context).load(ApiWebServices.base_url + "top_brands_images/" +
                                topBrandsModel.getLogo())
                        .placeholder(shimmerDrawable)
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(((ViewHolder) holder).itemImage);
                ((ViewHolder) holder).itemTitle.setText(Html.fromHtml(topBrandsModel.getTitle(), Html.FROM_HTML_MODE_LEGACY));
                holder.itemView.setOnClickListener(v -> {
                    topBrandsClickInterface.OnTopBrandClicked(topBrandsModelList.get(position));
                });

            } else if (holder.getItemViewType() == AD_VIEW) {
                ((AdViewHolder) holder).bindAdData();
            }

        } else {
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

            BrandsModel topBrandsModel = topBrandsModelList.get(pos);
            if (shouldShowAllItems) {
                // holder.imageCard.setLayoutParams(new ViewGroup.LayoutParams(210, 210));
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                        235,
                        235
                );
                params.setMargins(10, 10, 10, 10);
                ((ViewHolder) holder).imageCard.setLayoutParams(params);
            }
            Glide.with(context).load(ApiWebServices.base_url + "top_brands_images/" +
                            topBrandsModel.getLogo())
                    .placeholder(shimmerDrawable)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(((ViewHolder) holder).itemImage);
            ((ViewHolder) holder).itemTitle.setText(Html.fromHtml(topBrandsModel.getTitle(), Html.FROM_HTML_MODE_LEGACY));
            holder.itemView.setOnClickListener(v -> {
                topBrandsClickInterface.OnTopBrandClicked(topBrandsModelList.get(pos));
            });

        }


    }

    @Override
    public int getItemCount() {
        if (shouldShowAllItems) {
            if (topBrandsModelList.size() > 0) {
                return topBrandsModelList.size() + Math.round(topBrandsModelList.size() / ITEM_FEED_COUNT);
            }

        } else {
            int limit = 7;
            return Math.min(topBrandsModelList.size(), limit);
        }
        return 0;
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
