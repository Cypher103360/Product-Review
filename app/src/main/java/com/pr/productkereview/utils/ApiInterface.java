package com.pr.productkereview.utils;

import com.pr.productkereview.models.AllProducts.ProductModel;
import com.pr.productkereview.models.BannerImages.BannerImageModel;
import com.pr.productkereview.models.MessageModel;
import com.pr.productkereview.models.TopBrands.BrandsModel;
import com.pr.productkereview.models.UrlsModels.UrlModel;
import com.pr.productkereview.models.categories.CatModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("upload_user_data.php")
    Call<MessageModel> uploadUserData(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("fetch_urls.php")
    Call<UrlModel> getUrls(@Field("id") String id);

    @POST("fetch_banners.php")
    Call<List<BannerImageModel>> getBanners();

    @FormUrlEncoded
    @POST("fetch_sub_categories.php")
    Call<List<CatModel>> getCategories(@Field("id") String id);

    @POST("fetch_brands.php")
    Call<List<BrandsModel>> fetchBrands();

    @FormUrlEncoded
    @POST("fetch_products.php")
    Call<List<ProductModel>> fetchProducts(@Field("id") String id);

    @FormUrlEncoded
    @POST("fetch_products.php")
    Call<List<ProductModel>> fetchBestProducts(@Field("id") String id);
}

