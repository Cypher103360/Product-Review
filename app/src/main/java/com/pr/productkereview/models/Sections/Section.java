package com.pr.productkereview.models.Sections;

import com.pr.productkereview.models.AllProducts.ProductModel;

import java.util.List;

public class Section {
    private String sectionName;
    private List<ProductModel> latestProductModelList;

    public Section(String sectionName, List<ProductModel> latestProductModelList) {
        this.sectionName = sectionName;
        this.latestProductModelList = latestProductModelList;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public List<ProductModel> getLatestProductModelList() {
        return latestProductModelList;
    }

    public void setLatestProductModelList(List<ProductModel> latestProductModelList) {
        this.latestProductModelList = latestProductModelList;
    }

    @Override
    public String toString() {
        return "Section{" +
                "sectionName='" + sectionName + '\'' +
                ", latestProductModelList=" + latestProductModelList +
                '}';
    }
}
