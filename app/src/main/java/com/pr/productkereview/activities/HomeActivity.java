package com.pr.productkereview.activities;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ironsource.mediationsdk.IronSource;
import com.pr.productkereview.R;
import com.pr.productkereview.activities.ui.main.SectionsPagerAdapter;
import com.pr.productkereview.databinding.ActivityHomeBinding;
import com.pr.productkereview.fragments.CategoryFragment;
import com.pr.productkereview.fragments.HomeFragment;
import com.pr.productkereview.fragments.TrendingProductFragment;
import com.pr.productkereview.models.UrlsModels.UrlModel;
import com.pr.productkereview.utils.ApiInterface;
import com.pr.productkereview.utils.ApiWebServices;
import com.pr.productkereview.utils.CommonMethods;
import com.pr.productkereview.utils.MyReceiver;
import com.pr.productkereview.utils.Prevalent;
import com.pr.productkereview.utils.ShowAds;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Objects;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static final String BroadCastStringForAction = "checkingInternet";
    private static final float END_SCALE = 0.7f;
    int count = 1;
    ImageView navMenu;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ConstraintLayout categoryContainer;
    //    GoogleSignInOptions gso;
//    GoogleSignInClient gsc;
    String weburl, webUrlId, shareText, whatsappText, adsFreeText;
    SectionsPagerAdapter sectionsPagerAdapter;
    ActivityHomeBinding binding;
    ApiInterface apiInterface;
    ShowAds showAds = new ShowAds();
    Bundle bundle;
    Dialog loading;
    SharedPreferences preferences;
    String action;

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadCastStringForAction)) {
                if (intent.getStringExtra("online_status").equals("true")) {

                    Set_Visibility_ON();
                    count++;
                } else {
                    Set_Visibility_OFF();
                }
            }
        }
    };
    FirebaseAnalytics mFirebaseAnalytics;
    private IntentFilter intentFilter;

    public static String decodeEmoji(String message) {
        try {
            return URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

    private void Set_Visibility_ON() {
        binding.lottieHomeNoInternet.setVisibility(View.GONE);
        binding.tvNotConnected.setVisibility(View.GONE);
        binding.viewPager.setVisibility(View.VISIBLE);
        binding.tabs.setVisibility(View.VISIBLE);
        enableNavItems();
        fetchUrls("tips");
        fetchShareText("share");
        fetchWhatsappText("whatsapp");
        fetchAdsText("ads_free");
        if (count == 2) {
            ViewPager viewPager = binding.viewPager;
            viewPager.setAdapter(sectionsPagerAdapter);
            TabLayout tabs = binding.tabs;
            tabs.setupWithViewPager(viewPager);
            navigationDrawer();
            if (action != null) {
                Log.d("ContentValueForPref", action);
                switch (action) {
                    case "home":
                        binding.viewPager.setCurrentItem(0);
                        action = null;
                        break;
                    case "cat":
                        binding.viewPager.setCurrentItem(1);
                        action = null;

                        break;
                    case "tre":
                        binding.viewPager.setCurrentItem(2);
                        action = null;
                        break;
                    default:
                }

            }

        }
    }

    private void Set_Visibility_OFF() {
        binding.lottieHomeNoInternet.setVisibility(View.VISIBLE);
        binding.tvNotConnected.setVisibility(View.VISIBLE);
        binding.viewPager.setVisibility(View.GONE);
        binding.tabs.setVisibility(View.GONE);
        disableNavItems();
        loading.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        apiInterface = ApiWebServices.getApiInterface();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        action = getIntent().getStringExtra("action");

//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        gsc = GoogleSignIn.getClient(this, gso);
        loading = CommonMethods.getLoadingDialog(HomeActivity.this);
        navigationView = binding.navigation;
        navMenu = binding.navMenu;
        drawerLayout = binding.drawerLayout;
        bundle = new Bundle();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        getLifecycle().addObserver(showAds);

        // Setting Version Code
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            String version = pInfo.versionName;
            binding.versionCode.setText(getString(R.string.version, version));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //Internet Checking Condition
        intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, MyReceiver.class);
        startService(serviceIntent);
        if (isOnline(HomeActivity.this)) {
            Set_Visibility_ON();
        } else {
            Set_Visibility_OFF();
        }

        binding.lottieContact.setOnClickListener(view -> {
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Contact Home Top");
            mFirebaseAnalytics.logEvent("Clicked_On_Contact_Home_Top", bundle);

            try {
                CommonMethods.whatsApp(HomeActivity.this, whatsappText);
            } catch (UnsupportedEncodingException | PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        });

        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        sectionsPagerAdapter.addFragments(new HomeFragment(), "Home");
        sectionsPagerAdapter.addFragments(new CategoryFragment(), "Category");
        sectionsPagerAdapter.addFragments(new TrendingProductFragment(), "Trending");

    }

    public void fetchUrls(String tips) {
        Call<UrlModel> call = apiInterface.getUrls(tips);
        call.enqueue(new Callback<UrlModel>() {
            @Override
            public void onResponse(@NonNull Call<UrlModel> call, @NonNull Response<UrlModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    webUrlId = response.body().getId();
                    weburl = decodeEmoji(response.body().getUrl());
                    //Log.d("urls",weburl);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UrlModel> call, @NonNull Throwable t) {

            }
        });
    }

    public void fetchShareText(String share) {
        Call<UrlModel> call = apiInterface.getUrls(share);
        call.enqueue(new Callback<UrlModel>() {
            @Override
            public void onResponse(@NonNull Call<UrlModel> call, @NonNull Response<UrlModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    shareText = decodeEmoji(response.body().getUrl());
                    //Log.d("urls",weburl);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UrlModel> call, @NonNull Throwable t) {

            }
        });
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


    public void fetchAdsText(String adsFree) {
        ApiInterface apiInterface = ApiWebServices.getApiInterface();
        Call<UrlModel> call = apiInterface.getUrls(adsFree);
        call.enqueue(new Callback<UrlModel>() {
            @Override
            public void onResponse(@NonNull Call<UrlModel> call, @NonNull Response<UrlModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    adsFreeText = decodeEmoji(response.body().getUrl());
                    //Log.d("urls",weburl);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UrlModel> call, @NonNull Throwable t) {

            }
        });
    }

    public void navigationDrawer() {
        navigationView = findViewById(R.id.navigation);
        navigationView.bringToFront();
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                HomeActivity.this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(HomeActivity.this);
        navigationView.setCheckedItem(R.id.nav_home);
        categoryContainer = findViewById(R.id.container_layout);

        navMenu.setOnClickListener(view -> {
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawerLayout.setScrimColor(getColor(R.color.bg_color));
        }
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                categoryContainer.setScaleX(offsetScale);
                categoryContainer.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = categoryContainer.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                categoryContainer.setTranslationX(xTranslation);
            }
        });
    }

    public boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public void disableNavItems() {
        Menu navMenu = navigationView.getMenu();

        MenuItem nav_home = navMenu.findItem(R.id.nav_home);
        nav_home.setEnabled(false);

        MenuItem nav_expert_tips = navMenu.findItem(R.id.nav_expert_tips);
        nav_expert_tips.setEnabled(false);

        MenuItem nav_most_selling = navMenu.findItem(R.id.nav_most_selling);
        nav_most_selling.setEnabled(false);

        MenuItem nav_share = navMenu.findItem(R.id.nav_share);
        nav_share.setEnabled(false);

        MenuItem nav_rate = navMenu.findItem(R.id.nav_rate);
        nav_rate.setEnabled(false);

        MenuItem nav_contact = navMenu.findItem(R.id.nav_contact);
        nav_contact.setEnabled(false);

        MenuItem nav_favorite = navMenu.findItem(R.id.nav_contact);
        nav_favorite.setEnabled(false);

        MenuItem nav_policy = navMenu.findItem(R.id.nav_privacy);
        nav_policy.setEnabled(false);

        MenuItem nav_disclaimer = navMenu.findItem(R.id.nav_disclaimer);
        nav_disclaimer.setEnabled(false);

        MenuItem nav_ads_free = navMenu.findItem(R.id.nav_ads_free);
        nav_ads_free.setEnabled(false);
    }

    public void enableNavItems() {
        Menu navMenu = navigationView.getMenu();

        MenuItem nav_home = navMenu.findItem(R.id.nav_home);
        nav_home.setEnabled(true);

        MenuItem nav_expert_tips = navMenu.findItem(R.id.nav_expert_tips);
        nav_expert_tips.setEnabled(true);

        MenuItem nav_most_selling = navMenu.findItem(R.id.nav_most_selling);
        nav_most_selling.setEnabled(true);

        MenuItem nav_share = navMenu.findItem(R.id.nav_share);
        nav_share.setEnabled(true);

        MenuItem nav_rate = navMenu.findItem(R.id.nav_rate);
        nav_rate.setEnabled(true);

        MenuItem nav_contact = navMenu.findItem(R.id.nav_contact);
        nav_contact.setEnabled(true);

        MenuItem nav_favorite = navMenu.findItem(R.id.nav_contact);
        nav_favorite.setEnabled(true);

        MenuItem nav_policy = navMenu.findItem(R.id.nav_privacy);
        nav_policy.setEnabled(true);

        MenuItem nav_disclaimer = navMenu.findItem(R.id.nav_disclaimer);
        nav_disclaimer.setEnabled(true);

        MenuItem nav_ads_free = navMenu.findItem(R.id.nav_ads_free);
        nav_ads_free.setEnabled(true);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(0, 0);
                finish();
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Home Menu");
                mFirebaseAnalytics.logEvent("Clicked_On_Home_Menu", bundle);

                break;

            case R.id.nav_expert_tips:
                openWebPage(weburl, HomeActivity.this);
                break;

            case R.id.nav_most_selling:
                showAds.destroyBanner();
                showAds.showInterstitialAds(HomeActivity.this);
                Intent mostSellingIntent = new Intent(HomeActivity.this, ShowAllItemsActivity.class);
                mostSellingIntent.putExtra("key", "mostSelling");
                startActivity(mostSellingIntent);
                break;

            case R.id.nav_share:
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Share Menu");
                mFirebaseAnalytics.logEvent("Clicked_On_Share_Menu", bundle);
                CommonMethods.shareApp(HomeActivity.this, shareText);
                break;

            case R.id.nav_rate:
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Rate Menu");
                mFirebaseAnalytics.logEvent("Clicked_On_Rate_Menu", bundle);
                CommonMethods.rateApp(HomeActivity.this);
                break;

            case R.id.nav_ads_free:
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Ads Free");
                mFirebaseAnalytics.logEvent("Clicked_On_ads_free", bundle);
//                try {
//                    CommonMethods.whatsApp(HomeActivity.this, adsFreeText);
//                } catch (UnsupportedEncodingException | PackageManager.NameNotFoundException e) {
//                    e.printStackTrace();
//                }
                openWebPage(adsFreeText, HomeActivity.this);
                break;

            case R.id.nav_contact:
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Contact Menu");
                mFirebaseAnalytics.logEvent("Clicked_On_Contact_Menu", bundle);
                CommonMethods.contactUs(HomeActivity.this);
                break;

            case R.id.nav_privacy:
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Privacy Menu");
                mFirebaseAnalytics.logEvent("Clicked_On_Privacy_Menu", bundle);
                showAds.destroyBanner();
                showAds.showInterstitialAds(HomeActivity.this);
                Intent intent = new Intent(HomeActivity.this, PrivacyPolicy.class);
                intent.putExtra("key", "policy");
                startActivity(intent);
                break;

            case R.id.nav_disclaimer:
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Disclaimer Menu");
                mFirebaseAnalytics.logEvent("Clicked_On_Disclaimer_Menu", bundle);
                disclaimerDialog();
                break;
            default:
        }
        return true;
    }


    public void disclaimerDialog() {
        Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.disclaimer_layout);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(true);
        dialog.show();
    }

