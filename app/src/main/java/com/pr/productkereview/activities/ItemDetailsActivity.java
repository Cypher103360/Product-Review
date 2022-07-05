package com.pr.productkereview.activities;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ironsource.mediationsdk.IronSource;
import com.pr.productkereview.adapters.BuyingRatingPagerAdapter;
import com.pr.productkereview.databinding.ActivityItemDetailsBinding;
import com.pr.productkereview.fragments.BuyingGuideFragment;
import com.pr.productkereview.fragments.RatingsFragment;
import com.pr.productkereview.models.AllProducts.ProductModel;
import com.pr.productkereview.models.UrlsModels.UrlModel;
import com.pr.productkereview.utils.ApiInterface;
import com.pr.productkereview.utils.ApiWebServices;
import com.pr.productkereview.utils.CommonMethods;
import com.pr.productkereview.utils.Prevalent;
import com.pr.productkereview.utils.ShowAds;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDetailsActivity extends AppCompatActivity {
    public static ProductModel productModel;
    ActivityItemDetailsBinding binding;
    BuyingRatingPagerAdapter pagerAdapter;
    ShowAds showAds = new ShowAds();
    ApiInterface apiInterface;
    String whatsappText;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiInterface = ApiWebServices.getApiInterface();
        binding.backIcon.setOnClickListener(v -> onBackPressed());
        fetchWhatsappText("whatsapp");

        getLifecycle().addObserver(showAds);
        Bundle bundle = getIntent().getExtras();
        productModel = (ProductModel) bundle.getSerializable("latest");
       // binding.activityTitle.setText(HtmlCompat.fromHtml(productModel.getProductTitle(), HtmlCompat.FROM_HTML_MODE_LEGACY));

//        binding.lottieContact.setOnClickListener(v -> {
//            try {
//                CommonMethods.whatsApp(ItemDetailsActivity.this,whatsappText);
//            } catch (UnsupportedEncodingException | PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            }
//        });


        pagerAdapter = new BuyingRatingPagerAdapter(getSupportFragmentManager(),
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragments(new BuyingGuideFragment(), "Reviews");
//        pagerAdapter.addFragments(new RatingsFragment(), "Ratings & Brands");

        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        tabs.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        showAds.destroyBanner();
    }

    public void fetchWhatsappText(String whatsapp) {
        Call<UrlModel> call = apiInterface.getUrls(whatsapp);
        call.enqueue(new Callback<UrlModel>() {
            @Override
            public void onResponse(@NonNull Call<UrlModel> call, @NonNull Response<UrlModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    whatsappText = decodeEmoji(response.body().getUrl());
                    //Log.d("urls",weburl);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UrlModel> call, @NonNull Throwable t) {

            }
        });
    }
    public static String decodeEmoji(String message) {
        try {
            return URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
        showAds.destroyBanner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);

        if (Objects.requireNonNull(Paper.book().read(Prevalent.bannerTopNetworkName)).equals("IronSourceWithMeta")) {
            showAds.showTopBanner(this, binding.adViewTop);

        } else if (Objects.requireNonNull(Paper.book().read(Prevalent.bannerBottomNetworkName)).equals("IronSourceWithMeta")) {
            showAds.showBottomBanner(this, binding.adViewBottom);
        }
    }

}