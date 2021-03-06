package com.pr.productkereview.fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.pr.productkereview.activities.ItemDetailsActivity;
import com.pr.productkereview.activities.ShowAllItemsActivity;
import com.pr.productkereview.activities.TopBrandDetailsActivity;
import com.pr.productkereview.adapters.BestProductAdapter;
import com.pr.productkereview.adapters.BestProductClickInterface;
import com.pr.productkereview.adapters.BuyingGuidesAdapter;
import com.pr.productkereview.adapters.BuyingGuidesClickInterface;
import com.pr.productkereview.adapters.LatestProductAdapter;
import com.pr.productkereview.adapters.LatestProductClickInterface;
import com.pr.productkereview.adapters.topBrands.TopBrandsAdapter;
import com.pr.productkereview.adapters.topBrands.TopBrandsClickInterface;
import com.pr.productkereview.databinding.FragmentHomeBinding;
import com.pr.productkereview.db.ProductAppDatabase;
import com.pr.productkereview.db.entity.Products;
import com.pr.productkereview.models.AllProducts.BestProductModelFactory;
import com.pr.productkereview.models.AllProducts.BestProductViewModel;
import com.pr.productkereview.models.AllProducts.LatestProductModelFactory;
import com.pr.productkereview.models.AllProducts.ProductModel;
import com.pr.productkereview.models.AllProducts.ProductViewModel;
import com.pr.productkereview.models.BannerImages.BannerImageModel;
import com.pr.productkereview.models.TopBrands.BrandViewModel;
import com.pr.productkereview.models.TopBrands.BrandsModel;
import com.pr.productkereview.models.UrlsModels.UrlModel;
import com.pr.productkereview.utils.ApiInterface;
import com.pr.productkereview.utils.ApiWebServices;
import com.pr.productkereview.utils.CommonMethods;
import com.pr.productkereview.utils.Prevalent;
import com.pr.productkereview.utils.ShowAds;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements LatestProductClickInterface, BestProductClickInterface, TopBrandsClickInterface, BuyingGuidesClickInterface {
    FragmentHomeBinding binding;
    ImageSlider imageSlider;
    Dialog loading;
    List<SlideModel> slideModels = new ArrayList<>();
    List<BannerImageModel> bannerImageModelList = new ArrayList<>();
    ApiInterface apiInterface;
    String webUrl, bannerUrl, whatsappText;
    ProductViewModel latestProductViewModel;
    BestProductViewModel bestProductViewModel;
    BrandViewModel brandViewModel;

    List<ProductModel> latestProductModelList = new ArrayList<>();
    List<ProductModel> bestProductModelList = new ArrayList<>();
    List<BrandsModel> topBrandsModelList = new ArrayList<>();
    List<Products> buyingGuidesModelList = new ArrayList<>();

    LatestProductAdapter latestProductAdapter;
    BestProductAdapter bestProductAdapter;
    TopBrandsAdapter topBrandsAdapter;
    BuyingGuidesAdapter buyingGuidesAdapter;
    ShowAds showAds = new ShowAds();

    // Room Database
    private ProductAppDatabase productAppDatabase;

    public static String decodeEmoji(String message) {
        try {
            return URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Your migration strategy here
            database.execSQL("ALTER TABLE products ADD COLUMN url TEXT");
        }
    };
// 7817056405
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        apiInterface = ApiWebServices.getApiInterface();
        fetchWhatsappText("whatsapp");

        brandViewModel = new ViewModelProvider(requireActivity()).get(BrandViewModel.class);

        latestProductViewModel = new ViewModelProvider(requireActivity(),
                new LatestProductModelFactory(requireActivity().getApplication(), "latest")).get(ProductViewModel.class);

        bestProductViewModel = new ViewModelProvider(requireActivity(),
                new BestProductModelFactory(requireActivity().getApplication(), "best")).get(BestProductViewModel.class);

        loading = CommonMethods.getLoadingDialog(requireActivity());
        loading.show();

        binding.lottieSiteButton.setOnClickListener(v -> {
            openWebPage(webUrl, requireActivity());
        });
        binding.lottieWhatsappButton.setOnClickListener(v -> {
            try {
                CommonMethods.whatsApp(requireActivity(), whatsappText);
            } catch (UnsupportedEncodingException | PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        });
        binding.lottieGmailButton.setOnClickListener(v -> {
            CommonMethods.contactUs(requireActivity());
        });

        // Recyclerview
        binding.latestProductRecyclerview.setNestedScrollingEnabled(false);
        binding.bestProductRecyclerview.setNestedScrollingEnabled(false);
        binding.topBrandProductRecyclerview.setNestedScrollingEnabled(false);
        binding.buyingGuideRecyclerview.setNestedScrollingEnabled(false);


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
        latestProductAdapter = new LatestProductAdapter(requireActivity(), this, false);
        bestProductAdapter = new BestProductAdapter(requireActivity(), this, false);
        topBrandsAdapter = new TopBrandsAdapter(requireActivity(), this, false);
        buyingGuidesAdapter = new BuyingGuidesAdapter(requireActivity(), this);

        // setting adapters
        binding.latestProductRecyclerview.setAdapter(latestProductAdapter);
        binding.bestProductRecyclerview.setAdapter(bestProductAdapter);
        binding.topBrandProductRecyclerview.setAdapter(topBrandsAdapter);
        binding.buyingGuideRecyclerview.setAdapter(buyingGuidesAdapter);

        fetchBanners();
        fetchTopBrands();
        fetchUrls("site");
        fetchLatestProducts();
        fetchBestProducts();
        fetchUserClickedProducts();
        getLifecycle().addObserver(showAds);
        setShowAds();
        binding.homeSwipeRefreshLayout.setOnRefreshListener(() -> {
            setShowAds();
            fetchBanners();
            fetchTopBrands();
            fetchUrls("site");
            fetchLatestProducts();
            fetchBestProducts();
            fetchUserClickedProducts();
            fetchWhatsappText("whatsapp");

            binding.homeSwipeRefreshLayout.setRefreshing(false);
        });

        return binding.getRoot();
    }

    private void fetchUserClickedProducts() {
        // Room Database
        productAppDatabase = Room.databaseBuilder(
                        requireActivity(),
                        ProductAppDatabase.class,
                        "ProductDB")
                .allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2)
                .build();

        productAppDatabase.getProductDAO().deleteItemsByLimit();
        buyingGuidesModelList.clear();
        buyingGuidesModelList.addAll(productAppDatabase.getProductDAO().getProducts());
        buyingGuidesAdapter.updateList(buyingGuidesModelList);
        binding.buyingGuideLayout.setVisibility(View.VISIBLE);

    }

    private void fetchLatestProducts() {
        latestProductViewModel.getLatestProducts().observe(requireActivity(), productModels -> {
            latestProductModelList.clear();
            latestProductModelList.addAll(productModels);
            latestProductAdapter.updateList(latestProductModelList);
            loading.dismiss();
            binding.latestProductLayout.setVisibility(View.VISIBLE);

//            if (latestProductModelList.size() > 7) {
//                binding.latestViewAllBtn.setVisibility(View.VISIBLE);
//                binding.latestViewAllBtn.setOnClickListener(v -> {
//                    showAds.destroyBanner();
//                    showAds.showInterstitialAds(requireActivity());
//                    Intent intent = new Intent(requireActivity(), ShowAllItemsActivity.class);
//                    intent.putExtra("key", "latestProducts");
//                    startActivity(intent);
//
//                });
//            }
        });
    }

    private void fetchBestProducts() {
        bestProductViewModel.getBestProducts().observe(requireActivity(), productModels -> {
            bestProductModelList.clear();
            bestProductModelList.addAll(productModels);
            bestProductAdapter.updateList(bestProductModelList);
            loading.dismiss();
            binding.bestProductLayout.setVisibility(View.VISIBLE);

            if (bestProductModelList.size() > 6) {
                binding.bestViewAllBtn.setVisibility(View.VISIBLE);
                binding.bestViewAllBtn.setOnClickListener(v -> {
                    showAds.destroyBanner();
                    showAds.showInterstitialAds(requireActivity());
                    Intent intent = new Intent(requireActivity(), ShowAllItemsActivity.class);
                    intent.putExtra("key", "bestProducts");
                    startActivity(intent);
                });
            }
        });
    }

    private void fetchTopBrands() {
        brandViewModel.getBrands().observe(requireActivity(), brandsModels -> {
            if (!brandsModels.getData().isEmpty()) {
                topBrandsModelList.clear();
                topBrandsModelList.addAll(brandsModels.getData());
                topBrandsAdapter.updateList(topBrandsModelList);
                loading.dismiss();
                binding.topBrandProductLayout.setVisibility(View.VISIBLE);

//                if (topBrandsModelList.size() > 7) {
//                    binding.topBrandsViewAllBtn.setVisibility(View.VISIBLE);
//                    binding.topBrandsViewAllBtn.setOnClickListener(v -> {
//                        showAds.destroyBanner();
//                        showAds.showInterstitialAds(requireActivity());
//                        Intent intent = new Intent(requireActivity(), ShowAllItemsActivity.class);
//                        intent.putExtra("key", "topBrands");
//                        startActivity(intent);
//                    });
//                }
            }
        });
    }

    public void fetchBanners() {
        Call<List<BannerImageModel>> call = apiInterface.getBanners();
        call.enqueue(new Callback<List<BannerImageModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<BannerImageModel>> call, @NonNull Response<List<BannerImageModel>> response) {
                if (response.isSuccessful()) {
                    imageSlider = binding.imageSlider;
                    assert response.body() != null;
                    bannerImageModelList.clear();
                    slideModels.clear();
                    bannerImageModelList.addAll(response.body());
                    for (BannerImageModel bannerImageModel : response.body()) {
                        Log.d("bannerImages", bannerImageModel.getImage());
                        slideModels.add(new SlideModel(
                                ApiWebServices.base_url + "bannerImages/" + bannerImageModel.getImage(), ScaleTypes.FIT));

                    }
                    imageSlider.setImageList(slideModels);
                    imageSlider.setItemClickListener(i -> {
                        bannerUrl = bannerImageModelList.get(i).getUrl();
                        openWebPage(bannerUrl, requireActivity());
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BannerImageModel>> call, @NonNull Throwable t) {

            }
        });
    }

    public void fetchUrls(String tips) {
        Call<UrlModel> call = apiInterface.getUrls(tips);
        call.enqueue(new Callback<UrlModel>() {
            @Override
            public void onResponse(@NonNull Call<UrlModel> call, @NonNull Response<UrlModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    webUrl = decodeEmoji(response.body().getUrl());
                    Log.d("webUrl", webUrl);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UrlModel> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void OnLatestProductClicked(ProductModel latestProductModel, int position) {
        showAds.showInterstitialAds(requireActivity());
        showAds.destroyBanner();
        Intent intent = new Intent(requireActivity(), ItemDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("latest", latestProductModel);
        intent.putExtras(bundle);
        startActivity(intent);
        fetchUserClickedProducts();
        boolean checkProductExistence = productAppDatabase.getProductDAO().getProductByTitle(latestProductModel.getProductTitle());
        if (!checkProductExistence) {
            productAppDatabase.getProductDAO().addProducts(new Products(0, latestProductModel.getCategoryId(), latestProductModel.getProductImage()
                    , latestProductModel.getBanner(), latestProductModel.getProductTitle(), latestProductModel.getBuingGuideHindi(),
                    latestProductModel.getBuingGuideEnglish(), latestProductModel.getRatingHindi(), latestProductModel.getRatingEnglish()
                    , latestProductModel.getLatestProduct(), latestProductModel.getBestProduct(), latestProductModel.getTrendingProduct(),latestProductModel.getUrl()));

        }
    }

    @Override
    public void OnBestProductClicked(ProductModel bestProductModel) {
        showAds.showInterstitialAds(requireActivity());
        showAds.destroyBanner();
        Intent intent = new Intent(requireActivity(), ItemDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("latest", bestProductModel);
        intent.putExtras(bundle);
        startActivity(intent);

        fetchUserClickedProducts();

//         Room Database
        productAppDatabase = Room.databaseBuilder(requireActivity(), ProductAppDatabase.class, "ProductDB").allowMainThreadQueries()
                .addMigrations(MIGRATION_1_2)
                .build();
        boolean checkProductExistence = productAppDatabase.getProductDAO().getProductByTitle(bestProductModel.getProductTitle());
        if (!checkProductExistence) {
            productAppDatabase.getProductDAO().addProducts(new Products(0, bestProductModel.getCategoryId(), bestProductModel.getProductImage()
                    , bestProductModel.getBanner(), bestProductModel.getProductTitle(), bestProductModel.getBuingGuideHindi(),
                    bestProductModel.getBuingGuideEnglish(), bestProductModel.getRatingHindi(), bestProductModel.getRatingEnglish()
                    , bestProductModel.getLatestProduct(), bestProductModel.getBestProduct(), bestProductModel.getTrendingProduct(),bestProductModel.getUrl()));

        }

    }

    @Override
    public void OnTopBrandClicked(BrandsModel topBrandsModel) {
        showAds.showInterstitialAds(requireActivity());
        showAds.destroyBanner();
        Intent intent = new Intent(requireActivity(), TopBrandDetailsActivity.class);
        intent.putExtra("img", topBrandsModel.getBanner());
        intent.putExtra("title", topBrandsModel.getTitle());
        intent.putExtra("desc", topBrandsModel.getDesc());
        intent.putExtra("url", topBrandsModel.getUrl());
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        loading.show();
        fetchBanners();
        fetchTopBrands();
        fetchUrls("site");
        fetchLatestProducts();
        fetchBestProducts();
        fetchUserClickedProducts();
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
    public void OnBuyingGuidesClicked(Products buyingGuidesModel) {
        showAds.showInterstitialAds(requireActivity());
        showAds.destroyBanner();
        // Room Database
        Intent intent = new Intent(requireActivity(), ItemDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("latest", new ProductModel(
                buyingGuidesModel.getBanner(),
                String.valueOf(buyingGuidesModel.getId()),
                buyingGuidesModel.getCategoryId(),
                buyingGuidesModel.getProductImage(),
                buyingGuidesModel.getProductTitle(),
                buyingGuidesModel.getBuyingGuideHindi(),
                buyingGuidesModel.getBuyingGuideEnglish(),
                buyingGuidesModel.getRatingHindi(),
                buyingGuidesModel.getRatingEnglish(),
                buyingGuidesModel.getLatestProduct(),
                buyingGuidesModel.getBestProduct(),
                buyingGuidesModel.getTrendingProduct(),
                buyingGuidesModel.getUrl()));
        intent.putExtras(bundle);
        startActivity(intent);

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