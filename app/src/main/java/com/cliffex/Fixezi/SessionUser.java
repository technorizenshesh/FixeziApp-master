package com.cliffex.Fixezi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import java.util.HashMap;

/**
 * Created by user on 11/04/2016.
 */
public class SessionUser {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE=0;
    private static final String PREF_NAME="UserLogin";
    private static final String IS_LOGIN="IsLoggedIn";
    public static final String KEY_ID="id";
    public static final String KEY_NAME="name";
    public static final String KEY_FNAME="Fname";
    public static final String KEY_LNAME="Lname";
    public static final String KEY_LAT="lat";
    public static final String KEY_LON="lon";
    public static final String KEY_HOME_ADDRESS="homeAddress";
    public static final String KEY_POSTCODE="postcode";
    public static final String KEY_CITY="city";
    public static final String KEY_STATE="state";
    public static final String KEY_HOMEPHONE="homePhone";
    public static final String KEY_WORKPHONE="workphone";
    public static final String KEY_MOBILEPHONE="mobilephone";
    public static final String KEY_EMAIL="email";
    public static final String KEY_USERNAME="username";

    public SessionUser(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String lat,String lon,String id,String name,String fname,String lname,String homeAddress,String postcode,String city,String state,String homephone,String workphone,String mobilephone,String email,String username) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID,id);
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_FNAME,fname);
        editor.putString(KEY_LNAME,lname);
        editor.putString(KEY_HOME_ADDRESS,homeAddress);
        editor.putString(KEY_POSTCODE,postcode);
        editor.putString(KEY_CITY,city);
        editor.putString(KEY_STATE,state);
        editor.putString(KEY_LAT,lat);
        editor.putString(KEY_LON,lon);
        editor.putString(KEY_HOMEPHONE,homephone);
        editor.putString(KEY_WORKPHONE,workphone);
        editor.putString(KEY_MOBILEPHONE,mobilephone);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_USERNAME,username);
        editor.commit();
    }

    public void checkLogin() {
        if(!this.IsLoggedIn()) {
            Intent intent=new Intent(_context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(intent);
        }
    }


    public String getId() {
        return pref.getString(KEY_ID, null);
    }

    public HashMap<String,String> getUserDetails() {
        HashMap<String,String> user=new HashMap<String,String>();

        user.put(KEY_ID,pref.getString(KEY_ID,null));
        user.put(KEY_LAT,pref.getString(KEY_LAT,null));
        user.put(KEY_LON,pref.getString(KEY_LON,null));
        user.put(KEY_NAME,pref.getString(KEY_NAME,null));
        user.put(KEY_FNAME,pref.getString(KEY_FNAME,null));
        user.put(KEY_LNAME,pref.getString(KEY_LNAME,null));
        user.put(KEY_HOME_ADDRESS,pref.getString(KEY_HOME_ADDRESS,null));
        user.put(KEY_POSTCODE,pref.getString(KEY_POSTCODE,null));
        user.put(KEY_CITY,pref.getString(KEY_CITY,null));
        user.put(KEY_STATE,pref.getString(KEY_STATE,null));
        user.put(KEY_HOMEPHONE,pref.getString(KEY_HOMEPHONE,null));
        user.put(KEY_WORKPHONE,pref.getString(KEY_WORKPHONE,null));
        user.put(KEY_MOBILEPHONE,pref.getString(KEY_MOBILEPHONE,null));
        user.put(KEY_EMAIL,pref.getString(KEY_EMAIL,null));
        user.put(KEY_USERNAME,pref.getString(KEY_USERNAME,null));

        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent intent=new Intent(_context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);
    }

    public boolean IsLoggedIn() {
        return pref.getBoolean(IS_LOGIN,false);
    }

}