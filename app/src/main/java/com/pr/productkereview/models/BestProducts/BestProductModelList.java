package com.pr.productkereview.models.BestProducts;

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
