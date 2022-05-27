package com.pr.productreviewadmin.activities;

import android.app.Dialog;
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
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.pr.productreviewadmin.R;
import com.pr.productreviewadmin.adapters.BrandsAdapter;
import com.pr.productreviewadmin.adapters.BrandsInterface;
import com.pr.productreviewadmin.databinding.ActivityShowProductsBinding;
import com.pr.productreviewadmin.databinding.UploadTopBrandLayoutBinding;
import com.pr.productreviewadmin.models.ApiInterface;
import com.pr.productreviewadmin.models.ApiWebServices;
import com.pr.productreviewadmin.models.BrandsModel;
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

public class ShowBrandsActivity extends AppCompatActivity implements BrandsInterface {
    ActivityShowProductsBinding binding;
    GridLayoutManager layoutManager;
    BrandsAdapter brandsAdapter;
    List<BrandsModel> brandsModels;
    ApiInterface apiInterface;
    Dialog loadingDialog, topBrandLayoutDialog;
    UploadTopBrandLayoutBinding topBrandLayoutBinding;
    Map<String, String> map = new HashMap<>();
    ItemTouchHelper.SimpleCallback simpleCallback;
    String encodedImage, encodedImage2, deleteLogo, deleteBanner;
    ActivityResultLauncher<String> launcher;
    Call<MessageModel> call;
    boolean banner = false, logo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        layoutManager = new GridLayoutManager(this, 3);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.productRV.setLayoutManager(layoutManager);

        brandsModels = new ArrayList<>();
        brandsAdapter = new BrandsAdapter(this, this);
        apiInterface = ApiWebServices.getApiInterface();
        loadingDialog = Utils.loadingDialog(this);
        binding.productRV.setAdapter(brandsAdapter);
        fetchBrands();

        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                try {
                    if (logo) {
                        logo = false;
                        final InputStream inputStream = getContentResolver().openInputStream(result);
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (topBrandLayoutBinding != null) {
                            topBrandLayoutBinding.logo.setImageBitmap(bitmap);

                        }
                        encodedImage = imageStore(bitmap);
                    } else if (banner) {
                        banner = false;
                        final InputStream inputStream = getContentResolver().openInputStream(result);
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        if (topBrandLayoutBinding != null) {
                            topBrandLayoutBinding.banner.setImageBitmap(bitmap);

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

    public String imageStore(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);

        byte[] imageBytes = stream.toByteArray();
        return android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
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
                Log.d("ContentValue", Objects.requireNonNull(response.body()).toString());

                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<List<BrandsModel>> call, @NonNull Throwable t) {
                loadingDialog.dismiss();
                Log.d("ContentValue", t.getMessage());

            }
        });

        simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                deleteProducts(brandsModels.get(viewHolder.getAdapterPosition()), "deleteS");
                brandsModels.remove(viewHolder.getAdapterPosition());
            }
        };


        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.productRV);

    }

    @Override
    public void brandsClicked(BrandsModel brandsModel) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);

        String[] items = new String[]{"Update Product", "Delete Product"};
        builder.setTitle("Update OR Delete Product").setCancelable(true).setItems(items, (dialogInterface, which) -> {
            switch (which) {
                case 0:
                    updateTopBrandsDialog(brandsModel);
                    break;
                case 1:
                    deleteProducts(brandsModel, "deleteN");
                    break;

            }
        });

        builder.show();
    }

    private void deleteProducts(BrandsModel brandsModel, String deleteS) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        if (deleteS.equals("deleteS")) {
            deleteCall(brandsModel);
        } else if (deleteS.equals("deleteN")) {
            builder.setTitle("Delete Brand")
                    .setMessage("Would you like to delete this banner?")
                    .setNegativeButton("Cancel", (dialogInterface, i) -> {
                    })
                    .setPositiveButton("Ok", (dialogInterface, i) -> {
                        deleteCall(brandsModel);

                    }).show();
        }
    }

    private void deleteCall(BrandsModel brandsModel) {
        loadingDialog.show();
        map.put("id", brandsModel.getId());
        map.put("img", brandsModel.getLogo());
        map.put("banner", brandsModel.getBanner());
        call = apiInterface.deleteBrand(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ShowBrandsActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    fetchBrands();
                } else {
                    Toast.makeText(ShowBrandsActivity.this, Objects.requireNonNull(response.body()).getError(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }


            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                loadingDialog.dismiss();

            }
        });
    }


    private void updateTopBrandsDialog(BrandsModel brandsModel) {

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

        deleteLogo = brandsModel.getLogo();
        deleteBanner = brandsModel.getBanner();
        encodedImage = brandsModel.getLogo();
        encodedImage2 = brandsModel.getBanner();
        topBrandLayoutBinding.title.setText(R.string.upload_top_brands);
        topBrandLayoutBinding.desc.setVisibility(View.VISIBLE);
        topBrandLayoutBinding.url.setVisibility(View.VISIBLE);


        topBrandLayoutBinding.titleTv.setText(HtmlCompat.fromHtml(brandsModel.getTitle(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        topBrandLayoutBinding.desc.setText(HtmlCompat.fromHtml(brandsModel.getDesc(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        topBrandLayoutBinding.url.setText(brandsModel.getUrl());
        Glide.with(this).load(ApiWebServices.base_url + "top_brands_images/" + brandsModel.getLogo()).into(topBrandLayoutBinding.logo);
        Glide.with(this).load(ApiWebServices.base_url + "top_brands_images/" + brandsModel.getBanner()).into(topBrandLayoutBinding.banner);


        topBrandLayoutBinding.okBtn.setOnClickListener(view -> {
            loadingDialog.show();
            String tittle = topBrandLayoutBinding.titleTv.getText().toString().trim();

            String desc = topBrandLayoutBinding.desc.getText().toString().trim();
            String imgUrl = topBrandLayoutBinding.url.getText().toString().trim();

            if (TextUtils.isEmpty(tittle)) {
                topBrandLayoutBinding.titleTv.setError("Url Required");
                topBrandLayoutBinding.titleTv.requestFocus();
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(desc)) {
                topBrandLayoutBinding.desc.setError("Url Required");
                topBrandLayoutBinding.desc.requestFocus();
                loadingDialog.dismiss();
            } else if (TextUtils.isEmpty(imgUrl)) {
                topBrandLayoutBinding.url.setError("Url Required");
                topBrandLayoutBinding.url.requestFocus();
                loadingDialog.dismiss();
            } else {
                if (encodedImage.length() > 100 && encodedImage2.length() > 100) {
                    map.put("id", brandsModel.getId());
                    map.put("logo", encodedImage);
                    map.put("banner", encodedImage2);
                    map.put("deleteLogo", deleteLogo);
                    map.put("deleteBanner", deleteBanner);
                    map.put("title", tittle);
                    map.put("desc", desc);
                    map.put("url", imgUrl);
                    map.put("key", "3");
                } else if (encodedImage.length() > 100) {
                    map.put("id", brandsModel.getId());
                    map.put("logo", encodedImage);
                    map.put("banner", encodedImage2);
                    map.put("deleteLogo", deleteLogo);
                    map.put("deleteBanner", deleteBanner);
                    map.put("title", tittle);
                    map.put("desc", desc);
                    map.put("url", imgUrl);
                    map.put("key", "2");
                } else if (encodedImage2.length() > 100) {
                    map.put("id", brandsModel.getId());
                    map.put("logo", encodedImage);
                    map.put("banner", encodedImage2);
                    map.put("deleteLogo", deleteLogo);
                    map.put("deleteBanner", deleteBanner);
                    map.put("title", tittle);
                    map.put("desc", desc);
                    map.put("url", imgUrl);
                    map.put("key", "1");
                } else {
                    map.put("id", brandsModel.getId());
                    map.put("logo", encodedImage);
                    map.put("banner", encodedImage2);
                    map.put("deleteLogo", deleteLogo);
                    map.put("deleteBanner", deleteBanner);
                    map.put("title", tittle);
                    map.put("desc", desc);
                    map.put("url", imgUrl);
                    map.put("key", "0");
                }

                call = apiInterface.updateBrands(map);
                updateData(call, topBrandLayoutDialog);


            }

        });

    }

    private void updateData(Call<MessageModel> call, Dialog uploadDialog) {
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ShowBrandsActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    uploadDialog.dismiss();
                    fetchBrands();
                } else {
                    Toast.makeText(ShowBrandsActivity.this, Objects.requireNonNull(response.body()).getError(), Toast.LENGTH_SHORT).show();
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

}