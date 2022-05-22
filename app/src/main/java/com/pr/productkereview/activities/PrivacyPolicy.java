package com.pr.productkereview.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.pr.productkereview.databinding.ActivityPrivacyPolicyBinding;

public class PrivacyPolicy extends AppCompatActivity {
    ActivityPrivacyPolicyBinding binding;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (key.equals("policy")) {
            binding.webView.loadUrl("file:///android_asset/privacy_policy.html");
        }


    }
}