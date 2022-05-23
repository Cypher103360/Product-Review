package com.pr.productkereview.activities;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pr.productkereview.adapters.BuyingRatingPagerAdapter;
import com.pr.productkereview.databinding.ActivityItemDetailsBinding;
import com.pr.productkereview.fragments.BuyingGuideFragment;
import com.pr.productkereview.fragments.RatingsFragment;
import com.pr.productkereview.models.AllProducts.ProductModel;
import com.pr.productkereview.utils.CommonMethods;

import java.io.UnsupportedEncodingException;

public class ItemDetailsActivity extends AppCompatActivity {
    public static ProductModel productModel;
    ActivityItemDetailsBinding binding;
    BuyingRatingPagerAdapter pagerAdapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.backIcon.setOnClickListener(v -> onBackPressed());

        Bundle bundle = getIntent().getExtras();
        productModel = (ProductModel) bundle.getSerializable("latest");
        binding.activityTitle.setText(Html.fromHtml(productModel.getProductTitle(),Html.FROM_HTML_MODE_LEGACY));

        binding.lottieContact.setOnClickListener(v -> {
            try {
                CommonMethods.whatsApp(ItemDetailsActivity.this);
            } catch (UnsupportedEncodingException | PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        });


        pagerAdapter = new BuyingRatingPagerAdapter(getSupportFragmentManager(),
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pagerAdapter.addFragments(new BuyingGuideFragment(), "Buying Guides");
        pagerAdapter.addFragments(new RatingsFragment(), "Ratings & Brands");

        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}