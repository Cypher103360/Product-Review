package com.pr.productkereview.models.Sections;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pr.productkereview.R;
import com.pr.productkereview.adapters.LatestProductAdapter;
import com.pr.productkereview.adapters.LatestProductClickInterface;
import com.pr.productkereview.models.AllProducts.ProductModel;

import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder> implements LatestProductClickInterface {
    List<Section> sectionList;

    public MainRecyclerAdapter(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Section section = sectionList.get(position);
        String sectionName = section.getSectionName();
        List<ProductModel> items = section.getLatestProductModelList();

        holder.sectionName.setText(sectionName);
        LatestProductAdapter latestProductAdapter = new LatestProductAdapter(
                holder.itemView.getContext(), this,true);
        latestProductAdapter.updateList(items);
        holder.childRecyclerView.setAdapter(latestProductAdapter);

    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    @Override
    public void OnLatestProductClicked(ProductModel latestProductModel) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView sectionName;
        RecyclerView childRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sectionName = itemView.findViewById(R.id.sectionName);
            childRecyclerView = itemView.findViewById(R.id.childRecyclerView);
        }
    }
}
