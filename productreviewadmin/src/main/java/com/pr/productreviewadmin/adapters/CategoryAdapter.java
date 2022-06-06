package com.pr.productreviewadmin.adapters;

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

import com.bumptech.glide.Glide;
import com.pr.productreviewadmin.R;
import com.pr.productreviewadmin.models.ApiWebServices;
import com.pr.productreviewadmin.models.CatModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    List<CatModel> catModels = new ArrayList<>();
    Activity context;
    CateogryInterface cateogryInterface;

    public CategoryAdapter(Activity context, CateogryInterface cateogryInterface) {
        this.context = context;
        this.cateogryInterface = cateogryInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cat_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.itemTitle.setText(HtmlCompat.fromHtml(catModels.get(position).getTitle(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        Glide.with(context).load(ApiWebServices.base_url + "all_categories_images/" + catModels.get(position).getBanner()).into(holder.itemImg);
        holder.itemView.setOnClickListener(view -> cateogryInterface.onCategoryClicked(catModels.get(position)));
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
            itemImg = itemView.findViewById(R.id.item_img);
            itemTitle = itemView.findViewById(R.id.item_title);
        }
    }
}
