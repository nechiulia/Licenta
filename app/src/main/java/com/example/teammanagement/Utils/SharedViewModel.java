package com.example.teammanagement.Utils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<NewLocation> selected = new MutableLiveData<NewLocation>();

    public void setNewLocation(NewLocation item) {
        selected.setValue(item);
    }

    public LiveData<NewLocation> getSelected() {
        return selected;
    }

}
