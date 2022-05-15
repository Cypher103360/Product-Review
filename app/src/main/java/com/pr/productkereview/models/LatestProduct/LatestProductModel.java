package com.pr.productkereview.models.LatestProduct;

public class LatestProductModel {
    String id,image,title;

    public LatestProductModel(String id, String image, String title) {
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
