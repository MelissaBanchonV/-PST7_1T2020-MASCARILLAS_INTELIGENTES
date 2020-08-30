package com.example.proyectopst.ui.MisMascarillas;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MisMascarillasViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MisMascarillasViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("");
    }

    public LiveData<String> getText() {
        return mText;
    }
}