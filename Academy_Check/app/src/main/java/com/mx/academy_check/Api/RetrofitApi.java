package com.mx.academy_check.Api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitApi {

    @FormUrlEncoded
    @POST("fcm")
    Call<String> fcm(@Field("type") String type, @Field("user_code") String user_code);

    @FormUrlEncoded
    @POST("AppLogin")
    Call<String> doLogin(@Field("login_academy") String login_academy, @Field("login_code") String login_code, @Field("token") String token);

    @FormUrlEncoded
    @POST("AppStartCheck")
    Call<String> checkStartTime(@Field("login_code") String login_code,
                                @Field("user_code") String user_code,
                                @Field("start_time") String start_time,
                                @Field("start_day") String start_day);

    @FormUrlEncoded
    @POST("AppEndCheck")
    Call<String> checkEndTime(@Field("login_code") String login_code,
                              @Field("user_code") String user_code,
                              @Field("end_time") String end_time,
                              @Field("end_day") String end_day);

    @FormUrlEncoded
    @POST("CheckList")
    Call<String> getCheckList(@Field("user_code") String user_code, @Field("check_date") String check_date);

    @FormUrlEncoded
    @POST("TodayCheckList")
    Call<String> getTodayCheckTime(@Field("user_code") String user_code);

    /**
     * add user
     */
    @FormUrlEncoded
    @POST("AppRegisterUser")
    Call<String> addUser(@Field("login_code") String login_code,
                         @Field("user_class") String user_class,
                         @Field("user_name") String user_name,
                         @Field("user_gender") String user_gender,
                         @Field("user_phone_num") String user_phone_num,
                         @Field("parent_code") String parent_code);

    /**
     * edit user
     */
    @FormUrlEncoded
    @POST("AppUpdateInfo")
    Call<String> editUser(@Field("user_code") String user_code,
                          @Field("user_class") String user_class,
                          @Field("user_name") String user_name,
                          @Field("user_gender") String user_gender,
                          @Field("user_phone_num") String user_phone_num,
                          @Field("parent_code") String parent_code);

    @FormUrlEncoded
    @POST("App_getParentList")
    Call<String> getParentList(@Field("user_class") String user_class, @Field("login_code") String login_code);

    @FormUrlEncoded
    @POST("App_getTeacherList")
    Call<String> getTeacherList(@Field("login_code") String login_code);

    @FormUrlEncoded
    @POST("App_getStudentList")
    Call<String> getStudentList(@Field("user_class") String user_class, @Field("user_code") String user_code);

    @FormUrlEncoded
    @POST("AppLogin")
    Call<String> tabletCheck(@Field("user_phone_num") String student_code);

    @FormUrlEncoded
    @POST("fcm")
    Call<String> tabletFcm(@Field("type") String type, @Field("user_phone_num") String student_code);

    /* POST 방법 1 */
//    @POST("AppLogin")
//    Call<String> doLogin(@Body LoginUserVO loginVo);

    /* POST 방법 2 */
//    @FormUrlEncoded
//    @POST("AppLogin")
//    Call<String> doLogin(@Field("login_academy") String login_academy, @Field("login_code") String login_code);

//    /* GET 방법 */
//    @GET("AppLogin")
//    Call<String> doLogin(@Query("name") String name, @Query("comment") String comment);

}
