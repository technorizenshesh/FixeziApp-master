package com.cliffex.Fixezi.Connection;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface LoadInterface {

    // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Login %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @FormUrlEncoded
    @POST("FIXEZI/webserv.php?mobile_verify")
    Call<ResponseBody> otpverify(@Field("mobile") String mobile);
    // http://fixezi.com.au/FIXEZI/webserv.php?mobile_verify&mobile=8982891432

    @FormUrlEncoded
    @POST("FIXEZI/webserv.php?update_problem")
    Call<ResponseBody> updatedate(@Field("date") String date,
                                  @Field("time") String time,
                                  @Field("problem_id") String problem_id);

    @FormUrlEncoded
    @POST("FIXEZI/webserv.php?get_problem_id_again_userdata&")
    Call<ResponseBody> jobDetails(@Field("problem_id") String problem_id);
    // https://fixezi.com.au/FIXEZI/webserv.php?get_problem_id_again_userdata&problem_id=46

    @FormUrlEncoded
    @POST("FIXEZI/webserv.php?get_normal_user_data_byid")
    Call<ResponseBody> getUserRatingApiCall(@FieldMap Map<String,String> param);

    @Multipart
    @POST("FIXEZI/webserv.php?add_trademan_notes")
    Call<ResponseBody> addNotes(@Query("problem_id") String problem_id,
                                @Query("notes_description") String notes_description,
                                @Query("notes_type") String notes_type,
                                @Query("timeonjob") String timeonjob,
                                @Part MultipartBody.Part body);
    // https://fixezi.com.au/FIXEZI/webserv.php?add_trademan_notes&problem_id=6&notes_description=abcs&notes_type=Photo&timeonjob=6:45%20PM&notes_image=IMG_20190115_184521.jpg

    @FormUrlEncoded
    @POST("FIXEZI/webserv.php?get_tradesman_profile")
    Call<ResponseBody> tradmentrating(@Field("tradesman_id") String tradesman_id);
    // https://fixezi.com.au/FIXEZI/webserv.php?get_tradesman_profile&tradesman_id=1

    //***ForgetPassword
    @FormUrlEncoded
    @POST("FIXEZI/webserv.php?forgot_password_ios")
    Call<ResponseBody> forgetuser(@Field("email") String email);

    @FormUrlEncoded
    @POST("FIXEZI/webserv.php?add_card")
    Call<ResponseBody> addUserCard(@Field("cus_id") String cus_id,
                                   @Field("source") String source);

    // https://fixezi.com.au/FIXEZI/webserv.php?forgot_password_ios&email=lorence.technorizen@gmail.com@FormUrlEncoded
    @FormUrlEncoded
    @POST("FIXEZI/webserv.php?forgot_password_tradesman_ios")
    Call<ResponseBody> forgettrademan(@Field("email") String email);
    // https://fixezi.com.au/FIXEZI/webserv.php?forgot_password_tradesman_ios&email=lorence.technorizen@gmail.com

}
