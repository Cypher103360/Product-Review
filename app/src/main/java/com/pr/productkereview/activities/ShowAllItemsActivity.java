package com.pr.productkereview.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.room.Room;

import com.ironsource.mediationsdk.IronSource;
import com.pr.productkereview.R;
import com.pr.productkereview.adapters.BestProductAdapter;
import com.pr.productkereview.adapters.BestProductClickInterface;
import com.pr.productkereview.adapters.LatestProductAdapter;
import com.pr.productkereview.adapters.LatestProductClickInterface;
import com.pr.productkereview.adapters.Products.ProductsAdapter;
import com.pr.productkereview.adapters.Products.ProductsClickInterface;
import com.pr.productkereview.adapters.topBrands.TopBrandsAdapter;
import com.pr.productkereview.adapters.topBrands.TopBrandsClickInterface;
import com.pr.productkereview.adapters.trendingProducts.TrendingProductAdapter;
import com.pr.productkereview.adapters.trendingProducts.TrendingProductInterface;
import com.pr.productkereview.databinding.ActivityShowAllItemsBinding;
import com.pr.productkereview.db.ProductAppDatabase;
import com.pr.productkereview.db.entity.Products;
import com.pr.productkereview.models.AllProducts.BestProductModelFactory;
import com.pr.productkereview.models.AllProducts.BestProductViewModel;
import com.pr.productkereview.models.AllProducts.ProductModel;
import com.pr.productkereview.models.AllProducts.ProductModelFactory;
import com.pr.productkereview.models.AllProducts.ProductViewModel;
import com.pr.productkereview.models.AllProducts.TrendingProductModelFactory;
import com.pr.productkereview.models.AllProducts.TrendingProductViewModel;
import com.pr.productkereview.models.TopBrands.BrandViewModel;
import com.pr.productkereview.models.TopBrands.BrandsModel;
import com.pr.productkereview.utils.CommonMethods;
import com.pr.productkereview.utils.Prevalent;
import com.pr.productkereview.utils.ShowAds;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.paperdb.Paper;

public class ShowAllItemsActivity extends AppCompatActivity implements LatestProductClickInterface, BestProductClickInterface, TopBrandsClickInterface, ProductsClickInterface, TrendingProductInterface {
    ActivityShowAllItemsBinding binding;
    List<ProductModel> latestProductModelList = new ArrayList<>();
    List<ProductModel> bestProductModelList = new ArrayList<>();
    List<BrandsModel> topBrandsModelList = new ArrayList<>();
    List<ProductModel> productModelList = new ArrayList<>();
    List<ProductModel> trendingModelList = new ArrayList<>();
    LatestProductAdapter latestProductAdapter;
    BestProductAdapter bestProductAdapter;
    TopBrandsAdapter topBrandsAdapter;
    ProductsAdapter productsAdapter;
    TrendingProductAdapter trendingProductAdapter;
    BrandViewModel brandViewModel;
    ProductViewModel productViewModel;
    ProductViewModel latestProductViewModel;
    BestProductViewModel bestProductViewModel;
    TrendingProductViewModel trendingProductViewModel;
    String key, productId;
    Dialog loading;
    ShowAds showAds;
    // Room Database
    private ProductAppDatabase productAppDatabase;


    // List<Section> sectionList = new ArrayList<>();
    // MainRecyclerAdapter mainRecyclerAdapter;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowAllItemsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        productId = getIntent().getStringExtra("id");
        key = getIntent().getStringExtra("key");
        binding.backIcon.setOnClickListener(v -> onBackPressed());
        loading = CommonMethods.getLoadingDialog(ShowAllItemsActivity.this);
        loading.show();
        showAds = new ShowAds();
        getLifecycle().addObserver(showAds);

        productAppDatabase = Room.databaseBuilder(this, ProductAppDatabase.class, "ProductDB").allowMainThreadQueries()
                .build();
        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        binding.allItemsRecyclerview.setLayoutManager(gridLayoutManager);
        binding.allItemsRecyclerview.setNestedScrollingEnabled(false);


