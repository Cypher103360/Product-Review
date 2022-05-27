package com.pr.productkereview.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.pr.productkereview.R;
import com.pr.productkereview.databinding.ActivityPrivacyPolicyBinding;
import com.pr.productkereview.utils.ShowAds;

public class PrivacyPolicy extends AppCompatActivity {
    ActivityPrivacyPolicyBinding binding;
    String key;
    ShowAds showAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        key = getIntent().getStringExtra("key");

        if (key.equals("policy")) {
            binding.webView.loadUrl("file:///android_asset/privacy_policy.html");
        }

        showAds = new ShowAds();
        getLifecycle().addObserver(showAds);
        showAds.showBottomBanner(this, findViewById(R.id.adView_bottom));
        showAds.showTopBanner(this, findViewById(R.id.adView_top));


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showAds.destroyBanner();
    }
}