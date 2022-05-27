package com.pr.productkereview.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.bumptech.glide.Glide;
import com.ironsource.mediationsdk.IronSource;
import com.pr.productkereview.R;
import com.pr.productkereview.databinding.ActivityTopBrandDetailsBinding;
import com.pr.productkereview.utils.ApiWebServices;
import com.pr.productkereview.utils.CommonMethods;
import com.pr.productkereview.utils.ShowAds;

public class TopBrandDetailsActivity extends AppCompatActivity {
    ActivityTopBrandDetailsBinding binding;
    String title, desc, bannerImg, url;
    ShowAds showAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTopBrandDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bannerImg = getIntent().getStringExtra("img");
        title = getIntent().getStringExtra("title");
        desc = getIntent().getStringExtra("desc");
        url = getIntent().getStringExtra("url");
        binding.backIcon.setOnClickListener(v -> onBackPressed());
        binding.brandActivityTitle.setText(HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY));

        showAds = new ShowAds();
        getLifecycle().addObserver(showAds);

        Glide.with(this).load(
                        ApiWebServices.base_url + "top_brands_images/" + bannerImg)
                .placeholder(CommonMethods.setShimmer(TopBrandDetailsActivity.this))
                .into(binding.brandImage);

        binding.brandTitle.setText(HtmlCompat.fromHtml(title, HtmlCompat.FROM_HTML_MODE_LEGACY));
        binding.brandDesc.setText(HtmlCompat.fromHtml(desc.replaceAll("<.*?>", ""), HtmlCompat.FROM_HTML_MODE_LEGACY));
        binding.brandImage.setOnClickListener(v -> {
            openWebPage(url, TopBrandDetailsActivity.this);
        });

    }

    @SuppressLint("QueryPermissionsNeeded")
    public void openWebPage(String url, Context context) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        showAds.destroyBanner();
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
        showAds.showBottomBanner(this, findViewById(R.id.adView_bottom));
        showAds.showTopBanner(this, findViewById(R.id.adView_top));
    }
}