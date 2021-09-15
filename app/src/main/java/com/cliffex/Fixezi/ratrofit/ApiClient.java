package com.cliffex.Fixezi.ratrofit;

import android.content.Context;

import com.androidnetworking.interceptors.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private static Retrofit retrofit = null;
    public static String Account_BASE_URL = "https://fixezi.com.au/fixezi_admin/FIXEZI/";


    public static Retrofit getClient(Context context) {

        if (retrofit == null) {

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.MINUTES)
                    .addInterceptor(logging)
                    .retryOnConnectionFailure(true)
                    .addInterceptor(new ConnectivityInterceptor(context))
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Account_BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
    public static void ResetInstance(){
        retrofit= null;
    }
}
