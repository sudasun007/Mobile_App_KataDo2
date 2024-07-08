package com.example.finalproject.interfaces;

import com.example.finalproject.models.LoginResponse;

public interface ResponseCallBack {
        void onSuccess(LoginResponse loginResponse) throws Exception;
        void onError(Throwable t);
}
