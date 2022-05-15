package com.pr.productkereview.models.BestProducts;

import com.pr.productkereview.models.LatestProduct.LatestProductModel;

import java.util.List;

public class BestProductModelList {
    List<BestProductModel> data = null;

    public BestProductModelList(List<BestProductModel> data) {
        this.data = data;
    }

    public List<BestProductModel> getData() {
        return data;
    }
}
