package com.pr.productkereview.adapters.SubCatAdapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.pr.productkereview.R;
import com.pr.productkereview.adapters.categories.CategoryInterface;
import com.pr.productkereview.models.categories.CatModel;
import com.pr.productkereview.utils.ApiWebServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder> {

    List<CatModel> catModels = new ArrayList<>();
    Activity context;
    SubCategoryInterface subCategoryInterface;

    public SubCategoryAdapter(Activity context, SubCategoryInterface categoryInterface) {
        this.context = context;
        this.subCategoryInterface = categoryInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.sub_cat_layout, parent, false));
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

        holder.itemTitle.setText(Html.fromHtml(catModels.get(position).getTitle(),Html.FROM_HTML_MODE_LEGACY));
        Glide.with(context).load(ApiWebServices.base_url + "all_categories_images/"
                        + catModels.get(position).getBanner())
                .placeholder(shimmerDrawable)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.itemImg);
        holder.itemView.setOnClickListener(view -> subCategoryInterface.onSubCategoryClicked(catModels.get(position)));
    }

    @Override
    public int getItemCount() {
        return catModels.size();
    }

    public void updateCategoryList(List<CatModel> catModelList) {
        catModels.clear();
        catModels.addAll(catModelList);
        Collections.reverse(catModels);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImg;
        TextView itemTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.cat_image);
            itemTitle = itemView.findViewById(R.id.cat_title);
        }
    }
}
