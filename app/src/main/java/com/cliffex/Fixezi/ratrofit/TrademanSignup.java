package com.cliffex.Fixezi.ratrofit;


import com.cliffex.Fixezi.Model.TradesManSignUp;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface TrademanSignup {

    @Multipart
    @POST("webserv.php?tradesman_sign_up")
    Call<TradesManSignUp> signtradman(
            @Part("business_name") RequestBody business_name,
            @Part("trading_name") RequestBody trading_name,
            @Part("businessOwnerrnName") RequestBody businessOwnerrnName,
            @Part("businessAddress") RequestBody businessAddress,
            @Part("office_no") RequestBody office_no,
            @Part("mobile_no") RequestBody mobile_no,
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
            @Part("hour_min") RequestBody hour_min,
            @Part("registration_id") RequestBody registration_id,
            @Part MultipartBody.Part profile_pic,
            @Part MultipartBody.Part contractor_img);

   // Call<Map<String, String>> signtradman(String buisinessnameeS, String tradingnameS, String buisness_owner_name, String buisness_address, String tradeofiicenoS, String trademobileee, String tradeemailS, String companyDetail, String companywebsiteS, String afterhours, String selecttrade, String servicelocation, String doyouwork, String companyProfileRatio);
}
