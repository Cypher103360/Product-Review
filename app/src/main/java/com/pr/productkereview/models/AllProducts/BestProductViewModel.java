package com.pr.productkereview.models.AllProducts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pr.productkereview.utils.Repository;

import java.util.List;

public class BestProductViewModel extends AndroidViewModel {
    private final Repository repository;
    String id;

    public BestProductViewModel(@NonNull Application application, String id) {
        super(application);
        repository = Repository.getInstance();
        this.id = id;
    }

    public LiveData<List<ProductModel>> getBestProducts(){
        return repository.getBestProductMutableLiveData(id);
    }
}
