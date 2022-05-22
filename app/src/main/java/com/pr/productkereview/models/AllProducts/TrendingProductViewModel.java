package com.pr.productkereview.models.AllProducts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pr.productkereview.utils.Repository;

import java.util.List;

public class TrendingProductViewModel extends AndroidViewModel {
    private final Repository repository;
    String id;

    public TrendingProductViewModel(@NonNull Application application, String id) {
        super(application);
        repository = Repository.getInstance();
        this.id = id;
    }

    public LiveData<List<ProductModel>> getTrendingProducts(){
       return repository.getTrendingProductMutableLiveData(id);
    }


}
