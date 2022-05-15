package com.pr.productkereview.models.BannerImages;

import java.util.List;

public class BannerImageModleList {
    List<BannerImageModel> data = null;

    public BannerImageModleList(List<BannerImageModel> data) {
        this.data = data;
    }

    public List<BannerImageModel> getData() {
        return data;
    }
}
