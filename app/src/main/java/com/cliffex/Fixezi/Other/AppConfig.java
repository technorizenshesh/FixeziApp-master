package com.cliffex.Fixezi.Other;
import com.cliffex.Fixezi.Connection.LoadInterface;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppConfig {
    
    private static Retrofit retrofit = null;
    private static LoadInterface loadInterface = null;

    private static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1000, TimeUnit.SECONDS)
                    .readTimeout(1000, TimeUnit.SECONDS
                    ).build();
            retrofit = new Retrofit.Builder().baseUrl("https://fixezi.com.au/fixezi_admin/")
            //retrofit = new Retrofit.Builder().baseUrl("https://fixezi.com.au/fixezi_admin/FIXEZI/webserv.php?")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        
        return retrofit;
        
    }

    public static LoadInterface loadInterface() {
        if (loadInterface == null) {
            loadInterface = AppConfig.getClient().create(LoadInterface.class);
        }
        return loadInterface;
    }




}
