package com.pr.productkereview.adapters.Products;

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
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pr.productkereview.R;
import com.pr.productkereview.databinding.AdLayoutBinding;
import com.pr.productkereview.models.AllProducts.ProductModel;
import com.pr.productkereview.utils.ApiWebServices;
import com.pr.productkereview.utils.Prevalent;
import com.pr.productkereview.utils.ShowAds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int ITEM_VIEW = 0;
    private static final int AD_VIEW = 1;
    private static final int ITEM_FEED_COUNT = 3;
    private final boolean shouldShowAllItems;
    List<ProductModel> productModelList = new ArrayList<>();
    Activity context;
    ProductsClickInterface productsClickInterface;
    ShowAds showAds = new ShowAds();

    public ProductsAdapter(Activity context, ProductsClickInterface productsClickInterface, boolean shouldShowAllItems) {
        this.context = context;
        this.productsClickInterface = productsClickInterface;
        this.shouldShowAllItems = shouldShowAllItems;
    }

    @Override
    public int getItemViewType(int position) {
        if ((position + 1) % ITEM_FEED_COUNT == 0) {
            return AD_VIEW;
        }
        return ITEM_VIEW;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.sub_cat_layout, parent, false);
            return new ViewHolder(view);

        } else if (viewType == AD_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.ad_layout, parent, false);
            return new AdViewHolder(view);
        } else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {

        if (holder.getItemViewType() == ITEM_VIEW) {
            int position = pos - Math.round(pos / ITEM_FEED_COUNT);
            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(holder.itemView.getContext());
            circularProgressDrawable.setStrokeWidth(5f);
            circularProgressDrawable.setCenterRadius(30f);
            circularProgressDrawable.start();

            ProductModel productModel = productModelList.get(position);
            Glide.with(context).load(ApiWebServices.base_url + "all_products_images/" + productModel.getProductImage())
                    .placeholder(circularProgressDrawable)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(((ViewHolder) holder).itemImage);
            ((ViewHolder) holder).itemTitle.setText(HtmlCompat.fromHtml(productModel.getProductTitle(), HtmlCompat.FROM_HTML_MODE_LEGACY));
            holder.itemView.setOnClickListener(v -> {
                productsClickInterface.OnProductClicked(productModelList.get(position));
            });
        } else if (holder.getItemViewType() == AD_VIEW) {
            ((AdViewHolder) holder).bindAdData();
        }

    }

    @Override
    public int getItemCount() {
        if (shouldShowAllItems) {
            if (productModelList.size() > 0) {
                return productModelList.size() + Math.round(productModelList.size() / ITEM_FEED_COUNT);
            }

        } else {
            int limit = 7;

            return Math.min(productModelList.size() + Math.round(productModelList.size() / ITEM_FEED_COUNT), limit);
        }

        return 0;
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
