package com.pr.productreviewadmin.adapters;

import android.app.Activity;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pr.productreviewadmin.R;
import com.pr.productreviewadmin.models.ApiWebServices;
import com.pr.productreviewadmin.models.BrandsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BrandsAdapter extends RecyclerView.Adapter<BrandsAdapter.ViewHolder> {

    List<BrandsModel> brandsModels = new ArrayList<>();
    Activity context;
    BrandsInterface brandsInterface;

    public BrandsAdapter(Activity context, BrandsInterface brandsInterface) {
        this.context = context;
        this.brandsInterface = brandsInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.top_brands_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.itemTitle.setText(Html.fromHtml(brandsModels.get(position).getTitle(), Html.FROM_HTML_MODE_LEGACY));
        }
        Glide.with(context).load(ApiWebServices.base_url + "top_brands_images/" + brandsModels.get(position).getLogo()).into(holder.itemImg);
        holder.itemView.setOnClickListener(view -> brandsInterface.brandsClicked(brandsModels.get(position)));

    }

    public void updateCategoryList(List<BrandsModel> brandsModelList) {
        brandsModels.clear();
        brandsModels.addAll(brandsModelList);
        Collections.reverse(brandsModels);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return brandsModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImg;
        TextView itemTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.item_img);
            itemTitle = itemView.findViewById(R.id.item_title);
        }
    }
}
