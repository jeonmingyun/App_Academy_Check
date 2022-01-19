package com.example.academy_check_tablet.Api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitApi {

    @FormUrlEncoded
    @POST("AppLogin")
    Call<String> timeCheck(@Field("student_code") String student_code);

}