        switch (key) {
            case "latestProducts":
                binding.activityTitle.setText("Latest Products");
                latestProductViewModel = new ViewModelProvider(ShowAllItemsActivity.this,
                        new ProductModelFactory(this.getApplication(), "latest")).get(ProductViewModel.class);
                latestProductAdapter = new LatestProductAdapter(ShowAllItemsActivity.this, this, true);
                binding.allItemsRecyclerview.setAdapter(latestProductAdapter);
                fetchLatestProducts();


                // String sectionName1 = "Section 1";
                // binding.allItemsRecyclerview.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
                break;

            case "bestProducts":
                binding.activityTitle.setText("Best Products");
                bestProductViewModel = new ViewModelProvider(ShowAllItemsActivity.this,
                        new BestProductModelFactory(this.getApplication(), "best")).get(BestProductViewModel.class);
                bestProductAdapter = new BestProductAdapter(ShowAllItemsActivity.this, this, true);
                binding.allItemsRecyclerview.setAdapter(bestProductAdapter);
                fetchBestProducts();
                break;

            case "topBrands":
                binding.activityTitle.setText("Top Brands");
                brandViewModel = new ViewModelProvider(ShowAllItemsActivity.this).get(BrandViewModel.class);
                topBrandsAdapter = new TopBrandsAdapter(ShowAllItemsActivity.this, this, true);
                binding.allItemsRecyclerview.setAdapter(topBrandsAdapter);
                fetchTopBrands();
                break;

            case "Products":
                binding.activityTitle.setText(key);
                productViewModel = new ViewModelProvider(ShowAllItemsActivity.this,
                        new ProductModelFactory(this.getApplication(), productId)).get(ProductViewModel.class);
                productsAdapter = new ProductsAdapter(ShowAllItemsActivity.this, this, true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(ShowAllItemsActivity.this);
                layoutManager.setOrientation(RecyclerView.VERTICAL);
                binding.allItemsRecyclerview.setLayoutManager(layoutManager);
                binding.allItemsRecyclerview.setAdapter(productsAdapter);
                fetchProductsOfCategories();
                break;
            case "mostSelling":
                binding.activityTitle.setText("Most Selling Products");
                trendingProductViewModel = new ViewModelProvider(ShowAllItemsActivity.this,
                        new TrendingProductModelFactory(this.getApplication(),
                                "trending")).get(TrendingProductViewModel.class);
                trendingProductAdapter = new TrendingProductAdapter(ShowAllItemsActivity.this, this);
                LinearLayoutManager layoutManager1 = new LinearLayoutManager(ShowAllItemsActivity.this);
                layoutManager1.setOrientation(RecyclerView.VERTICAL);
                binding.allItemsRecyclerview.setLayoutManager(layoutManager1);
                binding.allItemsRecyclerview.setAdapter(trendingProductAdapter);
                fetchTrendingProducts();

        }

    }

    private void fetchTrendingProducts() {
        trendingProductViewModel.getTrendingProducts().observe(ShowAllItemsActivity.this, productModels -> {
            trendingModelList.clear();
            trendingModelList.addAll(productModels);
            trendingProductAdapter.updateTrendingList(trendingModelList);
            loading.dismiss();
        });
    }

    private void fetchLatestProducts() {
        latestProductViewModel.getLatestProducts().observe(ShowAllItemsActivity.this, productModels -> {
            latestProductModelList.clear();
            latestProductModelList.addAll(productModels);
            latestProductAdapter.updateList(latestProductModelList);
            loading.dismiss();
        });
    }

    private void fetchBestProducts() {
        bestProductViewModel.getBestProducts().observe(ShowAllItemsActivity.this, productModels -> {
            bestProductModelList.clear();
            bestProductModelList.addAll(productModels);
            bestProductAdapter.updateList(bestProductModelList);
            loading.dismiss();
        });
    }

    private void fetchProductsOfCategories() {
        productViewModel.getLatestProducts().observe(ShowAllItemsActivity.this, productModels -> {
            productModelList.clear();
            productModelList.addAll(productModels);
            productsAdapter.updateList(productModelList);
            loading.dismiss();
        });
    }

    private void fetchTopBrands() {
        brandViewModel.getBrands().observe(ShowAllItemsActivity.this, brandsModels -> {
            if (!brandsModels.isEmpty()) {
                topBrandsModelList.clear();
                topBrandsModelList.addAll(brandsModels);
                topBrandsAdapter.updateList(topBrandsModelList);
                loading.dismiss();
            }
        });
    }

