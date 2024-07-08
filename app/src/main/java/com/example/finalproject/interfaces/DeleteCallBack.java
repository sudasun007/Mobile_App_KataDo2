package com.example.finalproject.interfaces;

import com.example.finalproject.models.DeleteResponse;

public interface DeleteCallBack {
    void onSuccess(DeleteResponse deleteResponse);
    void onError(Throwable t);
}
