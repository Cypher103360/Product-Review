package com.pr.productkereview.fragments;

import static com.pr.productkereview.activities.ItemDetailsActivity.productModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pr.productkereview.R;
import com.pr.productkereview.databinding.FragmentBuyingGuideBinding;
import com.pr.productkereview.models.UrlsModels.UrlModel;
import com.pr.productkereview.utils.ApiInterface;
import com.pr.productkereview.utils.ApiWebServices;
import com.pr.productkereview.utils.CommonMethods;
import com.pr.productkereview.utils.Prevalent;
import com.pr.productkereview.utils.ShowAds;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyingGuideFragment extends Fragment {
    FragmentBuyingGuideBinding binding;
    MaterialButtonToggleGroup materialButtonToggleGroup;
    String key;
    String desc, plainText,shareText;
    ApiInterface apiInterface;
    Spanned spanned;
    Bundle bundle;
    FirebaseAnalytics mFirebaseAnalytics;
    char[] chars;
    public static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";

    ShowAds showAds = new ShowAds();


    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBuyingGuideBinding.inflate(inflater, container, false);
        materialButtonToggleGroup = binding.materialButtonToggleGroup;
        apiInterface = ApiWebServices.getApiInterface();
        bundle = new Bundle();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireActivity());
        key = requireActivity().getIntent().getStringExtra("key");
        setShowAds();
        fetchShareText("share");

        Glide.with(requireActivity()).load(ApiWebServices.base_url + "all_products_images/"
                        + productModel.getBanner())
                .placeholder(R.drawable.banner_picture)
                .into(binding.buyingBannerImg);

        binding.buyingTitle.setText(productModel.getProductTitle());
        desc = productModel.getBuingGuideEnglish().replaceAll("<.*?>", "");
        spanned = HtmlCompat.fromHtml(desc, HtmlCompat.FROM_HTML_MODE_LEGACY);
        chars = new char[spanned.length()];
        TextUtils.getChars(spanned, 0, spanned.length(), chars, 0);
        plainText = new String(chars);


        binding.buyingWebView.loadData(plainText, "text/html", "UTF-8");
        // materialButtonToggleGroup.check(R.id.buyingEnglishPreview);
        materialButtonToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked) {
                switch (checkedId) {
                    case R.id.buyingEnglishPreview:

                        desc = productModel.getBuingGuideEnglish().replaceAll("<.*?>", "");
                        spanned = HtmlCompat.fromHtml(desc, HtmlCompat.FROM_HTML_MODE_LEGACY);
                        chars = new char[spanned.length()];
                        TextUtils.getChars(spanned, 0, spanned.length(), chars, 0);
                        plainText = new String(chars);
                        binding.buyingWebView.loadData(plainText, "text/html", "UTF-8");
                        setShowAds();
                        break;
                    case R.id.buyingHindiPreview:
                        setShowAds();

                        desc = productModel.getBuingGuideHindi().replaceAll("<.*?>", "");
                        spanned = HtmlCompat.fromHtml(desc, HtmlCompat.FROM_HTML_MODE_LEGACY);
                        chars = new char[spanned.length()];
                        TextUtils.getChars(spanned, 0, spanned.length(), chars, 0);
                        plainText = new String(chars);

                        binding.buyingWebView.loadData(plainText, "text/html", "UTF-8");
                        break;

                }
            }
        });

        binding.whatsappShareBtn.setOnClickListener(v -> {
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Whatsapp Share Btn");
            mFirebaseAnalytics.logEvent("Clicked_On_Whatsapp_share_btn", bundle);
            CommonMethods.shareApp(requireActivity(),shareText);
        });


        Pattern p = Pattern.compile(URL_REGEX);
        if (productModel.getUrl()!=null) {
            Matcher m = p.matcher(productModel.getUrl());//replace with string to compare
            if (m.find()) {
                binding.readMoreBtn.setVisibility(View.VISIBLE);
                binding.readMoreBtn.setOnClickListener(v -> {
                    bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Read More Btn");
                    mFirebaseAnalytics.logEvent("Clicked_On_Read_more_Btn", bundle);
                    openWebPage(productModel.getUrl(), requireActivity());
                });
            } else {
                binding.readMoreBtn.setVisibility(View.GONE);
            }
        }



        return binding.getRoot();
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void openWebPage(String url, Context context) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivity(intent);
        }
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
    public static String decodeEmoji(String message) {
        try {
            return URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

    void setShowAds() {
        getLifecycle().addObserver(showAds);
        if (Paper.book().read(Prevalent.bannerTopNetworkName).equals("IronSourceWithMeta")) {
            binding.adViewTop.setVisibility(View.GONE);
            showAds.showBottomBanner(requireActivity(), binding.adViewBottom);

        } else if (Paper.book().read(Prevalent.bannerBottomNetworkName).equals("IronSourceWithMeta")) {
            binding.adViewBottom.setVisibility(View.GONE);
            showAds.showTopBanner(requireActivity(), binding.adViewTop);

        } else {
            showAds.showTopBanner(requireActivity(), binding.adViewTop);
            showAds.showBottomBanner(requireActivity(), binding.adViewBottom);
        }
    }

}