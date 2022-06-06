package com.pr.productreviewadmin.models;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {


    @FormUrlEncoded
    @POST("upload_banners.php")
    Call<MessageModel> uploadBanners(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("update_banners.php")
    Call<MessageModel> updateBanners(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("delete_banner.php")
    Call<MessageModel> deleteBanners(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("delete_category.php")
    Call<MessageModel> deleteCategory(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("delete_product.php")
    Call<MessageModel> deleteProduct(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("delete_brand.php")
    Call<MessageModel> deleteBrand(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("remove_product.php")
    Call<MessageModel> removeProduct(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("upload_top_brands.php")
    Call<MessageModel> uploadTopBrands(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("ads_fetch.php")
    Call<List<AdsModel>> fetchAds(@Field("id") String id);

    @FormUrlEncoded
    @POST("ads_update.php")
    Call<MessageModel> updateAdIds(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("upload_category.php")
    Call<MessageModel> uploadCategory(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("upload_sub_category.php")
    Call<MessageModel> uploadSubCategory(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("update_category.php")
    Call<MessageModel> updateCategory(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("upload_products.php")
    Call<MessageModel> uploadProducts(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("update_products.php")
    Call<MessageModel> updateProducts(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("update_brands.php")
    Call<MessageModel> updateBrands(@FieldMap Map<String, String> map);


    @FormUrlEncoded
    @POST("fetch_sub_categories.php")
    Call<List<CatModel>> fetchCategories(@Field("id") String id);

    @POST("fetch_banners.php")
    Call<List<BannerModel>> fetchBanner();

    @FormUrlEncoded
    @POST("fetch_products.php")
    Call<List<ProductModel>> fetchProducts(@Field("id") String id);

    @POST("fetch_brands.php")
    Call<BrandsModelList> fetchBrands();

    @FormUrlEncoded
    @POST("update_urls.php")
    Call<MessageModel> updateUrls(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("fetch_urls.php")
    Call<UrlModel> fetchUrls(@Field("id") String id);
}

