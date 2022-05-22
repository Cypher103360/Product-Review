package com.pr.productkereview.adapters.categories;

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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pr.productkereview.R;
import com.pr.productkereview.models.categories.CatModel;
import com.pr.productkereview.utils.ApiWebServices;
import com.pr.productkereview.utils.CommonMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    List<CatModel> catModels = new ArrayList<>();
    Activity context;
    CategoryInterface categoryInterface;

    public CategoryAdapter(Activity context, CategoryInterface categoryInterface) {
        this.context = context;
        this.categoryInterface = categoryInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.sub_cat_layout, parent, false));
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

        holder.itemTitle.setText(Html.fromHtml(catModels.get(position).getTitle(),Html.FROM_HTML_MODE_LEGACY));

        Glide.with(context).load(ApiWebServices.base_url + "all_categories_images/"
                        + catModels.get(position).getBanner())
                .placeholder(CommonMethods.setShimmer(holder.itemView.getContext()))
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(holder.itemImg);
        holder.itemView.setOnClickListener(view -> categoryInterface.onCategoryClicked(catModels.get(position)));
    }

    @Override
    public int getItemCount() {
        return catModels.size();
    }

    @SuppressLint("NotifyDataSetChanged")
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
