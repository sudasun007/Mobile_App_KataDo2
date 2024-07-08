package com.example.finalproject.services;

import com.example.finalproject.interfaces.ApiService;
import com.example.finalproject.interfaces.DeleteCallBack;
import com.example.finalproject.interfaces.ResponseCallBack;
import com.example.finalproject.models.DeleteAcc;
import com.example.finalproject.models.LoginResponse;
import com.example.finalproject.models.LogoutRequest;
import com.example.finalproject.models.User;
import com.example.finalproject.models.DeleteResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthService {
    private String base_url="http://172.20.10.2:5000/api/";
    public AuthService(){

    }
    public void login(User user, final ResponseCallBack callback){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiService apiService=retrofit.create(ApiService.class);
        Call<LoginResponse> call= apiService.getLogin(user);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    callback.onSuccess(response.body());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callback.onError(t);
            }
        });
    }
    public LoginResponse signup(User user, ResponseCallBack callBack){
        LoginResponse loginResponse=new LoginResponse();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiService apiService=retrofit.create(ApiService.class);
        Call<LoginResponse> call= apiService.getSignup(user);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    callBack.onSuccess(response.body());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
            callBack.onError(t);
            }
        });
        return loginResponse;
    }
    public void  deleteAcc(DeleteAcc deleteAcc,final DeleteCallBack callback){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiService apiService=retrofit.create(ApiService.class);
        Call<DeleteResponse> call= apiService.getDelete(deleteAcc);
        call.enqueue(new Callback<DeleteResponse>(){
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                callback.onError(t);
            }


        });


    }
    public LoginResponse update(User user, ResponseCallBack callBack){
        LoginResponse loginResponse=new LoginResponse();
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiService apiService=retrofit.create(ApiService.class);
        Call<LoginResponse> call= apiService.getUpdate(user);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    callBack.onSuccess(response.body());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callBack.onError(t);
            }
        });
        return loginResponse;
    }
    public void logout(LogoutRequest logoutRequest, DeleteCallBack callBack){

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(base_url)
                .addConverterFactory(GsonConverterFactory.create()).build();
        ApiService apiService=retrofit.create(ApiService.class);
        Call<DeleteResponse> call= apiService.getLogout(logoutRequest);
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                callBack.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                callBack.onError(t);
            }
        });

    }
}
