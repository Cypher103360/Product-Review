package com.pr.productkereview.adapters.SubCatAdapter;

import android.app.Activity;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.pr.productkereview.R;
import com.pr.productkereview.databinding.AdLayoutBinding;
import com.pr.productkereview.models.categories.CatModel;
import com.pr.productkereview.utils.ApiWebServices;
import com.pr.productkereview.utils.Prevalent;
import com.pr.productkereview.utils.ShowAds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;

public class SubCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW = 0;
    private static final int AD_VIEW = 1;
    private static final int ITEM_FEED_COUNT = 3;
    ShowAds showAds = new ShowAds();
    List<CatModel> catModels = new ArrayList<>();
    Activity context;
    SubCategoryInterface subCategoryInterface;
    SharedPreferences preferences;


    public SubCategoryAdapter(Activity context, SubCategoryInterface categoryInterface) {
        this.context = context;
        this.subCategoryInterface = categoryInterface;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

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
            return new ViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.sub_cat_layout, parent, false));
        } else if (viewType == AD_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.ad_layout, parent, false);
            return new AdViewHolder(view);
        } else return null;
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
            Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                    .setDuration(700) // how long the shimmering animation takes to do one full sweep
                    .setBaseAlpha(0.6f) //the alpha of the underlying children
                    .setHighlightAlpha(0.8f) // the shimmer alpha amount
                    .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                    .setAutoStart(true)
                    .build();

            // This is the placeholder for the imageView
            ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
            shimmerDrawable.setShimmer(shimmer);

            ((ViewHolder) holder).itemTitle.setText(HtmlCompat.fromHtml(catModels.get(position).getTitle(), HtmlCompat.FROM_HTML_MODE_LEGACY));
            Glide.with(context).load(ApiWebServices.base_url + "all_categories_images/"
                            + catModels.get(position).getBanner())
                    .placeholder(shimmerDrawable)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(((ViewHolder) holder).itemImg);
            holder.itemView.setOnClickListener(view -> {
                subCategoryInterface.onSubCategoryClicked(catModels.get(position));
                preferences.edit().clear().apply();
            });

            if (preferences.getString("action", "").equals("cat")) {
                if (!preferences.getString("cat_pos", "").equals("")) {
                    subCategoryInterface.onSubCategoryClicked(catModels.get(Integer.parseInt(preferences.getString("cat_pos", "0"))));
//                preferences.edit().clear().apply();
                }
            }

        } else if (holder.getItemViewType() == AD_VIEW) {
            ((AdViewHolder) holder).bindAdData();
        }
    }

    @Override
    public int getItemCount() {
        if (catModels.size() > 0) {
            return catModels.size() + Math.round(catModels.size() / ITEM_FEED_COUNT);
        }
        return 0;
    }

    public void updateCategoryList(List<CatModel> catModelList) {
        catModels.clear();
        catModels.addAll(catModelList);
        Collections.reverse(catModels);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImg;
        TextView itemTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.cat_image);
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
