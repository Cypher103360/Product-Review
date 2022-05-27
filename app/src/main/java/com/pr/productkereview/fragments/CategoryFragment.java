package com.pr.productkereview.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pr.productkereview.activities.ShowAllItemsActivity;
import com.pr.productkereview.activities.SubCategoryActivity;
import com.pr.productkereview.adapters.categories.CategoryAdapter;
import com.pr.productkereview.adapters.categories.CategoryInterface;
import com.pr.productkereview.databinding.FragmentCategoryBinding;
import com.pr.productkereview.models.categories.CatModel;
import com.pr.productkereview.models.categories.CatModelFactory;
import com.pr.productkereview.models.categories.CatViewModel;
import com.pr.productkereview.utils.ApiInterface;
import com.pr.productkereview.utils.ApiWebServices;
import com.pr.productkereview.utils.CommonMethods;
import com.pr.productkereview.utils.Prevalent;
import com.pr.productkereview.utils.ShowAds;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class CategoryFragment extends Fragment implements CategoryInterface {
    FragmentCategoryBinding binding;
    RecyclerView categoryRecyclerview;
    ApiInterface apiInterface;
    Dialog loading;
    CategoryAdapter categoryAdapter;
    CatViewModel catViewModel;
    List<CatModel> catModelList = new ArrayList<>();

    ShowAds showAds = new ShowAds();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater, container, false);
        apiInterface = ApiWebServices.getApiInterface();
        loading = CommonMethods.getLoadingDialog(requireActivity());
        loading.show();


        getLifecycle().addObserver(showAds);
        catViewModel = new ViewModelProvider(requireActivity(),
                new CatModelFactory(requireActivity().getApplication(), "0")).get(CatViewModel.class);

        categoryRecyclerview = binding.categoryRecyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        categoryRecyclerview.setLayoutManager(layoutManager);
        categoryAdapter = new CategoryAdapter(requireActivity(), this);
        categoryRecyclerview.setAdapter(categoryAdapter);
        categoryRecyclerview.setNestedScrollingEnabled(false);

        binding.refreshLayout.setOnRefreshListener(() -> {
            setCategories();
            binding.refreshLayout.setRefreshing(false);
        });
        setCategories();


        return binding.getRoot();
    }

    private void setCategories() {
        catViewModel.getCategories().observe(requireActivity(), catModels -> {
            if (!catModels.isEmpty()) {
                catModelList.clear();
                catModelList.addAll(catModels);
                categoryAdapter.updateCategoryList(catModelList);
                loading.dismiss();

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
        });
    }

    @Override
    public void onCategoryClicked(CatModel catModel) {
        showAds.destroyBanner();
        showAds.showInterstitialAds(requireActivity());
        if (catModel.getSubCat().equals("true")) {
            Intent intent = new Intent(requireActivity(), SubCategoryActivity.class);
            intent.putExtra("id", catModel.getId());
            intent.putExtra("title", catModel.getTitle());
            startActivity(intent);
        } else if (catModel.getProduct().equals("true")) {
            Intent intent = new Intent(requireActivity(), ShowAllItemsActivity.class);
            intent.putExtra("key", "Products");
            intent.putExtra("id", catModel.getId());
            startActivity(intent);
        }else {
            Toast.makeText(requireActivity(), "No Data Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        loading.show();
        setCategories();
    }
}