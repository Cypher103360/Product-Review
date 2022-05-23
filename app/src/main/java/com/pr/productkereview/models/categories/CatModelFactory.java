package com.pr.productkereview.models.categories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class CatModelFactory implements ViewModelProvider.Factory {
    Application application;
    String id;

    public CatModelFactory(Application application, String id) {
        this.application = application;
        this.id = id;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        return (T) new CatViewModel(application,id);
    }
}
