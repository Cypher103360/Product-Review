package com.pr.productkereview.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.pr.productkereview.R;
import com.pr.productkereview.databinding.ActivityTopBrandDetailsBinding;
import com.pr.productkereview.utils.ApiWebServices;
import com.pr.productkereview.utils.CommonMethods;

public class TopBrandDetailsActivity extends AppCompatActivity {
    ActivityTopBrandDetailsBinding binding;
    String title,desc,bannerImg,url;

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
        binding.brandActivityTitle.setText(title);
        Glide.with(this).load(
                ApiWebServices.base_url + "top_brands_images/"+bannerImg)
                .placeholder(CommonMethods.setShimmer(TopBrandDetailsActivity.this))
                .into(binding.brandImage);

        binding.brandTitle.setText(title);
        binding.brandDesc.setText(desc);
        binding.brandImage.setOnClickListener(v -> {
            openWebPage(url,TopBrandDetailsActivity.this);
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
    }
}