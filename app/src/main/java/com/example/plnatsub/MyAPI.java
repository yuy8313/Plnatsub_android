package com.example.plnatsub;


import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MyAPI{

    @Multipart
    @POST("/test/")
    Call<AccountItem> upload(@Part MultipartBody.Part file, @Part("device") String device, @Part("date") String date);

    @POST("/")
    Call<AccountItem> post_accounts(@Body AccountItem accountItem);

    @FormUrlEncoded
    @POST("/test/")
    Call<AccountItem> post(@Field("avg_temp") float avg_temp,@Field("min_temp") float min_temp,@Field("max_temp") float max_temp,@Field("rain_fall") float rain_fall);

    @PATCH("/account/{pk}/")
    Call<AccountItem> patch_accounts(@Path("pk") int pk, @Body AccountItem account);

    @DELETE("/account/{pk}/")
    Call<AccountItem> delete_accounts(@Path("pk") int pk);

//    @GET("/plant/")
//    Call<List<AccountItem>> get_accounts(@Query("price") String price);

    @GET("/result/")
    Call<List<AccountItem>> get_version();


    @GET("/result/{device}/{date}/")
    Call<List<AccountItem>> get_plant(@Path("device") String device, @Path("date") String date);

    @GET("/plantcon/{name}/")
    Call<List<AccountItem>> get_plant_con(@Path("name") String name);

}