    @Override
    public void OnLatestProductClicked(ProductModel latestProductModel, int position) {
        boolean checkProductExistence = productAppDatabase.getProductDAO().getProductByTitle(latestProductModel.getProductTitle());
        if (!checkProductExistence) {
            productAppDatabase.getProductDAO().addProducts(new Products(0, latestProductModel.getCategoryId(), latestProductModel.getProductImage()
                    , latestProductModel.getBanner(), latestProductModel.getProductTitle(), latestProductModel.getBuingGuideHindi(),
                    latestProductModel.getBuingGuideEnglish(), latestProductModel.getRatingHindi(), latestProductModel.getRatingEnglish()
                    , latestProductModel.getLatestProduct(), latestProductModel.getBestProduct(), latestProductModel.getTrendingProduct()));

        }
        showAds.destroyBanner();
        showAds.showInterstitialAds(this);
        Intent intent = new Intent(ShowAllItemsActivity.this, ItemDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("latest", latestProductModel);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void OnBestProductClicked(ProductModel bestProductModel) {
        boolean checkProductExistence = productAppDatabase.getProductDAO().getProductByTitle(bestProductModel.getProductTitle());
        if (!checkProductExistence) {
            productAppDatabase.getProductDAO().addProducts(new Products(0, bestProductModel.getCategoryId(), bestProductModel.getProductImage()
                    , bestProductModel.getBanner(), bestProductModel.getProductTitle(), bestProductModel.getBuingGuideHindi(),
                    bestProductModel.getBuingGuideEnglish(), bestProductModel.getRatingHindi(), bestProductModel.getRatingEnglish()
                    , bestProductModel.getLatestProduct(), bestProductModel.getBestProduct(), bestProductModel.getTrendingProduct()));

        }
        showAds.destroyBanner();
        showAds.showInterstitialAds(this);
        Intent intent = new Intent(ShowAllItemsActivity.this, ItemDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("latest", bestProductModel);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void OnTopBrandClicked(BrandsModel topBrandsModel) {

        showAds.destroyBanner();
        showAds.showInterstitialAds(this);
        Intent intent = new Intent(ShowAllItemsActivity.this, TopBrandDetailsActivity.class);
        intent.putExtra("img", topBrandsModel.getBanner());
        intent.putExtra("title", topBrandsModel.getTitle());
        intent.putExtra("desc", topBrandsModel.getDesc());
        intent.putExtra("url", topBrandsModel.getUrl());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        showAds.destroyBanner();
    }

    @Override
    public void OnProductClicked(ProductModel productModel) {
        boolean checkProductExistence = productAppDatabase.getProductDAO().getProductByTitle(productModel.getProductTitle());
        if (!checkProductExistence) {
            productAppDatabase.getProductDAO().addProducts(new Products(0, productModel.getCategoryId(), productModel.getProductImage()
                    , productModel.getBanner(), productModel.getProductTitle(), productModel.getBuingGuideHindi(),
                    productModel.getBuingGuideEnglish(), productModel.getRatingHindi(), productModel.getRatingEnglish()
                    , productModel.getLatestProduct(), productModel.getBestProduct(), productModel.getTrendingProduct()));

        }
        showAds.destroyBanner();
        showAds.showInterstitialAds(this);
        Intent intent = new Intent(ShowAllItemsActivity.this, ItemDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("latest", productModel);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void OnTrendingProductClicked(ProductModel productModel) {
        showAds.destroyBanner();
        showAds.showInterstitialAds(this);

        boolean checkProductExistence = productAppDatabase.getProductDAO().getProductByTitle(productModel.getProductTitle());
        if (!checkProductExistence) {
            productAppDatabase.getProductDAO().addProducts(new Products(0, productModel.getCategoryId(), productModel.getProductImage()
                    , productModel.getBanner(), productModel.getProductTitle(), productModel.getBuingGuideHindi(),
                    productModel.getBuingGuideEnglish(), productModel.getRatingHindi(), productModel.getRatingEnglish()
                    , productModel.getLatestProduct(), productModel.getBestProduct(), productModel.getTrendingProduct()));

        }
        Intent intent = new Intent(ShowAllItemsActivity.this, ItemDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("latest", productModel);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
        showAds.destroyBanner();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
        showAds.showBottomBanner(this, findViewById(R.id.adView_bottom));
        showAds.showTopBanner(this, findViewById(R.id.adView_top));
    }
}