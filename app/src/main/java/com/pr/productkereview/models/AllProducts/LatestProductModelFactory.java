package com.pr.productkereview.models.AllProducts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class LatestProductModelFactory implements ViewModelProvider.Factory {
    Application application;
    String id;

    public LatestProductModelFactory(Application application, String id) {
        this.application = application;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        return (T) new ProductViewModel(application,id);
    }
}
