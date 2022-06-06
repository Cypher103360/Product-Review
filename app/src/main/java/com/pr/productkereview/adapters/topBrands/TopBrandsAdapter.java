package com.pr.productkereview.adapters.topBrands;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.card.MaterialCardView;
import com.pr.productkereview.R;
import com.pr.productkereview.activities.ShowAllItemsActivity;
import com.pr.productkereview.databinding.AdLayoutBinding;
import com.pr.productkereview.models.TopBrands.BrandsModel;
import com.pr.productkereview.utils.ApiWebServices;
import com.pr.productkereview.utils.CommonMethods;
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

    private static final int BUTTON_VIEW_ALL = 1;
    private static final int BUTTON_COUNT = 8;

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
            if (viewType == ITEM_VIEW) {
                View view = LayoutInflater.from(context).inflate(R.layout.top_brands_layout, parent, false);
                return new ViewHolder(view);
            } else if (viewType == BUTTON_VIEW_ALL) {
                View view = LayoutInflater.from(context).inflate(R.layout.view_all_top_brands_layout, parent, false);
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

                BrandsModel topBrandsModel = topBrandsModelList.get(position);

                // holder.imageCard.setLayoutParams(new ViewGroup.LayoutParams(210, 210));
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                        235,
                        235
                );
                params.setMargins(10, 10, 10, 10);
                ((ViewHolder) holder).imageCard.setLayoutParams(params);

                Glide.with(context).load(ApiWebServices.base_url + "top_brands_images/" +
                                topBrandsModel.getLogo())
                        .placeholder(CommonMethods.setShimmer(context))
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(((ViewHolder) holder).itemImage);
                ((ViewHolder) holder).itemTitle.setText(HtmlCompat.fromHtml(topBrandsModel.getTitle(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                holder.itemView.setOnClickListener(v -> {
                    topBrandsClickInterface.OnTopBrandClicked(topBrandsModelList.get(position));
                });

            } else if (holder.getItemViewType() == AD_VIEW) {
                ((AdViewHolder) holder).bindAdData();
            }

        } else {

            if (holder.getItemViewType() == ITEM_VIEW) {
                int position = pos - Math.round(pos / BUTTON_COUNT);
                BrandsModel brandsModel = topBrandsModelList.get(position);
                Glide.with(context).load(ApiWebServices.base_url + "top_brands_images/" +
                                brandsModel.getLogo())
                        .placeholder(CommonMethods.setShimmer(context))
                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                        .into(((ViewHolder) holder).itemImage);
                ((ViewHolder) holder).itemTitle.setText(HtmlCompat.fromHtml(brandsModel.getTitle(), HtmlCompat.FROM_HTML_MODE_LEGACY));
                holder.itemView.setOnClickListener(v -> {
                    topBrandsClickInterface.OnTopBrandClicked(topBrandsModelList.get(position));
                });
            } else if (holder.getItemViewType() == BUTTON_VIEW_ALL) {
                ((ButtonViewHolder) holder).viewAllBtn.setOnClickListener(v -> {
                    showAds.destroyBanner();
                    showAds.showInterstitialAds(context);
                    Intent intent = new Intent(context, ShowAllItemsActivity.class);
                    intent.putExtra("key", "topBrands");
                    context.startActivity(intent);
                });
            }

        }


    }

    @Override
    public int getItemCount() {
        if (shouldShowAllItems) {
            if (topBrandsModelList.size() > 0) {
                return topBrandsModelList.size() + Math.round(topBrandsModelList.size() / ITEM_FEED_COUNT);
            }

        } else {
            return Math.min(topBrandsModelList.size(), BUTTON_COUNT);
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

    public static class ButtonViewHolder extends RecyclerView.ViewHolder {
        ImageView viewAllBtn;
        TextView viewAllText;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            viewAllBtn = itemView.findViewById(R.id.view_all_imageView);
            viewAllText = itemView.findViewById(R.id.top_view_all_text);
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
