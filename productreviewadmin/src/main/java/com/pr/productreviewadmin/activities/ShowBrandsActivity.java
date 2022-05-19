package com.pr.productreviewadmin.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pr.productreviewadmin.adapters.BrandsAdapter;
import com.pr.productreviewadmin.adapters.BrandsInterface;
import com.pr.productreviewadmin.databinding.ActivityShowProductsBinding;
import com.pr.productreviewadmin.models.ApiInterface;
import com.pr.productreviewadmin.models.ApiWebServices;
import com.pr.productreviewadmin.models.BrandsModel;
import com.pr.productreviewadmin.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowBrandsActivity extends AppCompatActivity implements BrandsInterface {
    ActivityShowProductsBinding binding;
    GridLayoutManager layoutManager;
    BrandsAdapter brandsAdapter;
    List<BrandsModel> brandsModels;
    ApiInterface apiInterface;
    Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.productRV.setLayoutManager(layoutManager);

        brandsModels = new ArrayList<>();
        brandsAdapter = new BrandsAdapter(this, this);
        apiInterface = ApiWebServices.getApiInterface();
        loadingDialog = Utils.loadingDialog(this);
        binding.productRV.setAdapter(brandsAdapter);
        fetchBrands();
    }

    private void fetchBrands() {
        loadingDialog.show();
        Call<List<BrandsModel>> call = apiInterface.fetchBrands();
        call.enqueue(new Callback<List<BrandsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<BrandsModel>> call, @NonNull Response<List<BrandsModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        brandsModels.clear();
                        brandsModels.addAll(response.body());
                    }
                    brandsAdapter.updateCategoryList(brandsModels);

                }
                Log.d("ContentValue", response.body().toString());

                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<List<BrandsModel>> call, @NonNull Throwable t) {
                loadingDialog.dismiss();
                Log.d("ContentValue", t.getMessage());

            }
        });

    }

    @Override
    public void brandsClicked(BrandsModel brandsModel) {

    }
}