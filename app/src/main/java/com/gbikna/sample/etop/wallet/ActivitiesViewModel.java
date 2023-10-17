package com.gbikna.sample.etop.wallet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class ActivitiesViewModel extends ViewModel {
    private static final String TAG = ActivitiesViewModel.class.getSimpleName();
    private MutableLiveData<ApiResponse<List<ActivitiesResponse>>> activitiesResponse = new MutableLiveData<ApiResponse<List<ActivitiesResponse>>>();

    public LiveData<ApiResponse<List<ActivitiesResponse>>> getResponse() {
        return activitiesResponse;
    }
}

