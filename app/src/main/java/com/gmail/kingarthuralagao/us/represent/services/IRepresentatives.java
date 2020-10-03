package com.gmail.kingarthuralagao.us.represent.services;

import com.gmail.kingarthuralagao.us.represent.models.representatives.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IRepresentatives {
    @GET("representatives")
    Call<Result> getResults(@Query("address") String address,
                            @Query("roles") String role1,
                            @Query("roles") String role2,
                            @Query("key") String key);
}
