package com.pr.productreviewadmin.adapters;

import android.app.Activity;
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
import com.pr.productreviewadmin.models.BannerModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {

    List<BannerModel> bannerModels = new ArrayList<>();
    Activity context;
    BannerInterface bannerInterface;

    public BannerAdapter(Activity context, BannerInterface bannerInterface) {
        this.context = context;
        this.bannerInterface = bannerInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cat_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        Glide.with(context).load(ApiWebServices.base_url + "bannerImages/" + bannerModels.get(position).getImage()).into(holder.itemImg);
        holder.itemView.setOnClickListener(view -> bannerInterface.onBannerClicked(bannerModels.get(position)));
    }

    @Override
    public int getItemCount() {
        return bannerModels.size();
    }

    public void updateBannerList(List<BannerModel> bannerModelList) {
        bannerModels.clear();
        bannerModels.addAll(bannerModelList);
        Collections.reverse(bannerModels);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImg;
        TextView itemTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImg = itemView.findViewById(R.id.item_img);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemTitle.setVisibility(View.GONE);
        }
    }
}
