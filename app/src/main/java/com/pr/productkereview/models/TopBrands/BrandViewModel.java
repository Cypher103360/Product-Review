package com.pr.productkereview.models.TopBrands;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pr.productkereview.utils.Repository;

import java.util.List;

public class BrandViewModel extends AndroidViewModel {
    private final Repository repository;

    public BrandViewModel(@NonNull Application application) {
        super(application);
        repository = Repository.getInstance();
    }

    public LiveData<List<BrandsModel>> getBrands(){
        return repository.getBrandMutableLiveData();
    }
}
