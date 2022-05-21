package com.pr.productreviewadmin.activities;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.pr.productreviewadmin.adapters.ProductAdapter;
import com.pr.productreviewadmin.adapters.ProductInterface;
import com.pr.productreviewadmin.databinding.ActivityShowProductsBinding;
import com.pr.productreviewadmin.databinding.ProductLaytoutBinding;
import com.pr.productreviewadmin.models.ApiInterface;
import com.pr.productreviewadmin.models.ApiWebServices;
import com.pr.productreviewadmin.models.MessageModel;
import com.pr.productreviewadmin.models.ProductModel;
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

public class ShowProducts extends AppCompatActivity implements ProductInterface {
    public static String key, id;
    ActivityShowProductsBinding binding;
    GridLayoutManager layoutManager;
    ProductAdapter productAdapter;
    List<ProductModel> productModels;
    ApiInterface apiInterface;
    Dialog loadingDialog, productDialog;
    ProductLaytoutBinding productLaytoutBinding;
    ActivityResultLauncher<String> launcher;
    String encodedImage, encodedImage2, deleteLogo, deleteBanner;

    Map<String, String> map = new HashMap<>();
    Call<MessageModel> call;
    boolean banner = false, logo = false;
    ItemTouchHelper.SimpleCallback simpleCallback;

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
        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                try {
                    if (logo) {
                        logo = false;
                        final InputStream inputStream = getContentResolver().openInputStream(result);
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (productLaytoutBinding != null) {
                            productLaytoutBinding.logo.setImageBitmap(bitmap);

                        }
                        encodedImage = imageStore(bitmap);
                    } else if (banner) {
                        banner = false;
                        final InputStream inputStream = getContentResolver().openInputStream(result);
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (productLaytoutBinding != null) {
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

    }

    private void fetchProducts(String id) {
        loadingDialog.show();
        Call<List<ProductModel>> callProduct = apiInterface.fetchProducts(id);
        callProduct.enqueue(new Callback<List<ProductModel>>() {
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

        Log.d("ContentValue", id);

        if (Objects.equals(id, "latest") || Objects.equals(id, "best") || Objects.equals(id, "trending")) {
            simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    if (Objects.equals(id, "latest")) {
                        map.put("id", productModels.get(viewHolder.getAdapterPosition()).getId());
                        map.put("key", "latest");
                        call = apiInterface.removeProduct(map);
                        deleteData(call);
                        productModels.remove(viewHolder.getAdapterPosition());
                    } else if (Objects.equals(id, "best")) {
                        map.put("id", productModels.get(viewHolder.getAdapterPosition()).getId());
                        map.put("key", "best");
                        call = apiInterface.removeProduct(map);
                        deleteData(call);
                        productModels.remove(viewHolder.getAdapterPosition());
                    } else if (Objects.equals(id, "trending")) {
                        map.put("id", productModels.get(viewHolder.getAdapterPosition()).getId());
                        map.put("key", "trending");
                        call = apiInterface.removeProduct(map);
                        deleteData(call);
                        productModels.remove(viewHolder.getAdapterPosition());
                    }
                }

                private void deleteData(Call<MessageModel> call) {
                    call.enqueue(new Callback<MessageModel>() {
                        @Override
                        public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(ShowProducts.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                                if (key != null) {
                                    fetchProducts(key);
                                } else if (id != null) {
                                    fetchProducts(id);

                                }
                            } else {
                                Toast.makeText(ShowProducts.this, Objects.requireNonNull(response.body()).getError(), Toast.LENGTH_SHORT).show();
                            }
                            loadingDialog.dismiss();
                        }


                        @Override
                        public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                            loadingDialog.dismiss();

                        }
                    });
                }


            };
        } else {
            simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                    deleteProducts(productModels.get(viewHolder.getAdapterPosition()), "deleteS");
                    productModels.remove(viewHolder.getAdapterPosition());
                }
            };


        }
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.productRV);
    }

    @Override
    public void productClicked(ProductModel productModel) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);

        if (Objects.equals(key, "latest") || Objects.equals(key, "best") || Objects.equals(key, "trending")) {

        } else {
            String[] items = new String[]{"Update Product", "Delete Product"};
            builder.setTitle("Update OR Delete Product").setCancelable(true).setItems(items, (dialogInterface, which) -> {
                switch (which) {
                    case 0:
                        updateProducts(productModel);
                        break;
                    case 1:
                        deleteProducts(productModel, "deleteN");
                        break;

                }
            });

            builder.show();
        }

    }

    private void deleteProducts(ProductModel productModel, String deleteS) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        if (deleteS.equals("deleteS")) {
            deleteCall(productModel);
        } else if (deleteS.equals("deleteN")) {
            builder.setTitle("Delete Product")
                    .setMessage("Would you like to delete this banner?")
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    })
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        deleteCall(productModel);

                    }).show();
        }
    }

    private void deleteCall(ProductModel productModel) {
        loadingDialog.show();

        map.put("id", productModel.getId());
        map.put("img", productModel.getProductImage());
        map.put("banner", productModel.getBanner());
        call = apiInterface.deleteProduct(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ShowProducts.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    if (key != null) {
                        fetchProducts(key);
                    } else if (id != null) {
                        fetchProducts(id);

                    }
                } else {
                    Toast.makeText(ShowProducts.this, Objects.requireNonNull(response.body()).getError(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }


            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                loadingDialog.dismiss();

            }
        });
    }


    private void updateProducts(ProductModel productModel) {
        productDialog = new Dialog(this);
        productLaytoutBinding = ProductLaytoutBinding.inflate(getLayoutInflater());
        productDialog.setContentView(productLaytoutBinding.getRoot());
        productDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        productDialog.setCancelable(false);
        productDialog.show();
        deleteLogo = productModel.getProductImage();
        deleteBanner = productModel.getBanner();
        encodedImage = productModel.getProductImage();
        encodedImage2 = productModel.getBanner();
        productLaytoutBinding.title.setText(String.format("Update %s", productModel.getProductTitle()));
        productLaytoutBinding.backBtn.setOnClickListener(view -> productDialog.dismiss());
        productLaytoutBinding.logo.setOnClickListener(view -> {
            launcher.launch("image/*");
            logo = true;

        });
        productLaytoutBinding.banner.setOnClickListener(view -> {
            launcher.launch("image/*");
            banner = true;
        });
        productLaytoutBinding.treandingProduct.setChecked(Boolean.parseBoolean(productModel.getTrendingProduct()));
        productLaytoutBinding.bestProduct.setChecked(Boolean.parseBoolean(productModel.getBestProduct()));
        productLaytoutBinding.latestProduct.setChecked(Boolean.parseBoolean(productModel.getLatestProduct()));
        productLaytoutBinding.titleTv.setText(productModel.getProductTitle());
        Glide.with(this).load(ApiWebServices.base_url + "all_products_images/" + productModel.getProductImage()).into(productLaytoutBinding.logo);
        Glide.with(this).load(ApiWebServices.base_url + "all_products_images/" + productModel.getBanner()).into(productLaytoutBinding.banner);
        productLaytoutBinding.buyingGuideHindi.setText(productModel.getBuingGuideHindi());
        productLaytoutBinding.buyingGuideEnglish.setText(productModel.getBuingGuideEnglish());
        productLaytoutBinding.enterRatingInHindi.setText(productModel.getRatingHindi());
        productLaytoutBinding.enterRatingInEnglish.setText(productModel.getRatingEnglish());

        productLaytoutBinding.okBtn.setOnClickListener(view -> {
            loadingDialog.show();
            boolean trending;
            boolean best;
            boolean latest;

            trending = productLaytoutBinding.treandingProduct.isChecked();
            best = productLaytoutBinding.bestProduct.isChecked();
            latest = productLaytoutBinding.latestProduct.isChecked();
            String tittle = productLaytoutBinding.titleTv.getText().toString().trim();
            String buyingGuideHindi = productLaytoutBinding.buyingGuideHindi.getText().toString().trim();
            String buyingGuideEnglish = productLaytoutBinding.buyingGuideEnglish.getText().toString().trim();
            String enterRatingInHindi = productLaytoutBinding.enterRatingInHindi.getText().toString().trim();
            String enterRatingInEnglish = productLaytoutBinding.enterRatingInEnglish.getText().toString().trim();

            if (TextUtils.isEmpty(tittle)) {
                productLaytoutBinding.titleTv.setError("Url Required");
                productLaytoutBinding.titleTv.requestFocus();
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(buyingGuideHindi)) {
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
                if (encodedImage.length() > 100 && encodedImage2.length() > 100) {
                    map.put("catId", productModel.getId());
                    map.put("img", encodedImage);
                    map.put("banner", encodedImage2);
                    map.put("deleteLogo", deleteLogo);
                    map.put("deleteBanner", deleteBanner);
                    map.put("title", tittle);
                    map.put("buyGuideH", buyingGuideHindi);
                    map.put("buyGuideE", buyingGuideEnglish);
                    map.put("ratingH", enterRatingInHindi);
                    map.put("ratingE", enterRatingInEnglish);
                    map.put("latestPro", String.valueOf(latest));
                    map.put("bestPro", String.valueOf(best));
                    map.put("trendingPro", String.valueOf(trending));
                    map.put("key", "3");
                } else if (encodedImage.length() > 100) {
                    map.put("catId", productModel.getId());
                    map.put("img", encodedImage);
                    map.put("banner", encodedImage2);
                    map.put("deleteLogo", deleteLogo);
                    map.put("deleteBanner", deleteBanner);
                    map.put("title", tittle);
                    map.put("buyGuideH", buyingGuideHindi);
                    map.put("buyGuideE", buyingGuideEnglish);
                    map.put("ratingH", enterRatingInHindi);
                    map.put("ratingE", enterRatingInEnglish);
                    map.put("latestPro", String.valueOf(latest));
                    map.put("bestPro", String.valueOf(best));
                    map.put("trendingPro", String.valueOf(trending));
                    map.put("key", "2");
                } else if (encodedImage2.length() > 100) {
                    map.put("catId", productModel.getId());
                    map.put("img", encodedImage);
                    map.put("banner", encodedImage2);
                    map.put("deleteLogo", deleteLogo);
                    map.put("deleteBanner", deleteBanner);
                    map.put("title", tittle);
                    map.put("buyGuideH", buyingGuideHindi);
                    map.put("buyGuideE", buyingGuideEnglish);
                    map.put("ratingH", enterRatingInHindi);
                    map.put("ratingE", enterRatingInEnglish);
                    map.put("latestPro", String.valueOf(latest));
                    map.put("bestPro", String.valueOf(best));
                    map.put("trendingPro", String.valueOf(trending));
                    map.put("key", "1");
                } else {
                    map.put("catId", productModel.getId());
                    map.put("img", encodedImage);
                    map.put("banner", encodedImage2);
                    map.put("deleteLogo", deleteLogo);
                    map.put("deleteBanner", deleteBanner);
                    map.put("title", tittle);
                    map.put("buyGuideH", buyingGuideHindi);
                    map.put("buyGuideE", buyingGuideEnglish);
                    map.put("ratingH", enterRatingInHindi);
                    map.put("ratingE", enterRatingInEnglish);
                    map.put("latestPro", String.valueOf(latest));
                    map.put("bestPro", String.valueOf(best));
                    map.put("trendingPro", String.valueOf(trending));
                    map.put("key", "0");
                }

                call = apiInterface.updateProducts(map);
                updateData(call, productDialog);

            }

        });


    }

    private void updateData(Call<MessageModel> call, Dialog uploadDialog) {
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ShowProducts.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    uploadDialog.dismiss();
                    if (key != null) {
                        fetchProducts(key);
                    } else if (id != null) {
                        fetchProducts(id);

                    }
                } else {
                    Toast.makeText(ShowProducts.this, Objects.requireNonNull(response.body()).getError(), Toast.LENGTH_SHORT).show();
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