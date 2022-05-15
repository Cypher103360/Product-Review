package com.pr.productkereview.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.pr.productkereview.adapters.BestProductAdapter;
import com.pr.productkereview.adapters.BestProductClickInterface;
import com.pr.productkereview.adapters.BuyingGuidesAdapter;
import com.pr.productkereview.adapters.BuyingGuidesClickInterface;
import com.pr.productkereview.adapters.LatestProductAdapter;
import com.pr.productkereview.adapters.LatestProductClickInterface;
import com.pr.productkereview.adapters.TopBrandsAdapter;
import com.pr.productkereview.adapters.TopBrandsClickInterface;
import com.pr.productkereview.databinding.FragmentHomeBinding;
import com.pr.productkereview.models.BestProducts.BestProductModel;
import com.pr.productkereview.models.BuyingGuides.BuyingGuidesModel;
import com.pr.productkereview.models.LatestProduct.LatestProductModel;
import com.pr.productkereview.models.TopBrands.TopBrandsModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements LatestProductClickInterface, BestProductClickInterface, TopBrandsClickInterface, BuyingGuidesClickInterface {
    FragmentHomeBinding binding;
    ImageSlider imageSlider;
    List<SlideModel> slideModels = new ArrayList<>();
    RecyclerView latestProductRV, bestProductRV, topBrandsRV, buyingGuidesRV;

    List<LatestProductModel> latestProductModelList = new ArrayList<>();
    List<BestProductModel> bestProductModelList = new ArrayList<>();
    List<TopBrandsModel> topBrandsModelList = new ArrayList<>();
    List<BuyingGuidesModel> buyingGuidesModelList = new ArrayList<>();

    LatestProductAdapter latestProductAdapter;
    BestProductAdapter bestProductAdapter;
    TopBrandsAdapter topBrandsAdapter;
    BuyingGuidesAdapter buyingGuidesAdapter;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.latestProductLayout.setVisibility(View.VISIBLE);
        binding.bestProductLayout.setVisibility(View.VISIBLE);
        binding.topBrandProductLayout.setVisibility(View.VISIBLE);
        binding.buyingGuideLayout.setVisibility(View.VISIBLE);

        // LayoutManager
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(requireActivity());
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(requireActivity());
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(requireActivity());
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);

        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager4.setOrientation(LinearLayoutManager.VERTICAL);

        // setting layout manager
        binding.latestProductRecyclerview.setLayoutManager(layoutManager1);
        binding.bestProductRecyclerview.setLayoutManager(gridLayoutManager);
        binding.topBrandProductRecyclerview.setLayoutManager(layoutManager3);
        binding.buyingGuideRecyclerview.setLayoutManager(layoutManager4);

        // initializing adapter
        latestProductAdapter = new LatestProductAdapter(requireActivity(), this);
        bestProductAdapter = new BestProductAdapter(requireActivity(), this);
        topBrandsAdapter = new TopBrandsAdapter(requireActivity(), this);
        buyingGuidesAdapter = new BuyingGuidesAdapter(requireActivity(), this);

        // setting adapters
        binding.latestProductRecyclerview.setAdapter(latestProductAdapter);
        binding.bestProductRecyclerview.setAdapter(bestProductAdapter);
        binding.topBrandProductRecyclerview.setAdapter(topBrandsAdapter);
        binding.buyingGuideRecyclerview.setAdapter(buyingGuidesAdapter);

        latestProductModelList.clear();
        latestProductModelList.add(new LatestProductModel("id", "https://images.unsplash.com/photo-1542332213-9b5a5a3fad35?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Latest Products"));
        latestProductModelList.add(new LatestProductModel("id", "https://images.unsplash.com/photo-1542332213-9b5a5a3fad35?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Latest Products"));
        latestProductModelList.add(new LatestProductModel("id", "https://images.unsplash.com/photo-1542332213-9b5a5a3fad35?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Latest Products"));
        latestProductModelList.add(new LatestProductModel("id", "https://images.unsplash.com/photo-1542332213-9b5a5a3fad35?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Latest Products"));
        latestProductModelList.add(new LatestProductModel("id", "https://images.unsplash.com/photo-1542332213-9b5a5a3fad35?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Latest Products"));
        latestProductModelList.add(new LatestProductModel("id", "https://images.unsplash.com/photo-1542332213-9b5a5a3fad35?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Latest Products"));
        latestProductModelList.add(new LatestProductModel("id", "https://images.unsplash.com/photo-1542332213-9b5a5a3fad35?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Latest Products"));
        latestProductModelList.add(new LatestProductModel("id", "https://images.unsplash.com/photo-1542332213-9b5a5a3fad35?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Latest Products"));
        latestProductModelList.add(new LatestProductModel("id", "https://images.unsplash.com/photo-1542332213-9b5a5a3fad35?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Latest Products"));
        latestProductModelList.add(new LatestProductModel("id", "https://images.unsplash.com/photo-1542332213-9b5a5a3fad35?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Latest Products"));
        latestProductModelList.add(new LatestProductModel("id", "https://images.unsplash.com/photo-1542332213-9b5a5a3fad35?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Latest Products"));
        latestProductAdapter.updateList(latestProductModelList);

        bestProductModelList.clear();
        bestProductModelList.add(new BestProductModel("id", "https://images.unsplash.com/photo-1519114056088-b877fe073a5e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1033&q=80", "Best Products"));
        bestProductModelList.add(new BestProductModel("id", "https://images.unsplash.com/photo-1519114056088-b877fe073a5e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1033&q=80", "Best Products"));
        bestProductModelList.add(new BestProductModel("id", "https://images.unsplash.com/photo-1519114056088-b877fe073a5e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1033&q=80", "Best Products"));
        bestProductModelList.add(new BestProductModel("id", "https://images.unsplash.com/photo-1519114056088-b877fe073a5e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1033&q=80", "Best Products"));
        bestProductModelList.add(new BestProductModel("id", "https://images.unsplash.com/photo-1519114056088-b877fe073a5e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1033&q=80", "Best Products"));
        bestProductModelList.add(new BestProductModel("id", "https://images.unsplash.com/photo-1519114056088-b877fe073a5e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1033&q=80", "Best Products"));
        bestProductModelList.add(new BestProductModel("id", "https://images.unsplash.com/photo-1519114056088-b877fe073a5e?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1033&q=80", "Best Products"));
        bestProductAdapter.updateList(bestProductModelList);
        if (bestProductModelList.size() > 6){
            binding.bestViewAllBtn.setVisibility(View.VISIBLE);
        }

            topBrandsModelList.clear();
        topBrandsModelList.add(new TopBrandsModel("id", "https://images.unsplash.com/photo-1542690969-5a2050285637?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Top Brands Products"));
        topBrandsModelList.add(new TopBrandsModel("id", "https://images.unsplash.com/photo-1542690969-5a2050285637?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Top Brands Products"));
        topBrandsModelList.add(new TopBrandsModel("id", "https://images.unsplash.com/photo-1542690969-5a2050285637?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Top Brands Products"));
        topBrandsModelList.add(new TopBrandsModel("id", "https://images.unsplash.com/photo-1542690969-5a2050285637?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Top Brands Products"));
        topBrandsModelList.add(new TopBrandsModel("id", "https://images.unsplash.com/photo-1542690969-5a2050285637?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Top Brands Products"));
        topBrandsModelList.add(new TopBrandsModel("id", "https://images.unsplash.com/photo-1542690969-5a2050285637?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Top Brands Products"));
        topBrandsModelList.add(new TopBrandsModel("id", "https://images.unsplash.com/photo-1542690969-5a2050285637?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Top Brands Products"));
        topBrandsModelList.add(new TopBrandsModel("id", "https://images.unsplash.com/photo-1542690969-5a2050285637?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Top Brands Products"));
        topBrandsModelList.add(new TopBrandsModel("id", "https://images.unsplash.com/photo-1542690969-5a2050285637?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Top Brands Products"));
        topBrandsModelList.add(new TopBrandsModel("id", "https://images.unsplash.com/photo-1542690969-5a2050285637?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Top Brands Products"));
        topBrandsModelList.add(new TopBrandsModel("id", "https://images.unsplash.com/photo-1542690969-5a2050285637?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Top Brands Products"));
        topBrandsModelList.add(new TopBrandsModel("id", "https://images.unsplash.com/photo-1542690969-5a2050285637?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Top Brands Products"));
        topBrandsModelList.add(new TopBrandsModel("id", "https://images.unsplash.com/photo-1542690969-5a2050285637?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Top Brands Products"));
        topBrandsModelList.add(new TopBrandsModel("id", "https://images.unsplash.com/photo-1542690969-5a2050285637?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Top Brands Products"));
        topBrandsModelList.add(new TopBrandsModel("id", "https://images.unsplash.com/photo-1542690969-5a2050285637?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Top Brands Products"));
        topBrandsModelList.add(new TopBrandsModel("id", "https://images.unsplash.com/photo-1542690969-5a2050285637?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Top Brands Products"));
        topBrandsAdapter.updateList(topBrandsModelList);


        buyingGuidesModelList.clear();
        buyingGuidesModelList.add(new BuyingGuidesModel("id", "https://images.unsplash.com/photo-1542042179-ff8ef4a8254f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Buying Guides"));
        buyingGuidesModelList.add(new BuyingGuidesModel("id", "https://images.unsplash.com/photo-1542042179-ff8ef4a8254f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Buying Guides"));
        buyingGuidesModelList.add(new BuyingGuidesModel("id", "https://images.unsplash.com/photo-1542042179-ff8ef4a8254f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Buying Guides"));
        buyingGuidesModelList.add(new BuyingGuidesModel("id", "https://images.unsplash.com/photo-1542042179-ff8ef4a8254f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", "Buying Guides"));
        buyingGuidesAdapter.updateList(buyingGuidesModelList);


        imageSlider = binding.imageSlider;
        slideModels.add(new SlideModel("https://images.unsplash.com/photo-1542332213-31f87348057f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://images.unsplash.com/photo-1542995470-870e12e7e14f?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1470&q=80", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://images.unsplash.com/photo-1503376780353-7e6692767b70?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://images.unsplash.com/photo-1485291571150-772bcfc10da5?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=928&q=80", ScaleTypes.FIT));
        imageSlider.setImageList(slideModels);

        // RecyclerViews


        return binding.getRoot();
    }

    @Override
    public void OnLatestProductClicked(LatestProductModel latestProductModel) {

    }

    @Override
    public void OnBestProductClicked(BestProductModel bestProductModel) {

    }

    @Override
    public void OnTopBrandClicked(TopBrandsModel topBrandsModel) {

    }

    @Override
    public void OnBuyingGuidesClicked(BuyingGuidesModel buyingGuidesModel) {

    }
}