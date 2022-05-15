package com.pr.productkereview.models.LatestProduct;

import java.util.List;

public class LatestProductModelList {
    List<LatestProductModel> data = null;

    public LatestProductModelList(List<LatestProductModel> data) {
        this.data = data;
    }

    public List<LatestProductModel> getData() {
        return data;
    }
}
