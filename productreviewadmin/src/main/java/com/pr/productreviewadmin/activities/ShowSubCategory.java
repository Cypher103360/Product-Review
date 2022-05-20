package com.pr.productreviewadmin.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.pr.productreviewadmin.adapters.CategoryAdapter;
import com.pr.productreviewadmin.adapters.CateogryInterface;
import com.pr.productreviewadmin.databinding.ActivityShowSubCategoryBinding;
import com.pr.productreviewadmin.databinding.ProductLaytoutBinding;
import com.pr.productreviewadmin.databinding.UploadTopBrandLayoutBinding;
import com.pr.productreviewadmin.models.ApiInterface;
import com.pr.productreviewadmin.models.ApiWebServices;
import com.pr.productreviewadmin.models.CatModel;
import com.pr.productreviewadmin.models.MessageModel;
import com.pr.productreviewadmin.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowSubCategory extends AppCompatActivity implements CateogryInterface {
    LinearLayoutManager layoutManager;
    CategoryAdapter categoryAdapter;
    List<CatModel> catModels;
    ApiInterface apiInterface;
    Dialog loadingDialog;
    ActivityShowSubCategoryBinding binding;
    String key;
    UploadTopBrandLayoutBinding topBrandLayoutBinding;
    ProductLaytoutBinding productLaytoutBinding;

    // dialog variables
    Dialog topBrandLayoutDialog, productDialog;
    ActivityResultLauncher<String> launcher;
    String encodedImage, encodedImage2;
    Map<String, String> map = new HashMap<>();
    Call<MessageModel> call;
    boolean banner = false, logo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowSubCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.catRV.setLayoutManager(layoutManager);

        catModels = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(this, this);
        apiInterface = ApiWebServices.getApiInterface();
        loadingDialog = Utils.loadingDialog(this);
        binding.catRV.setAdapter(categoryAdapter);
        key = getIntent().getStringExtra("key");

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                try {
                    if (logo) {
                        logo = false;
                        final InputStream inputStream = getContentResolver().openInputStream(result);
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (topBrandLayoutBinding != null) {
                            topBrandLayoutBinding.logo.setImageBitmap(bitmap);

                        } else if (productLaytoutBinding != null) {
                            productLaytoutBinding.logo.setImageBitmap(bitmap);
                        }
                        encodedImage = imageStore(bitmap);
                    } else if (banner) {
                        banner = false;
                        final InputStream inputStream = getContentResolver().openInputStream(result);
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (topBrandLayoutBinding!=null){
                            topBrandLayoutBinding.banner.setImageBitmap(bitmap);

                        }else if (productLaytoutBinding!=null){
                            productLaytoutBinding.banner.setImageBitmap(bitmap);

                        }
                        encodedImage2 = imageStore(bitmap);
                    }


                    Log.d("ContentValue", encodedImage + " @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@/n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@/n" + encodedImage2);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


        });
        fetchCategory(key);
        Log.d("ContentValue", key);

    }

    private void fetchCategory(String id) {
        loadingDialog.show();
        Call<List<CatModel>> call = apiInterface.fetchCategories(id);
        call.enqueue(new Callback<List<CatModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<CatModel>> call, @NonNull Response<List<CatModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        catModels.clear();
                        catModels.addAll(response.body());
                    }
                    categoryAdapter.updateCategoryList(catModels);

                }
                Log.d("ContentValue", response.body().toString());

                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<List<CatModel>> call, @NonNull Throwable t) {
                loadingDialog.dismiss();
                Log.d("ContentValue", t.getMessage());

            }
        });
    }

    @Override
    public void onCategoryClicked(CatModel catModel) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        if (catModel.getSubCat().equals("false") && catModel.getProduct().equals("false")) {
            String[] items = new String[]{"Add Sub Category", "Add Item", "Update Category", "Delete Category"};
            builder.setTitle("Add Sub Category or Item").setCancelable(true).setItems(items, (dialogInterface, which) -> {
                switch (which) {
                    case 0:
                        uploadTopBrandsDialog(catModel);
                        break;
                    case 1:
                        uploadProducts(catModel);
                        break;
                    case 2:
                        // update category
//                        catId = categoryModel.getId();
//                        catImage = categoryModel.getImage();
//                        catTitle = categoryModel.getTitle();
//                        updateCategoryDialog(catId, catImage, catTitle);
                        break;
                    case 3:
//                        // delete cat
//                        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
//                        builder.setTitle("Delete this Category?")
//                                .setNegativeButton("Cancel", (dialog1, which1) -> {
//
//                                }).setPositiveButton("Delete", (dialog12, which12) -> {
//                                    loadingDialog.show();
//                                    catId = categoryModel.getId();
//                                    catImage = categoryModel.getImage();
//                                    map.put("id", catId);
//                                    map.put("title", "category");
//                                    map.put("path", "category_images/" + catImage);
//                                    deleteCategory(map, "category");
//                                }).show();
//                        break;
                }
            });
        } else if (catModel.getSubCat().equals("true")) {
            String[] items2 = new String[]{"Add a Subcategory", "Show Sub Category", "Update Category"};
            builder.setTitle("Add Subcategories").setCancelable(true).setItems(items2, (dialogInterface, which) -> {
                switch (which) {
                    case 0:
                        uploadTopBrandsDialog(catModel);
                        break;
                    case 1:
                        Intent intent = new Intent(this, ShowSubCategory.class);
                        intent.putExtra("key", catModel.getId());
                        startActivity(intent);
                        break;
                    case 2:
                        // Update Category
//                        catId = categoryModel.getId();
//                        catImage = categoryModel.getImage();
//                        catTitle = categoryModel.getTitle();
//                        updateCategoryDialog(catId, catImage, catTitle);
                        break;
                }
            });
        } else if (catModel.getProduct().equals("true")) {
            String[] items3 = new String[]{"Add an Item", "Show Images", "Update Category"};
            builder.setTitle("Add Item").setCancelable(true).setItems(items3, (dialogInterface, which) -> {
                switch (which) {
                    case 0:
                        uploadProducts(catModel);
                        break;
                    case 1:
                        Intent intent = new Intent(this, ShowProducts.class);
                        intent.putExtra("catId", catModel.getId());
                        intent.putExtra("key", "catPro");

                        startActivity(intent);
                        break;
                    case 2:
                        // Update Category
//                        catId = categoryModel.getId();
//                        catImage = categoryModel.getImage();
//                        catTitle = categoryModel.getTitle();
//                        updateCategoryDialog(catId, catImage, catTitle);
                        break;
                }
            });
        }

        builder.show();

    }

    private void uploadProducts(CatModel catModel) {
        productDialog = new Dialog(this);
        productLaytoutBinding = ProductLaytoutBinding.inflate(getLayoutInflater());
        productDialog.setContentView(productLaytoutBinding.getRoot());
        productDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        productDialog.setCancelable(false);
        productDialog.show();

        productLaytoutBinding.backBtn.setOnClickListener(view -> topBrandLayoutDialog.dismiss());
        productLaytoutBinding.logo.setOnClickListener(view -> {
            launcher.launch("image/*");
            logo = true;

        });
        productLaytoutBinding.banner.setOnClickListener(view -> {
            launcher.launch("image/*");
            banner = true;
        });
        productLaytoutBinding.okBtn.setOnClickListener(view -> {
            loadingDialog.show();
            boolean trending;
            boolean best;
            boolean latest;
            trending = productLaytoutBinding.treandingProduct.isChecked();
            best = productLaytoutBinding.bestProduct.isChecked();
            latest = productLaytoutBinding.latestProduct.isChecked();
            String tittle = productLaytoutBinding.titleTv.getText().toString().trim();
            String buingGuideHindi = productLaytoutBinding.buyingGuideHindi.getText().toString().trim();
            String buyingGuideEnglish = productLaytoutBinding.buyingGuideEnglish.getText().toString().trim();
            String enterRatingInHindi = productLaytoutBinding.enterRatingInHindi.getText().toString().trim();
            String enterRatingInEnglish = productLaytoutBinding.enterRatingInEnglish.getText().toString().trim();

            if (encodedImage == null) {
                loadingDialog.dismiss();
                Toast.makeText(ShowSubCategory.this, "Please Select an Image", Toast.LENGTH_SHORT).show();
            } else if (encodedImage2 == null) {
                loadingDialog.dismiss();
                Toast.makeText(ShowSubCategory.this, "Please Select an Image", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(tittle)) {
                productLaytoutBinding.titleTv.setError("Url Required");
                productLaytoutBinding.titleTv.requestFocus();
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(buingGuideHindi)) {
                productLaytoutBinding.buyingGuideHindi.setError("Url Required");
                productLaytoutBinding.buyingGuideHindi.requestFocus();
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(buyingGuideEnglish)) {
                productLaytoutBinding.buyingGuideEnglish.setError("Url Required");
                productLaytoutBinding.buyingGuideEnglish.requestFocus();
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(enterRatingInHindi)) {
                productLaytoutBinding.enterRatingInHindi.setError("Url Required");
                productLaytoutBinding.enterRatingInHindi.requestFocus();
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(enterRatingInEnglish)) {
                productLaytoutBinding.enterRatingInEnglish.setError("Url Required");
                productLaytoutBinding.enterRatingInEnglish.requestFocus();
                loadingDialog.dismiss();
            } else {
                map.put("catId", catModel.getId());
                map.put("img", encodedImage);
                map.put("banner", encodedImage2);
                map.put("title", tittle);
                map.put("buyGuideH", buingGuideHindi);
                map.put("buyGuideE", buyingGuideEnglish);
                map.put("ratingH", enterRatingInHindi);
                map.put("ratingE", enterRatingInEnglish);
                map.put("latestPro", String.valueOf(latest));
                map.put("bestPro", String.valueOf(best));
                map.put("trendingPro", String.valueOf(trending));
                call = apiInterface.uploadProducts(map);
                uploadData(call, productDialog);

            }

        });


    }

    private void uploadTopBrandsDialog(CatModel catModel) {

        topBrandLayoutDialog = new Dialog(this);
        topBrandLayoutBinding = UploadTopBrandLayoutBinding.inflate(getLayoutInflater());
        topBrandLayoutDialog.setContentView(topBrandLayoutBinding.getRoot());
        topBrandLayoutDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        topBrandLayoutDialog.setCancelable(false);
        topBrandLayoutDialog.show();

        topBrandLayoutBinding.backBtn.setOnClickListener(view -> topBrandLayoutDialog.dismiss());
        topBrandLayoutBinding.banner.setOnClickListener(view -> {
            launcher.launch("image/*");
            banner = true;
        });
        topBrandLayoutBinding.logo.setOnClickListener(view -> {
            launcher.launch("image/*");
            logo = true;
        });

        topBrandLayoutBinding.title.setText("Upload Category");
        topBrandLayoutBinding.title.setText("Upload Brands");
        topBrandLayoutBinding.desc.setVisibility(View.GONE);
        topBrandLayoutBinding.url.setVisibility(View.GONE);
        topBrandLayoutBinding.banner.setVisibility(View.GONE);

        topBrandLayoutBinding.okBtn.setOnClickListener(view -> {
            loadingDialog.show();
            String tittle = topBrandLayoutBinding.titleTv.getText().toString().trim();

            if (encodedImage == null) {
                loadingDialog.dismiss();
                Toast.makeText(ShowSubCategory.this, "Please Select an Image", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(tittle)) {
                topBrandLayoutBinding.titleTv.setError("Url Required");
                topBrandLayoutBinding.titleTv.requestFocus();
                loadingDialog.dismiss();
            } else {
                map.put("logo", encodedImage);
                map.put("title", tittle);
                map.put("parentId", catModel.getId());
                map.put("subCat", "true");
                map.put("product", "false");
                call = apiInterface.uploadSubCategory(map);
                uploadData(call, topBrandLayoutDialog);

            }

        });

    }

    private void uploadData(Call<MessageModel> call, Dialog uploadDialog) {
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ShowSubCategory.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    uploadDialog.dismiss();
                } else {
                    Toast.makeText(ShowSubCategory.this, Objects.requireNonNull(response.body()).getError(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Log.d("responseError", t.getMessage());
                loadingDialog.dismiss();
            }
        });
    }

    public String imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);

        byte[] imageBytes = stream.toByteArray();
        return android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }
}