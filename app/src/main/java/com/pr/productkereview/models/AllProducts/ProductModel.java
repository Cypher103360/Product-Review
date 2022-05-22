package com.pr.productkereview.models.AllProducts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductModel implements Serializable {
    private String banner;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("product_title")
    @Expose
    private String productTitle;
    @SerializedName("buing_guide_hindi")
    @Expose
    private String buingGuideHindi;
    @SerializedName("buing_guide_english")
    @Expose
    private String buingGuideEnglish;
    @SerializedName("rating_hindi")
    @Expose
    private String ratingHindi;
    @SerializedName("rating_english")
    @Expose
    private String ratingEnglish;
    @SerializedName("latest_product")
    @Expose
    private String latestProduct;
    @SerializedName("best_product")
    @Expose
    private String bestProduct;
    @SerializedName("trending_product")
    @Expose
    private String trendingProduct;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getBanner() {
        return banner;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getBuingGuideHindi() {
        return buingGuideHindi;
    }

    public void setBuingGuideHindi(String buingGuideHindi) {
        this.buingGuideHindi = buingGuideHindi;
    }

    public String getBuingGuideEnglish() {
        return buingGuideEnglish;
    }

    public void setBuingGuideEnglish(String buingGuideEnglish) {
        this.buingGuideEnglish = buingGuideEnglish;
    }

    public String getRatingHindi() {
        return ratingHindi;
    }

    public void setRatingHindi(String ratingHindi) {
        this.ratingHindi = ratingHindi;
    }

    public String getRatingEnglish() {
        return ratingEnglish;
    }

    public void setRatingEnglish(String ratingEnglish) {
        this.ratingEnglish = ratingEnglish;
    }

    public String getLatestProduct() {
        return latestProduct;
    }

    public void setLatestProduct(String latestProduct) {
        this.latestProduct = latestProduct;
    }

    public String getBestProduct() {
        return bestProduct;
    }

    public void setBestProduct(String bestProduct) {
        this.bestProduct = bestProduct;
    }

    public String getTrendingProduct() {
        return trendingProduct;
    }

    public void setTrendingProduct(String trendingProduct) {
        this.trendingProduct = trendingProduct;
    }
}
