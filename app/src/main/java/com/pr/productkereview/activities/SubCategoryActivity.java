package com.pr.productkereview.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pr.productkereview.adapters.SubCatAdapter.SubCategoryAdapter;
import com.pr.productkereview.adapters.SubCatAdapter.SubCategoryInterface;
import com.pr.productkereview.databinding.ActivitySubCategoryBinding;
import com.pr.productkereview.models.categories.CatModel;
import com.pr.productkereview.models.categories.CatModelFactory;
import com.pr.productkereview.models.categories.CatViewModel;
import com.pr.productkereview.utils.ApiInterface;
import com.pr.productkereview.utils.ApiWebServices;
import com.pr.productkereview.utils.CommonMethods;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryActivity extends AppCompatActivity implements SubCategoryInterface {
    ActivitySubCategoryBinding binding;
    SubCategoryAdapter subCategoryAdapter;
    Dialog loading;
    List<CatModel> catModelList = new ArrayList<>();
    ApiInterface apiInterface;
    CatViewModel catViewModel;
    String id, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loading = CommonMethods.getLoadingDialog(SubCategoryActivity.this);
        loading.show();
        apiInterface = ApiWebServices.getApiInterface();
        id = getIntent().getStringExtra("id");
        title = getIntent().getStringExtra("title");

        catViewModel = new ViewModelProvider(SubCategoryActivity.this,
                new CatModelFactory(this.getApplication(), id)).get(CatViewModel.class);


        binding.activityTitle.setText(title);
        binding.backIcon.setOnClickListener(v -> {
            onBackPressed();
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.subCatRecyclerview.setLayoutManager(layoutManager);
        subCategoryAdapter = new SubCategoryAdapter(this, this);
        binding.subCatRecyclerview.setAdapter(subCategoryAdapter);

        setSubCategories();
    }

    private void setSubCategories() {
        catViewModel.getCategories().observe(SubCategoryActivity.this, catModels -> {
            if (!catModels.isEmpty()) {
                catModelList.clear();
                catModelList.addAll(catModels);
                subCategoryAdapter.updateCategoryList(catModelList);
                loading.dismiss();
            }
        });
    }

    @Override
    public void onSubCategoryClicked(CatModel catModel) {
        if (catModel.getSubCat().equals("true")) {
            Intent intent = new Intent(SubCategoryActivity.this, SubCategoryActivity.class);
            intent.putExtra("title", catModel.getTitle());
            intent.putExtra("id", catModel.getId());
            startActivity(intent);
        } else if (catModel.getProduct().equals("true")) {
            Intent intent = new Intent(SubCategoryActivity.this, ShowAllItemsActivity.class);
            intent.putExtra("key","Products");
            intent.putExtra("id", catModel.getId());
            startActivity(intent);
        } else {
            Toast.makeText(this, "No data is available!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loading.show();
        setSubCategories();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}