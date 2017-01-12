package com.blackbeltcoder.nunut.service;

import com.blackbeltcoder.nunut.model.NuterModel;
import com.blackbeltcoder.nunut.model.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by ainozenbook on 9/15/2016.
 */
public interface RestInterface {

    @FormUrlEncoded
    @POST("user/login")
    Call<NuterResponse> doLogin(@Field("email") String email,
                                      @Field("password") String password);

    @POST("user/register")
    Call<NuterResponse> doRegister(@Body RegisterModel register);

    @POST("user/balance")
    Call<NuterResponse> getBalance(@Body NuterModel nuter);

    @POST("data/allroute")
    Call<MasterDataResponse> getAllRoute(@Body NuterModel nuter);

    @FormUrlEncoded
    @POST("data/checkroute")
    Call<NuterResponse> checkRoute(@Field("originpostal") String originpostal,
                                   @Field("destinationpostal") String destinationpostal);

}
