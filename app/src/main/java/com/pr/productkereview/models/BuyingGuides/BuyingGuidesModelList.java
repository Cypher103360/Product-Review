package com.pr.productkereview.models.BuyingGuides;

import java.util.List;

public class BuyingGuidesModelList {
    List<BuyingGuidesModel> data = null;

    public BuyingGuidesModelList(List<BuyingGuidesModel> data) {
        this.data = data;
    }

    public List<BuyingGuidesModel> getData() {
        return data;
    }
}
