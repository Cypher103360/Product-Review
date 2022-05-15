package com.pr.productkereview.models.TopBrands;

public class TopBrandsModel {
    String id,image,title;

    public TopBrandsModel(String id, String image, String title) {
        this.id = id;
        this.image = image;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }
}
