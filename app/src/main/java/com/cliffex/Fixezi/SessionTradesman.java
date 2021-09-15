package com.cliffex.Fixezi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by user on 11/04/2016.
 */
public class SessionTradesman {


    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "TradesmanLogin";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_ID = "id";
    public static final String KEY_BUSINESSNAME = "BusinessName";
    public static final String KEY_TRADINGNAME = "TradingName";
    public static final String KEY_OFFICENO = "OfficeNo";
    public static final String KEY_MOBILENO = "MobileNo";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_WEBSITEURL = "WebsiteUrl";
    public static final String KEY_PROFILEPIC = "ProfilePic";
    public static final String KEY_BUSYSTATUS = "BusyStatus";


    public SessionTradesman(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String id, String businessname, String tradingname, String officeno, String mobileno, String email, String websiteurl, String profilepic, String busystatus) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_BUSINESSNAME, businessname);
        editor.putString(KEY_TRADINGNAME, tradingname);
        editor.putString(KEY_OFFICENO, officeno);
        editor.putString(KEY_MOBILENO, mobileno);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_WEBSITEURL, websiteurl);
        editor.putString(KEY_PROFILEPIC, profilepic);
        editor.putString(KEY_BUSYSTATUS, busystatus);
        editor.commit();
    }


    public void updateBusyStatus(String busystatus) {
        editor.putString(KEY_BUSYSTATUS, busystatus);
        editor.commit();
    }

    public void checkLogin() {
        if (!this.IsLoggedIn()) {
            Intent intent = new Intent(_context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(intent);
        }
    }

    public String getId() {
        return pref.getString(KEY_ID, null);
    }

    public String getKeyBusystatus() {
        return pref.getString(KEY_BUSYSTATUS, "");
    }


    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_BUSINESSNAME, pref.getString(KEY_BUSINESSNAME, null));
        user.put(KEY_TRADINGNAME, pref.getString(KEY_TRADINGNAME, null));
        user.put(KEY_OFFICENO, pref.getString(KEY_OFFICENO, null));
        user.put(KEY_MOBILENO, pref.getString(KEY_MOBILENO, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_WEBSITEURL, pref.getString(KEY_WEBSITEURL, null));
        user.put(KEY_PROFILEPIC, pref.getString(KEY_PROFILEPIC, null));
        user.put(KEY_BUSYSTATUS, pref.getString(KEY_BUSYSTATUS, ""));


        return user;
    }


    public void logoutUser() {
        editor.clear();
        editor.commit();

        Intent intent = new Intent(_context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);
    }

    public boolean IsLoggedIn()

    {
        return pref.getBoolean(IS_LOGIN, false);
    }


}