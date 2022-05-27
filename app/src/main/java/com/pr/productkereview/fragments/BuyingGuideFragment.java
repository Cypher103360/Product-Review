package com.pr.productkereview.fragments;

import static com.pr.productkereview.activities.ItemDetailsActivity.productModel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.pr.productkereview.R;
import com.pr.productkereview.databinding.FragmentBuyingGuideBinding;
import com.pr.productkereview.utils.Prevalent;
import com.pr.productkereview.utils.ShowAds;

import io.paperdb.Paper;

public class BuyingGuideFragment extends Fragment {
    FragmentBuyingGuideBinding binding;
    MaterialButtonToggleGroup materialButtonToggleGroup;
    String key;
    String desc, plainText;
    Spanned spanned;
    char[] chars;

    ShowAds showAds = new ShowAds();


    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBuyingGuideBinding.inflate(inflater, container, false);
        materialButtonToggleGroup = binding.materialButtonToggleGroup;
        key = requireActivity().getIntent().getStringExtra("key");
        setShowAds();


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

        return binding.getRoot();
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