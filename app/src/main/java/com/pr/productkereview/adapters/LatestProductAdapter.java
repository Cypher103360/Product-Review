package com.pr.productkereview.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.pr.productkereview.R;
import com.pr.productkereview.activities.ShowAllItemsActivity;
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

public class LatestProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_VIEW = 0;

    private static final int AD_VIEW = 1;
    private static final int ITEM_FEED_COUNT = 4;

    private static final int BUTTON_VIEW_ALL = 1;
    private static final int BUTTON_COUNT = 8;

    private final boolean shouldShowAllItems;
    List<ProductModel> latestProductModelList = new ArrayList<>();
    Activity context;
    LatestProductClickInterface latestProductClickInterface;
    ShowAds showAds = new ShowAds();
    SharedPreferences preferences;


    public LatestProductAdapter(Activity context, LatestProductClickInterface latestProductClickInterface, boolean shouldShowAllItems) {
        this.context = context;
        this.latestProductClickInterface = latestProductClickInterface;
        this.shouldShowAllItems = shouldShowAllItems;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public int getItemViewType(int position) {
        if (shouldShowAllItems) {
            if ((position + 1) % ITEM_FEED_COUNT == 0) {
                return AD_VIEW;
            }
        } else {
            if ((position + 1) % BUTTON_COUNT == 0) {
                return BUTTON_VIEW_ALL;
            }
        }
        return ITEM_VIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (shouldShowAllItems) {
            if (viewType == ITEM_VIEW) {
                View view = LayoutInflater.from(context).inflate(R.layout.show_full_screen_layout, parent, false);
                return new ViewHolder(view);

            } else if (viewType == AD_VIEW) {
                View view = LayoutInflater.from(context).inflate(R.layout.ad_layout, parent, false);
                final ViewGroup.LayoutParams lp = view.getLayoutParams();
                if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams glp = (StaggeredGridLayoutManager.LayoutParams) lp;
                    glp.setFullSpan(true);
                }
                return new AdViewHolder(view);
            }
        } else {
            if (viewType == ITEM_VIEW) {
                View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
                return new ViewHolder(view);
            } else if (viewType == BUTTON_VIEW_ALL) {
                View view = LayoutInflater.from(context).inflate(R.layout.view_all_item_layout, parent, false);
                return new ButtonViewHolder(view);
            }

        }
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {

        if (shouldShowAllItems) {
            if (holder.getItemViewType() == ITEM_VIEW) {
                int position = pos - Math.round(pos / ITEM_FEED_COUNT);
                CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(holder.itemView.getContext());
                circularProgressDrawable.setStrokeWidth(5f);
                circularProgressDrawable.setCenterRadius(30f);
                circularProgressDrawable.start();

                ProductModel latestProductModel = latestProductModelList.get(position);
                Glide.with(context).load(ApiWebServices.base_url + "all_products_images/"
                                + latestProductModel.getProductImage())
                        .placeholder(circularProgressDrawable)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(((ViewHolder) holder).itemImage);
                ((ViewHolder) holder).itemTitle.setText(HtmlCompat.fromHtml(latestProductModel.getProductTitle(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                holder.itemView.setOnClickListener(v -> {
                    latestProductClickInterface.OnLatestProductClicked(latestProductModelList.get(position), position);
                });

                if (preferences.getString("action", "").equals("lat")) {
                    if (!preferences.getString("pos", "").equals("")) {
                        latestProductClickInterface.OnLatestProductClicked(latestProductModelList.get(Integer.parseInt(preferences.getString("pos", "0"))), position);
//                preferences.edit().clear().apply();
                    }
                }

            } else if (holder.getItemViewType() == AD_VIEW) {
                ((AdViewHolder) holder).bindAdData();

            }

        } else {
            if (holder.getItemViewType() == ITEM_VIEW) {
                ProductModel latestProductModel = latestProductModelList.get(pos);
                Glide.with(context).load(ApiWebServices.base_url + "all_products_images/"
                                + latestProductModel.getProductImage())
                        .placeholder(CommonMethods.CircularDrawable(context))
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(((ViewHolder) holder).itemImage);
                ((ViewHolder) holder).itemTitle.setText(HtmlCompat.fromHtml(latestProductModel.getProductTitle(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                holder.itemView.setOnClickListener(v -> {
                    latestProductClickInterface.OnLatestProductClicked(latestProductModelList.get(pos), pos);
                });

                if (preferences.getString("action", "").equals("lat")) {
                    if (!preferences.getString("pos", "").equals("")) {
                        latestProductClickInterface.OnLatestProductClicked(latestProductModelList.get(Integer.parseInt(preferences.getString("pos", "0"))), pos);
//                preferences.edit().clear().apply();
                    }
                }

            } else if (holder.getItemViewType() == BUTTON_VIEW_ALL) {
                ((ButtonViewHolder) holder).fab.setOnClickListener(v -> {
                    showAds.destroyBanner();
                    showAds.showInterstitialAds(context);
                    Intent intent = new Intent(context, ShowAllItemsActivity.class);
                    intent.putExtra("key", "latestProducts");
                    context.startActivity(intent);
                });
            }
        }

    }


    @Override
    public int getItemCount() {
        if (shouldShowAllItems) {
            if (latestProductModelList.size() > 0) {
                return latestProductModelList.size() + Math.round(latestProductModelList.size() / ITEM_FEED_COUNT);
            }

        } else {
            return Math.min(latestProductModelList.size(), BUTTON_COUNT);
        }
        return 0;
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

    public class ButtonViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView viewAllCardBtn;
        ImageView fab;
        TextView viewAllText;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            viewAllCardBtn = itemView.findViewById(R.id.view_all_card_btn);
            fab = itemView.findViewById(R.id.view_all_fab_btn);
            viewAllText = itemView.findViewById(R.id.view_all_text);


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
