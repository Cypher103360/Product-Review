package com.pr.productreviewadmin.models;

import java.util.List;

public class BrandsModelList {
    List<BrandsModel> data = null;

    public BrandsModelList(List<BrandsModel> data) {
        this.data = data;
    }

    public List<BrandsModel> getData() {
        return data;
    }
}
