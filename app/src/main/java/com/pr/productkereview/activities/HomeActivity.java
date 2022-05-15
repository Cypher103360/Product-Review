package com.pr.productkereview.activities;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pr.productkereview.R;
import com.pr.productkereview.activities.ui.main.SectionsPagerAdapter;
import com.pr.productkereview.databinding.ActivityHomeBinding;
import com.pr.productkereview.fragments.HomeFragment;
import com.pr.productkereview.fragments.TopProductFragment;
import com.pr.productkereview.utils.CommonMethods;

import java.io.UnsupportedEncodingException;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    SectionsPagerAdapter sectionsPagerAdapter;
    Bundle bundle;
    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bundle = new Bundle();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);


        // Setting Version Code
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String version = pInfo.versionName;
            binding.versionCode.setText(getString(R.string.version,version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        binding.lottieContact.setOnClickListener(view -> {
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Contact Home Top");
            mFirebaseAnalytics.logEvent("Clicked_On_Contact_Home_Top", bundle);

            try {
                CommonMethods.whatsApp(HomeActivity.this);
            } catch (UnsupportedEncodingException | PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        });

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        sectionsPagerAdapter.addFragments(new HomeFragment(),"Home");
        sectionsPagerAdapter.addFragments(new TopProductFragment(),"Top Products");
        sectionsPagerAdapter.addFragments(new HomeFragment(),"Categories");
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

    }
}