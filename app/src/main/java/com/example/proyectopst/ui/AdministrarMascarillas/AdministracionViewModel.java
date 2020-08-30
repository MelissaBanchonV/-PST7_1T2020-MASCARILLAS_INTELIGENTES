package com.example.proyectopst.ui.AdministrarMascarillas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AdministracionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AdministracionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Mascarillas registradas:");
    }

    public LiveData<String> getText() {
        return mText;
    }
}