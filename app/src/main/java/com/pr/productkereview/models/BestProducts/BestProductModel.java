package com.pr.productkereview.models.BestProducts;

public class BestProductModel {
    String id,image,title;

    public BestProductModel(String id, String image, String title) {
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
