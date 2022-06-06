package com.pr.productkereview.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
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

public class BestProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_VIEW = 0;
    private static final int AD_VIEW = 1;
    private static final int ITEM_FEED_COUNT = 4;
    private final boolean shouldShowAllItems;
    List<ProductModel> bestProductModelList = new ArrayList<>();
    Activity context;
    BestProductClickInterface bestProductClickInterface;
    ShowAds showAds = new ShowAds();

    public BestProductAdapter(Activity context, BestProductClickInterface bestProductClickInterface, boolean shouldShowAllItems) {
        this.context = context;
        this.bestProductClickInterface = bestProductClickInterface;
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
                View view = LayoutInflater.from(context).inflate(R.layout.best_product_layout, parent, false);
                return new ViewHolder(view);

            } else if (viewType == AD_VIEW) {
                View view = LayoutInflater.from(context).inflate(R.layout.ad_layout, parent, false);
                final ViewGroup.LayoutParams lp = view.getLayoutParams();
                if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams glp = (StaggeredGridLayoutManager.LayoutParams) lp;
                    glp.setFullSpan(true);
                }
                return new AdViewHolder(view);
            } else
                return null;

        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.best_product_layout, parent, false);
            return new ViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {

        if (shouldShowAllItems) {
            if (holder.getItemViewType() == ITEM_VIEW) {
                int position = pos - Math.round(pos / ITEM_FEED_COUNT);
                ProductModel bestProductModel = bestProductModelList.get(position);
                Glide.with(context).load(ApiWebServices.base_url + "all_products_images/"
                        + bestProductModel.getProductImage())
                        .placeholder(CommonMethods.setShimmer(context))
                        .into(((ViewHolder) holder).itemImage);
                ((ViewHolder) holder).itemTitle.setText(HtmlCompat.fromHtml(bestProductModel.getProductTitle(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                holder.itemView.setOnClickListener(v -> {
                    bestProductClickInterface.OnBestProductClicked(bestProductModelList.get(position));
                });
            } else if (holder.getItemViewType() == AD_VIEW) {
                ((AdViewHolder) holder).bindAdData();

            }

        } else {
            ProductModel bestProductModel = bestProductModelList.get(pos);
            Glide.with(context).load(ApiWebServices.base_url + "all_products_images/"
                    + bestProductModel.getProductImage())
                    .placeholder(CommonMethods.setShimmer(context))
                    .into(((ViewHolder) holder).itemImage);
            ((ViewHolder) holder).itemTitle.setText(HtmlCompat.fromHtml(bestProductModel.getProductTitle(), HtmlCompat.FROM_HTML_MODE_LEGACY));
            holder.itemView.setOnClickListener(v -> {
                bestProductClickInterface.OnBestProductClicked(bestProductModelList.get(pos));
            });
        }

    }

    @Override
    public int getItemCount() {
        if (shouldShowAllItems) {
            if (bestProductModelList.size() > 0) {
                return bestProductModelList.size() + Math.round(bestProductModelList.size() / ITEM_FEED_COUNT);
            }

        } else {
            int limit = 6;
            return Math.min(bestProductModelList.size(), limit);
        }
        return 0;
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
