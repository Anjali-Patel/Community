package com.gss.community;

import com.gss.community.Model.Zipcode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface ApiInterface {
    @GET()
    Call<Zipcode> getUsersList(@Url String zipcode);

}
