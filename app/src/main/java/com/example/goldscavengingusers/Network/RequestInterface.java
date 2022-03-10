package com.example.goldscavengingusers.Network;


import com.example.goldscavengingusers.Model.AddToWarehouseResponse;
import com.example.goldscavengingusers.Model.AddedResponse;
import com.example.goldscavengingusers.Model.DeleteResponse;
import com.example.goldscavengingusers.Model.GoldBarStatusResponse;
import com.example.goldscavengingusers.Model.LoginResponse;
import com.example.goldscavengingusers.Model.RecoveryGoldbarWarehousesResponse;
import com.example.goldscavengingusers.Model.RecoveryToWarehousesResponse;
import com.example.goldscavengingusers.Model.RestpasswordResponse;
import com.example.goldscavengingusers.Model.ShowGoldbarsResponse;
import com.example.goldscavengingusers.Model.UpdateResponse;
import com.example.goldscavengingusers.Model.UserUpdateResponse;
import com.example.goldscavengingusers.Model.WarehouseDetailsResponse;
import com.example.goldscavengingusers.Model.WarehouseRecycleBinResponse;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


public interface RequestInterface {


    //LOGIN
    @FormUrlEncoded
    @POST("userlogin/{mac}")
    Call<LoginResponse> Login(
            @Path("mac") String mac,
            @Field("phone") String phone,
            @Field("password") String password);

    //Reset Password
    @FormUrlEncoded
    @POST("resetpassword/{phone}")
    Call<RestpasswordResponse> RestPassword(
            @Path("phone") String phone,
            @Field("mac_address") String mac_address,
            @Field("password") String password);

    //Added
    @FormUrlEncoded
    @POST("goldbar")
    Call<AddedResponse> Added_Owner(
            @Field("gold_bar_owner") String gold_bar_owner,
            @Field("gold_ingot_weight") String gold_ingot_weight,
            @Field("sample_weight") String sample_weight,
            @Field("gold_karat_weight") String gold_karat_weight,
            @Field("price_gram") String price_gram,
            @Field("date_add") String date_add,
            @Header("Authorization") String authorization
    );

    //Show
    @GET("goldbar")
    Call<ShowGoldbarsResponse> Show_Goldbars(
            @Header("Authorization") String authorization
    );


    //Weight_Edit
    @FormUrlEncoded
    @PUT("goldbar/{id}")
    Call<UpdateResponse> WeightEdit(
            @Path("id") String id,
            @Field("gold_bar_owner") String gold_bar_owner,
            @Field("gold_ingot_weight") String gold_ingot_weight,
            @Field("sample_weight") String sample_weight,
            @Field("gold_karat_weight") String gold_karat_weight,
            @Field("price_gram") String price_gram,
            @Header("Authorization") String authorization
    );

    //Delete_Goldbar
    @DELETE("goldbar/{id}")
    Call<DeleteResponse> GoldbarDelete(
            @Path("id") String id,
            @Header("Authorization") String authorization
    );


    //Edit_profiel
    @FormUrlEncoded
    @PUT("useredit/{id}")
    Call<UserUpdateResponse> Edit_profile(
            @Path("id") String id,
            @Field("name") String name,
            @Field("phone") String phone,
            @Field("shop") String shop,
            @Field("password") String password,
            @Header("Authorization") String authorization
    );


    //goldbar_status
    @FormUrlEncoded
    @PUT("goldbarstatus/{id}")
    Call<GoldBarStatusResponse> Goldbarstatus(
            @Path("id") String id,
            @Field("status") String status,
            @Header("Authorization") String authorization
    );


    //Add To Warehouse
    @FormUrlEncoded
    @POST("warehouse/store")
    Call<AddToWarehouseResponse> AddToWarehouse(
            @Field("gold_bar_id") String gold_bar_id,
            @Field("date_add_item") String date_add_item,
            @Header("Authorization") String authorization
    );

    //Get Warehouse Details
    @GET("warehouse/show/{date_add_item}")
    Call<WarehouseDetailsResponse> GetWarehouseDetails(
            @Path("date_add_item") String date_add_item,
            @Header("Authorization") String authorization
    );

    //Detelte From Warehouse
    @POST("warehouse/delete/{id}")
    Call<RecoveryGoldbarWarehousesResponse> Recovey(
            @Path("id") String id,
            @Header("Authorization") String authorization);


    //Recovery To Warehouse
    @FormUrlEncoded
    @POST("warehouse/store2")
    Call<RecoveryToWarehousesResponse> RecoveryToWarehouses(
            @Field("date_add_item") String date_add_item,
            @Field("gold_bar_id") String gold_bar_id,
            @Header("Authorization") String authorization);

    //Delete From Warehouse RecycleBin To Main Acticity
    @POST("returned/delete/{id}")
    Call<RecoveryToWarehousesResponse> DeleteToMain(
            @Path("id") String id,
            @Header("Authorization") String authorization);

};


