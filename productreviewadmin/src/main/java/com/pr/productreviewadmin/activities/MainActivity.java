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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.pr.productreviewadmin.R;
import com.pr.productreviewadmin.databinding.ActivityMainBinding;
import com.pr.productreviewadmin.databinding.AdsUpdateLayoutBinding;
import com.pr.productreviewadmin.databinding.UploadBannerLayoutBinding;
import com.pr.productreviewadmin.databinding.UploadTopBrandLayoutBinding;
import com.pr.productreviewadmin.models.AdsModel;
import com.pr.productreviewadmin.models.ApiInterface;
import com.pr.productreviewadmin.models.ApiWebServices;
import com.pr.productreviewadmin.models.MessageModel;
import com.pr.productreviewadmin.models.UrlModel;
import com.pr.productreviewadmin.utils.Utils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // binding variables
    ActivityMainBinding binding;
    UploadBannerLayoutBinding uploadBannerLayoutBinding;
    UploadTopBrandLayoutBinding topBrandLayoutBinding;
    // dialog variables
    Dialog uploadBannerDialog, loadingDialog, topBrandLayoutDialog, adsUpdateDialog;
    ActivityResultLauncher<String> launcher;
    String encodedImage, encodedImage2;
    Map<String, String> map = new HashMap<>();
    Call<MessageModel> call;
    ApiInterface apiInterface;
    String urlId, urls;
    boolean banner = false, logo = false;
    Intent intent;

    String[] items = new String[]{"AdmobWithMeta", "IronSourceWithMeta", "AppLovinWithMeta", "Meta"};
    String[] item2 = new String[]{"Native", "MREC"};
    AutoCompleteTextView BannerTopNetworkName, BannerBottomNetworkName, InterstitialNetwork, NativeAdsNetworkName, RewardAdsNetwork, nativeType;
    EditText AppId, AppLovinSdkKey, BannerTop, BannerBottom, InterstitialAds, NativeAds, rewardAds;
    Button UploadAdsBtn;
    String key;
    Dialog loading;
    String appId, appLovinSdkKey, bannerTopNetworkName, bannerTop, bannerBottomNetworkName,
            bannerBottom, interstitialNetwork, interstitialAds, nativeAdsNetworkName,
            nativeAds, nativeAdsType, rewardAd, rewardAdsNetwork;

    AdsUpdateLayoutBinding adsUpdateLayoutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadingDialog = Utils.loadingDialog(this);

        binding.uploadBanner.setOnClickListener(view -> {
            showUploadBannerDialog("banner");
        });
        binding.uploadCat.setOnClickListener(view -> {
            uploadTopBrandsDialog("cat");

        });
        binding.uploadTopBrands.setOnClickListener(view -> {
            uploadTopBrandsDialog("brands");
        });
        binding.uploadExpertTipsUrl.setOnClickListener(view -> {
            showUploadBannerDialog("tips");

        });
        binding.uploadWebsiteUrl.setOnClickListener(view -> {
            showUploadBannerDialog("site");

        });
        binding.showCategory.setOnClickListener(view -> {
            intent = new Intent(this, ShowCategoryActivity.class);
            intent.putExtra("key", "cat");
            startActivity(intent);
        });
        binding.showBrands.setOnClickListener(view -> {
            intent = new Intent(this, ShowBrandsActivity.class);
            intent.putExtra("key", "Top Brands");
            startActivity(intent);
        });
        binding.showTrending.setOnClickListener(view -> {
            intent = new Intent(this, ShowProducts.class);
            intent.putExtra("key", "trending");
            startActivity(intent);
        });
        binding.showLatest.setOnClickListener(view -> {
            intent = new Intent(this, ShowProducts.class);
            intent.putExtra("key", "latest");
            startActivity(intent);
        });
        binding.showBest.setOnClickListener(view -> {
            intent = new Intent(this, ShowProducts.class);
            intent.putExtra("key", "best");
            startActivity(intent);
        });
        binding.updateAds.setOnClickListener(view -> {

            showUpdateAdsDialog("Product Review");
        });

        apiInterface = ApiWebServices.getApiInterface();
        launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), result -> {
            if (result != null) {
                try {
                    if (logo) {
                        logo = false;
                        final InputStream inputStream = getContentResolver().openInputStream(result);
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        topBrandLayoutBinding.logo.setImageBitmap(bitmap);
                        encodedImage = imageStore(bitmap);
                    } else if (banner) {
                        banner = false;
                        final InputStream inputStream = getContentResolver().openInputStream(result);
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        topBrandLayoutBinding.banner.setImageBitmap(bitmap);
                        encodedImage2 = imageStore(bitmap);
                    } else {
                        final InputStream inputStream = getContentResolver().openInputStream(result);
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        uploadBannerLayoutBinding.selectImage.setImageBitmap(bitmap);
                        encodedImage = imageStore(bitmap);
                    }


                    Log.d("ContentValue", encodedImage + " @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@/n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@/n" + encodedImage2);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


        });

    }

    private void showUpdateAdsDialog(String id) {

        adsUpdateDialog = new Dialog(this);
        adsUpdateLayoutBinding = AdsUpdateLayoutBinding.inflate(getLayoutInflater());
        adsUpdateDialog.setContentView(adsUpdateLayoutBinding.getRoot());
        adsUpdateDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        adsUpdateDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.item_bg));
        adsUpdateDialog.setCancelable(false);
        adsUpdateDialog.show();


        adsUpdateLayoutBinding.cancelId.setOnClickListener(v -> adsUpdateDialog.dismiss());
        BannerTopNetworkName = adsUpdateLayoutBinding.bannerTopNetworkName;
        BannerBottomNetworkName = adsUpdateLayoutBinding.bannerBottomNetworkName;
        InterstitialNetwork = adsUpdateLayoutBinding.interstitialNetwork;
        NativeAdsNetworkName = adsUpdateLayoutBinding.nativeAdsNetworkName;
        RewardAdsNetwork = adsUpdateLayoutBinding.rewardAdsNetwork;
        UploadAdsBtn = adsUpdateLayoutBinding.uploadIds;

        AppId = adsUpdateLayoutBinding.appId;
        AppLovinSdkKey = adsUpdateLayoutBinding.appLovinSdkKey;
        BannerTop = adsUpdateLayoutBinding.bannerTop;
        BannerBottom = adsUpdateLayoutBinding.bannerBottom;
        InterstitialAds = adsUpdateLayoutBinding.interstitialAds;
        NativeAds = adsUpdateLayoutBinding.nativeAds;
        nativeType = adsUpdateLayoutBinding.nativeAdsType;
        rewardAds = adsUpdateLayoutBinding.rewardAds;

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, items);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(MainActivity.this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, item2);
        nativeType.setAdapter(arrayAdapter2);
        BannerTopNetworkName.setAdapter(arrayAdapter);
        BannerBottomNetworkName.setAdapter(arrayAdapter);
        InterstitialNetwork.setAdapter(arrayAdapter);
        NativeAdsNetworkName.setAdapter(arrayAdapter);
        RewardAdsNetwork.setAdapter(arrayAdapter);

        apiInterface = ApiWebServices.getApiInterface();
        Call<List<AdsModel>> call = apiInterface.fetchAds(id);
        call.enqueue(new Callback<List<AdsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<AdsModel>> call, @NonNull Response<List<AdsModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        for (AdsModel ads : response.body()) {
                            AppId.setText(ads.getAppId());
                            AppLovinSdkKey.setText(ads.getAppLovinAppKey());
                            BannerTopNetworkName.setText(ads.getBannerTopAdNetwork());
                            BannerTop.setText(ads.getBannerTop());
                            BannerBottomNetworkName.setText(ads.getBannerBottomAdNetwork());
                            BannerBottom.setText(ads.getBannerBottom());
                            InterstitialNetwork.setText(ads.getInterstitalAdNetwork());
                            InterstitialAds.setText(ads.getInterstitial());
                            NativeAdsNetworkName.setText(ads.getNativeAdNetwork());
                            NativeAds.setText(ads.getNativeAd());
                            nativeType.setText(ads.getNativeType());
                            RewardAdsNetwork.setText(ads.getRewardAdNetwork());
                            rewardAds.setText(ads.getRewardAd());

                        }
                    }
                } else {
                    Log.e("adsError", response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<AdsModel>> call, @NonNull Throwable t) {
                Log.d("adsError", t.getMessage());
            }
        });


        UploadAdsBtn.setOnClickListener(view -> {
            appId = AppId.getText().toString().trim();
            appLovinSdkKey = AppLovinSdkKey.getText().toString().trim();
            bannerTopNetworkName = BannerTopNetworkName.getText().toString().trim();
            bannerTop = BannerTop.getText().toString().trim();
            bannerBottomNetworkName = BannerBottomNetworkName.getText().toString().trim();
            bannerBottom = BannerBottom.getText().toString().trim();
            interstitialNetwork = InterstitialNetwork.getText().toString().trim();
            interstitialAds = InterstitialAds.getText().toString().trim();
            nativeAdsNetworkName = NativeAdsNetworkName.getText().toString().trim();
            nativeAds = NativeAds.getText().toString().trim();
            nativeAdsType = nativeType.getText().toString().trim();
            rewardAdsNetwork = RewardAdsNetwork.getText().toString().trim();
            rewardAd = rewardAds.getText().toString().trim();

            if (TextUtils.isEmpty(appId)) {
                AppId.setError("App id is required");
                AppId.requestFocus();
                loading.dismiss();
            } else if (TextUtils.isEmpty(appLovinSdkKey)) {
                AppLovinSdkKey.setError("AppLovinSdkKey is required");
                AppLovinSdkKey.requestFocus();
                loading.dismiss();
            } else if (TextUtils.isEmpty(bannerTopNetworkName)) {
                BannerTopNetworkName.setError("BannerTopNetworkName is required");
                BannerTopNetworkName.requestFocus();
                loading.dismiss();
            } else if (TextUtils.isEmpty(bannerTop)) {
                BannerTop.setError("BannerTop is required");
                BannerTop.requestFocus();
                loading.dismiss();
            } else if (TextUtils.isEmpty(bannerBottomNetworkName)) {
                BannerBottomNetworkName.setError("BannerBottomNetworkName is required");
                BannerBottomNetworkName.requestFocus();
                loading.dismiss();
            } else if (TextUtils.isEmpty(bannerBottom)) {
                BannerBottom.setError("BannerBottom is required");
                BannerBottom.requestFocus();
                loading.dismiss();
            } else if (TextUtils.isEmpty(interstitialNetwork)) {
                InterstitialNetwork.setError("InterstitialNetwork is required");
                InterstitialNetwork.requestFocus();
                loading.dismiss();
            } else if (TextUtils.isEmpty(interstitialAds)) {
                InterstitialAds.setError("InterstitialAds is required");
                InterstitialAds.requestFocus();
                loading.dismiss();
            } else if (TextUtils.isEmpty(nativeAdsNetworkName)) {
                NativeAdsNetworkName.setError("NativeAdsNetworkName is required");
                NativeAdsNetworkName.requestFocus();
                loading.dismiss();
            } else if (TextUtils.isEmpty(nativeAds)) {
                NativeAds.setError("NativeAds is required");
                NativeAds.requestFocus();
                loading.dismiss();
            } else if (TextUtils.isEmpty(nativeAdsType)) {
                nativeType.setError("NativeType is required");
                nativeType.requestFocus();
                loading.dismiss();
            } else if (TextUtils.isEmpty(rewardAdsNetwork)) {
                RewardAdsNetwork.setError("rewardAdsNetwork is required");
                RewardAdsNetwork.requestFocus();
                loading.dismiss();
            } else if (TextUtils.isEmpty(rewardAd)) {
                rewardAds.setError("rewardAd is required");
                rewardAds.requestFocus();
                loading.dismiss();
            } else {
                loadingDialog.show();
                map.put("id", id);
                map.put("appId", appId);
                map.put("appLovinSdkKey", appLovinSdkKey);
                map.put("bannerTop", bannerTop);
                map.put("bannerTopNetworkName", bannerTopNetworkName);
                map.put("bannerBottom", bannerBottom);
                map.put("bannerBottomNetworkName", bannerBottomNetworkName);
                map.put("interstitialAds", interstitialAds);
                map.put("interstitialNetwork", interstitialNetwork);
                map.put("nativeAds", nativeAds);
                map.put("nativeAdsNetworkName", nativeAdsNetworkName);
                map.put("nativeType", nativeAdsType);
                map.put("rewardAd", rewardAd);
                map.put("rewardAdNetwork", rewardAdsNetwork);
                updateAdIds(map);
            }

        });

    }

    private void updateAdIds(Map<String, String> map) {
        Call<MessageModel> call = apiInterface.updateAdIds(map);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
                adsUpdateDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private void uploadTopBrandsDialog(String key) {

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

        if (key.equals("brands")) {

            topBrandLayoutBinding.title.setText("Upload Brands");
            topBrandLayoutBinding.desc.setVisibility(View.VISIBLE);
            topBrandLayoutBinding.url.setVisibility(View.VISIBLE);
        } else {

            topBrandLayoutBinding.title.setText("Upload Category");
            topBrandLayoutBinding.title.setText("Upload Brands");
            topBrandLayoutBinding.desc.setVisibility(View.GONE);
            topBrandLayoutBinding.url.setVisibility(View.GONE);
            topBrandLayoutBinding.logo.setVisibility(View.GONE);

        }


        topBrandLayoutBinding.okBtn.setOnClickListener(view -> {
            loadingDialog.show();
            String tittle = topBrandLayoutBinding.titleTv.getText().toString().trim();
            if (key.equals("brands")) {
                String desc = topBrandLayoutBinding.desc.getText().toString().trim();
                String imgUrl = topBrandLayoutBinding.url.getText().toString().trim();

                if (encodedImage == null) {
                    loadingDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Please Select an Image", Toast.LENGTH_SHORT).show();
                } else if (encodedImage2 == null) {
                    loadingDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Please Select an Image", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(tittle)) {
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
                    map.put("logo", encodedImage);
                    map.put("banner", encodedImage2);
                    map.put("title", tittle);
                    map.put("desc", desc);
                    map.put("url", imgUrl);
                    call = apiInterface.uploadTopBrands(map);
                    uploadBanners(call, topBrandLayoutDialog);

                }
            } else if (key.equals("cat")) {
                if (encodedImage2 == null) {
                    loadingDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Please Select an Image", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(tittle)) {
                    topBrandLayoutBinding.titleTv.setError("Url Required");
                    topBrandLayoutBinding.titleTv.requestFocus();
                    loadingDialog.dismiss();
                } else {
                    map.put("banner", encodedImage2);
                    map.put("title", tittle);
                    map.put("parentId", "0");
                    map.put("subCat", "false");
                    map.put("product", "false");
                    call = apiInterface.uploadCategory(map);
                    uploadBanners(call, topBrandLayoutDialog);

                }
            }
        });

    }


    private void showUploadBannerDialog(String key) {
        uploadBannerDialog = new Dialog(this);
        uploadBannerLayoutBinding = UploadBannerLayoutBinding.inflate(getLayoutInflater());
        uploadBannerDialog.setContentView(uploadBannerLayoutBinding.getRoot());
        uploadBannerDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        uploadBannerDialog.setCancelable(false);
        uploadBannerDialog.show();

        switch (key) {
            case "banner":
                uploadBannerLayoutBinding.selectImage.setVisibility(View.VISIBLE);
                break;
            case "tips": {
                Call<UrlModel> callUrl = apiInterface.fetchUrls("tips");
                callUrl.enqueue(new Callback<UrlModel>() {
                    @Override
                    public void onResponse(@NonNull Call<UrlModel> call, @NonNull Response<UrlModel> response) {

                        if (response.isSuccessful()) {
                            urlId = Objects.requireNonNull(response.body()).getId();
                            urls = response.body().getUrl();
                            uploadBannerLayoutBinding.url.setText(urls);

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UrlModel> call, @NonNull Throwable t) {

                    }
                });

                uploadBannerLayoutBinding.selectImage.setVisibility(View.GONE);
                break;
            }
            case "site": {
                Call<UrlModel> callUrl = apiInterface.fetchUrls("site");
                callUrl.enqueue(new Callback<UrlModel>() {
                    @Override
                    public void onResponse(@NonNull Call<UrlModel> call, @NonNull Response<UrlModel> response) {

                        if (response.isSuccessful()) {
                            urlId = Objects.requireNonNull(response.body()).getId();
                            urls = response.body().getUrl();
                            uploadBannerLayoutBinding.url.setText(urls);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UrlModel> call, @NonNull Throwable t) {

                    }
                });

                uploadBannerLayoutBinding.selectImage.setVisibility(View.GONE);
                break;
            }
        }

        uploadBannerLayoutBinding.backBtn.setOnClickListener(view -> uploadBannerDialog.dismiss());
        uploadBannerLayoutBinding.selectImage.setOnClickListener(view -> launcher.launch("image/*"));


        uploadBannerLayoutBinding.okBtn.setOnClickListener(view -> {
            loadingDialog.show();
            String url = uploadBannerLayoutBinding.url.getText().toString().trim();
            if (key.equals("banner")) {
                if (encodedImage == null) {
                    loadingDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Please Select an Image", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(url)) {
                    uploadBannerLayoutBinding.url.setError("Url Required");
                    uploadBannerLayoutBinding.url.requestFocus();
                    loadingDialog.dismiss();
                } else {
                    map.put("img", encodedImage);
                    map.put("url", url);
                    call = apiInterface.uploadBanners(map);
                    uploadBanners(call, uploadBannerDialog);

                }
            } else if (key.equals("tips") || key.equals("site")) {

                if (TextUtils.isEmpty(url)) {
                    uploadBannerLayoutBinding.url.setError("Url Required");
                    uploadBannerLayoutBinding.url.requestFocus();
                    loadingDialog.dismiss();
                } else {
                    map.put("id", urlId);
                    map.put("url", url);
                    call = apiInterface.updateUrls(map);
                    uploadBanners(call, uploadBannerDialog);

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

    private void uploadBanners(Call<MessageModel> call, Dialog uploadDialog) {
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, Objects.requireNonNull(response.body()).getMessage(), Toast.LENGTH_SHORT).show();
                    uploadDialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, Objects.requireNonNull(response.body()).getError(), Toast.LENGTH_SHORT).show();
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