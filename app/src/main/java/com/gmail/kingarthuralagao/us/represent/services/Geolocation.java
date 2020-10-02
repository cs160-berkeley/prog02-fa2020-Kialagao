package com.gmail.kingarthuralagao.us.represent.services;

import com.gmail.kingarthuralagao.us.represent.models.Results;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Geolocation {

    @GET("json")
    Call<Results> getResults(@Query("latlng") String latlng,
                             @Query("result_type") String type,
                             @Query("key") String api_key);
}
