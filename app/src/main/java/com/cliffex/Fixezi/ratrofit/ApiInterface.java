package com.cliffex.Fixezi.ratrofit;


import com.cliffex.Fixezi.Model.ACCEPTED_REJECTED;
import com.cliffex.Fixezi.Model.CurrentSelectCard;
import com.cliffex.Fixezi.Model.TradesManSignUp;
import com.cliffex.Fixezi.Model.TradesManUserBean;
import com.cliffex.Fixezi.Model.UserDetail;
import com.cliffex.Fixezi.Model.WorkerUserList;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("webserv.php?get_tradesman_user_by_trade")
    Observable<TradesManUserBean> get_tradesman_user_by_trade(@Query("select_trade") String select_trade, @Query("service_locatin") String service_locatin, @Query("address_type") String address_type, @Query("after_hours") String after_hours, @Query("select_trade_id") String select_trade_id, @Query("lat") String lat, @Query("lon") String lon);


    @GET("webserv.php?get_user_current_Select_Card")
    Observable<CurrentSelectCard> get_user_current_Select_Card(
            @Query("user_id") String user_id);

    @GET("webserv.php?ACCEPTED_REJECTED")
    Observable<ACCEPTED_REJECTED> ACCEPTED_REJECTED(@Query("order_status") String order_status,
                                                    @Query("problem_id") String problem_id,
                                                    @Query("user_id") String user_id,
                                                    @Query("token") String token,
                                                    @Query("tradesman_user_id") String tradesman_user_id,
                                                    @Query("payment_status") String payment_status,
                                                    @Query("already_Quote") String already_Quote,
                                                    @Query("plan_type") String plan_type);

    @POST("webserv.php?bzystatus_change_by_tradesman")
    Call<ResponseBody> onHoldApi(@Query("tradesman_id") String tradesmanId);

    @GET("webserv.php?worker_user_list")
    Observable<WorkerUserList> getEmpListTrade(@Query("tradesman_id") String tradesman_id);

    @Multipart
    @POST("webserv.php?/tradesman_sign_up")
    Call<TradesManSignUp> signtradman(
            @Part("business_name") RequestBody business_name,
            @Part("trading_name") RequestBody trading_name,
            @Part("businessOwnerrnName") RequestBody businessOwnerrnName,
            @Part("businessAddress") RequestBody businessAddress,
            @Part("office_no") RequestBody office_no,
            @Part("mobile_no") RequestBody mobile_no,
            @Part("abn") RequestBody Abn,
            @Part("email") RequestBody email,
            @Part("website_url") RequestBody website_url,
            @Part("after_hours") RequestBody after_hours,
            @Part("select_trade") RequestBody select_trade,
            @Part("service_locatin") RequestBody service_locatin,
            @Part("work_location") RequestBody work_location,
            @Part("company_upload_ratio") RequestBody company_upload_ratio,
            @Part("company_detail") RequestBody company_detail,
            @Part("company_profile_ratio") RequestBody company_profile_ratio,
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("select_trade_id") RequestBody problemId,
            @Part("hour_min") RequestBody hour_min,
            @Part("registration_id") RequestBody registration_id,
            @Part MultipartBody.Part profile_pic,
            @Part MultipartBody.Part contractor_img);

    @FormUrlEncoded
    @POST("webserv.php?/update_normal_user_profile")
    Call<UserDetail> uploadUserDetail(@Field("user_id") String user_id,
                                      @Field("user_image") String user_image,
                                      @Field("name") String name,
                                      @Field("first_name") String first_name,
                                      @Field("last_name") String last_name,
                                      @Field("home_address") String home_address,
                                      @Field("home_phone") String home_phone,
                                      @Field("work_phone") String work_phone,
                                      @Field("mobile_phone") String mobile_phone,
                                      @Field("email") String email,
                                      @Field("username") String username);

    @FormUrlEncoded
    @POST("webserv.php?/get_tradesman_profile")
    Call<UserDetail> getTRademenProfile(@Field("tradesman_id") String tradesman_id);

    @POST("webserv.php?sign_up")
    Call<String> signUp(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("home_address") String home_address,
            @Field("housenoo") String housenoo,
            @Field("post_code") String post_code,
            @Field("home_phone") String home_phone,
            @Field("work_phone") String work_phone,
            @Field("mobile_phone") String mobile_phone,
            @Field("email") String email,
            @Field("tradesman") String tradesman,
            @Field("username") String username,
            @Field("password") String password,
            @Field("city") String city,
            @Field("state") String state,
            @Field("name") String name,
            @Field("user_lat") String user_lat,
            @Field("user_lon") String user_lon
    );

    // Call<Map<String, String>> signtradman(String buisinessnameeS, String tradingnameS, String buisness_owner_name, String buisness_address, String tradeofiicenoS, String trademobileee, String tradeemailS, String companyDetail, String companywebsiteS, String afterhours, String selecttrade, String servicelocation, String doyouwork, String companyProfileRatio);

}
