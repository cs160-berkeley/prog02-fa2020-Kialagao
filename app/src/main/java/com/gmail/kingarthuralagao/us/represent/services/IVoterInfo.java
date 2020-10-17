package com.gmail.kingarthuralagao.us.represent.services;

import com.gmail.kingarthuralagao.us.represent.models.representatives.Result;
import com.gmail.kingarthuralagao.us.represent.models.voterinfo.VoterInfoResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IVoterInfo {
    @GET("voterinfo")
    Call<VoterInfoResult> getVoterInfo(@Query("address") String address,
                                       @Query("electionId") Long electionId,
                                       @Query("key") String key);
}
