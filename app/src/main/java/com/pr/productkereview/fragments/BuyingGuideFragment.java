package com.pr.productkereview.fragments;

import static com.pr.productkereview.activities.ItemDetailsActivity.productModel;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.pr.productkereview.R;
import com.pr.productkereview.databinding.FragmentBuyingGuideBinding;
import com.pr.productkereview.models.AllProducts.ProductModel;
import com.pr.productkereview.utils.ApiWebServices;
import com.pr.productkereview.utils.CommonMethods;

public class BuyingGuideFragment extends Fragment {
    FragmentBuyingGuideBinding binding;
    MaterialButtonToggleGroup materialButtonToggleGroup;
    String key;

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBuyingGuideBinding.inflate(inflater, container, false);
        materialButtonToggleGroup = binding.materialButtonToggleGroup;
        key = requireActivity().getIntent().getStringExtra("key");

        binding.buyingWebView.loadData(productModel.getBuingGuideEnglish(), "text/html", "UTF-8");
       // materialButtonToggleGroup.check(R.id.buyingEnglishPreview);
        materialButtonToggleGroup.addOnButtonCheckedListener((group, checkedId, isChecked) -> {
            if (isChecked){
                switch (checkedId){
                    case R.id.buyingEnglishPreview:

                        binding.buyingWebView.loadData(productModel.getBuingGuideEnglish(), "text/html", "UTF-8");
                        break;
                    case R.id.buyingHindiPreview:
                        binding.buyingWebView.loadData(productModel.getBuingGuideHindi(), "text/html", "UTF-8");
                        break;

                }
            }
        });

        return binding.getRoot();
    }
}