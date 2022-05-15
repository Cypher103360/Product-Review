package com.pr.productkereview.models.BuyingGuides;

public class BuyingGuidesModel {
    String id,image,title;

    public BuyingGuidesModel(String id, String image, String title) {
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
