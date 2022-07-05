package com.pr.productkereview.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "products")
public class Products implements Serializable {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final int id;

    @ColumnInfo(name = "categoryId")
    private String categoryId;

    @ColumnInfo(name = "productImage")
    private String productImage;

    @ColumnInfo(name = "banner")
    private String banner;

    @ColumnInfo(name = "productTitle")
    private String productTitle;

    @ColumnInfo(name = "buyingGuideHindi")
    private String buyingGuideHindi;

    @ColumnInfo(name = "buyingGuideEnglish")
    private String buyingGuideEnglish;

    @ColumnInfo(name = "ratingHindi")
    private String ratingHindi;

    @ColumnInfo(name = "ratingEnglish")
    private String ratingEnglish;

    @ColumnInfo(name = "latestProduct")
    private String latestProduct;

    @ColumnInfo(name = "bestProduct")
    private String bestProduct;

    @ColumnInfo(name = "trendingProduct")
    private String trendingProduct;

    private String url;

    public Products(int id, String categoryId, String productImage, String banner, String productTitle, String buyingGuideHindi, String buyingGuideEnglish, String ratingHindi, String ratingEnglish, String latestProduct, String bestProduct, String trendingProduct,String url) {
        this.id = id;
        this.categoryId = categoryId;
        this.productImage = productImage;
        this.banner = banner;
        this.productTitle = productTitle;
        this.buyingGuideHindi = buyingGuideHindi;
        this.buyingGuideEnglish = buyingGuideEnglish;
        this.ratingHindi = ratingHindi;
        this.ratingEnglish = ratingEnglish;
        this.latestProduct = latestProduct;
        this.bestProduct = bestProduct;
        this.trendingProduct = trendingProduct;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getBanner() {
        return banner;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public String getBuyingGuideHindi() {
        return buyingGuideHindi;
    }

    public String getBuyingGuideEnglish() {
        return buyingGuideEnglish;
    }

    public String getRatingHindi() {
        return ratingHindi;
    }

    public String getRatingEnglish() {
        return ratingEnglish;
    }

    public String getLatestProduct() {
        return latestProduct;
    }

    public String getBestProduct() {
        return bestProduct;
    }

    public String getTrendingProduct() {
        return trendingProduct;
    }

    public String getUrl() {
        return url;
    }
}
