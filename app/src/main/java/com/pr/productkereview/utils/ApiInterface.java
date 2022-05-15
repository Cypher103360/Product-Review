package com.pr.productkereview.utils;

import com.pr.productkereview.models.MessageModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("upload_user_data.php")
    Call<MessageModel> uploadUserData(@FieldMap Map<String, String> map);
}