//    public void googleSignOut() {
//        gsc.signOut().addOnCompleteListener(task -> {
//            finish();
//            loading.dismiss();
//            startActivity(new Intent(HomeActivity.this, SignupActivity.class));
//        });
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
        showAds.destroyBanner();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
        registerReceiver(receiver, intentFilter);

        if (Objects.requireNonNull(Paper.book().read(Prevalent.bannerTopNetworkName)).equals("IronSourceWithMeta")) {
            showAds.showTopBanner(this, binding.adViewTop);

        } else if (Objects.requireNonNull(Paper.book().read(Prevalent.bannerBottomNetworkName)).equals("IronSourceWithMeta")) {
            showAds.showBottomBanner(this, binding.adViewBottom);
        }
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

        if (preferences.getString("action", "").equals("")) {
            super.onBackPressed();
            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
                Intent intent = new Intent(HomeActivity.this, WelcomeActivity.class);
                startActivity(intent);
                preferences.edit().clear().apply();
                overridePendingTransition(0, 0);
                finish();
                overridePendingTransition(0, 0);

                showAds.destroyBanner();
            }
        } else {

            if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                startActivity(new Intent(HomeActivity.this, WelcomeActivity.class));
                super.onBackPressed();
                showAds.destroyBanner();
                if (Objects.equals(Paper.book().read(Prevalent.interstitialNetwork), "AdmobWithMeta"))
                    showAds.showInterstitialAds(this);

            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showAds.destroyBanner();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}