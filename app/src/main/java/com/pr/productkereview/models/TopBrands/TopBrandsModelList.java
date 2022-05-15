package com.pr.productkereview.models.TopBrands;

import java.util.List;

public class TopBrandsModelList {
    List<TopBrandsModel> data = null;

    public TopBrandsModelList(List<TopBrandsModel> data) {
        this.data = data;
    }

    public List<TopBrandsModel> getData() {
        return data;
    }
}
