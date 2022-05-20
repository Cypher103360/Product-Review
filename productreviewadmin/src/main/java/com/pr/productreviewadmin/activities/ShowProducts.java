package com.pr.productreviewadmin.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pr.productreviewadmin.adapters.ProductAdapter;
import com.pr.productreviewadmin.adapters.ProductInterface;
import com.pr.productreviewadmin.databinding.ActivityShowProductsBinding;
import com.pr.productreviewadmin.models.ApiInterface;
import com.pr.productreviewadmin.models.ApiWebServices;
import com.pr.productreviewadmin.models.ProductModel;
import com.pr.productreviewadmin.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowProducts extends AppCompatActivity implements ProductInterface {
    ActivityShowProductsBinding binding;
    GridLayoutManager layoutManager;
    ProductAdapter productAdapter;
    List<ProductModel> productModels;
    ApiInterface apiInterface;
    Dialog loadingDialog;
    String key, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityShowProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.productRV.setLayoutManager(layoutManager);

        productModels = new ArrayList<>();
        productAdapter = new ProductAdapter(this, this);
        apiInterface = ApiWebServices.getApiInterface();
        loadingDialog = Utils.loadingDialog(this);
        binding.productRV.setAdapter(productAdapter);
        key = getIntent().getStringExtra("catId");
        id = getIntent().getStringExtra("key");
        if (key != null) {
            fetchProducts(key);
        } else if (id != null) {
            fetchProducts(id);

        }
    }

    private void fetchProducts(String id) {
        loadingDialog.show();
        Call<List<ProductModel>> call = apiInterface.fetchProducts(id);
        call.enqueue(new Callback<List<ProductModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductModel>> call, @NonNull Response<List<ProductModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        productModels.clear();
                        productModels.addAll(response.body());
                    }
                    productAdapter.updateCategoryList(productModels);

                }
                Log.d("ContentValue", Objects.requireNonNull(response.body()).toString());

                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductModel>> call, @NonNull Throwable t) {
                loadingDialog.dismiss();
                Log.d("ContentValue", t.getMessage());

            }
        });

    }

    @Override
    public void productClicked(ProductModel productModel) {


    }
}