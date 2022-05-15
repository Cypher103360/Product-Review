package com.pr.productkereview.models.BannerImages;

public class BannerImageModel {
    String id,Image,ImageUrl;

    public BannerImageModel(String id, String image, String imageUrl) {
        this.id = id;
        Image = image;
        ImageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return Image;
    }

    public String getImageUrl() {
        return ImageUrl;
    }
}
