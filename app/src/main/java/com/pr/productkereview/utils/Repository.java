package com.pr.productkereview.utils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.pr.productkereview.models.AllProducts.ProductModel;
import com.pr.productkereview.models.TopBrands.BrandsModel;
import com.pr.productkereview.models.TopBrands.BrandsModelList;
import com.pr.productkereview.models.categories.CatModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    public static Repository repository;
    ApiInterface apiInterface;

    MutableLiveData<List<CatModel>> catModelMutableLiveData = new MutableLiveData<>();
    MutableLiveData<BrandsModelList> brandMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<ProductModel>> latestProductMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<ProductModel>> bestProductMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<ProductModel>> trendingProductMutableLiveData = new MutableLiveData<>();


    public Repository() {
        apiInterface = ApiWebServices.getApiInterface();
    }

    public static Repository getInstance() {
        if (repository == null) {
            repository = new Repository();
        }
        return repository;
    }

    public MutableLiveData<List<CatModel>> getCatModelMutableLiveData(String id) {
        Call<List<CatModel>> call = apiInterface.getCategories(id);
        call.enqueue(new Callback<List<CatModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CatModel>> call, @NonNull Response<List<CatModel>> response) {
                if (response.isSuccessful()) {
                    catModelMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CatModel>> call, @NonNull Throwable t) {

            }
        });
        return catModelMutableLiveData;
    }

    public MutableLiveData<BrandsModelList> getBrandMutableLiveData() {
        Call<BrandsModelList> call = apiInterface.fetchBrands();
        call.enqueue(new Callback<BrandsModelList>() {
            @Override
            public void onResponse(@NonNull Call<BrandsModelList> call, @NonNull Response<BrandsModelList> response) {
                if (response.isSuccessful()) {
                    brandMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<BrandsModelList> call, @NonNull Throwable t) {

            }
        });
        return brandMutableLiveData;
    }

    public MutableLiveData<List<ProductModel>> getLatestProductMutableLiveData(String id) {
        Call<List<ProductModel>> call = apiInterface.fetchProducts(id);
        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (response.isSuccessful()) {
                    latestProductMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {

            }
        });
        return latestProductMutableLiveData;
    }

    public MutableLiveData<List<ProductModel>> getBestProductMutableLiveData(String id) {
        Call<List<ProductModel>> call = apiInterface.fetchBestProducts(id);
        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (response.isSuccessful()) {
                    bestProductMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {

            }
        });
        return bestProductMutableLiveData;
    }

    public MutableLiveData<List<ProductModel>> getTrendingProductMutableLiveData(String id) {
        Call<List<ProductModel>> call = apiInterface.fetchBestProducts(id);
        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (response.isSuccessful()) {
                    trendingProductMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {

            }
        });
        return trendingProductMutableLiveData;
    }
}
