package com.pr.productkereview.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.pr.productkereview.activities.ItemDetailsActivity;
import com.pr.productkereview.adapters.trendingProducts.TrendingProductAdapter;
import com.pr.productkereview.adapters.trendingProducts.TrendingProductInterface;
import com.pr.productkereview.databinding.FragmentTrendingProductBinding;
import com.pr.productkereview.db.ProductAppDatabase;
import com.pr.productkereview.db.entity.Products;
import com.pr.productkereview.models.AllProducts.ProductModel;
import com.pr.productkereview.models.AllProducts.TrendingProductModelFactory;
import com.pr.productkereview.models.AllProducts.TrendingProductViewModel;
import com.pr.productkereview.utils.CommonMethods;
import com.pr.productkereview.utils.Prevalent;
import com.pr.productkereview.utils.ShowAds;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class TrendingProductFragment extends Fragment implements TrendingProductInterface {
    FragmentTrendingProductBinding binding;
    TrendingProductViewModel trendingProductViewModel;
    Dialog loading;
    List<ProductModel> trendingModelList = new ArrayList<>();
    TrendingProductAdapter trendingProductAdapter;

    ShowAds showAds = new ShowAds();
    // Room Database
    private ProductAppDatabase productAppDatabase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTrendingProductBinding.inflate(inflater, container, false);
        loading = CommonMethods.getLoadingDialog(requireActivity());
        loading.show();
        trendingProductViewModel = new ViewModelProvider(requireActivity(),
                new TrendingProductModelFactory(requireActivity().getApplication(),
                        "trending")).get(TrendingProductViewModel.class);
        trendingProductAdapter = new TrendingProductAdapter(requireActivity(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.trendingRecyclerview.setLayoutManager(layoutManager);
        binding.trendingRecyclerview.setAdapter(trendingProductAdapter);
        fetchTrendingProducts();
        binding.trendingSwipe.setOnRefreshListener(() -> {
            fetchTrendingProducts();
            binding.trendingSwipe.setRefreshing(false);
        });
        return binding.getRoot();
    }

    private void fetchTrendingProducts() {
        setShowAds();
        trendingProductViewModel.getTrendingProducts().observe(requireActivity(), productModels -> {
            trendingModelList.clear();
            trendingModelList.addAll(productModels);
            trendingProductAdapter.updateTrendingList(trendingModelList);
            loading.dismiss();
        });
    }

    @Override
    public void OnTrendingProductClicked(ProductModel productModel) {
        showAds.destroyBanner();
        showAds.showInterstitialAds(requireActivity());
        productAppDatabase = Room.databaseBuilder(requireActivity(), ProductAppDatabase.class, "ProductDB").allowMainThreadQueries()
                .build();
        boolean checkProductExistence = productAppDatabase.getProductDAO().getProductByTitle(productModel.getProductTitle());
        if (!checkProductExistence) {
            productAppDatabase.getProductDAO().addProducts(new Products(0, productModel.getCategoryId(), productModel.getProductImage()
                    , productModel.getBanner(), productModel.getProductTitle(), productModel.getBuingGuideHindi(),
                    productModel.getBuingGuideEnglish(), productModel.getRatingHindi(), productModel.getRatingEnglish()
                    , productModel.getLatestProduct(), productModel.getBestProduct(), productModel.getTrendingProduct(),productModel.getUrl()));

        }
        Intent intent = new Intent(requireActivity(), ItemDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("latest", productModel);
        intent.putExtras(bundle);
        startActivity(intent);
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