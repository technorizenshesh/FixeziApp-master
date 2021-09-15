package com.cliffex.Fixezi.Constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class PreferenceConnector {

    public static final String PREF_NAME = "DOCTOR";
    public static final int MODE = Context.MODE_PRIVATE;
    public static final String Status = "Status";
    public static final String UR_ID = "user_id";
    public static final String Api_key = "Api_key";
    public static String otpStatus = "";
    public static String Latitude = "Latitude";
    public static String Longitude = "Longitude";
    public static final String LoginStatus = "login";
    public static final String First_Name = "First_Name";
    public static final String Address_Save = "Address_Save";
    public static final String Address_Save1 = "Address_Save1";
    public static final String Select_radius = "Select_radius";
    public static final String Select_address_Raedus = "Select_address";
    public static final String Select_radius1 = "Select_radius1";
    public static final String Select_address_Raedus1 = "Select_address_Raedus1";
    public static final String buisinessnameeS = "buisinessnameeS";
    public static final String Abn = "Abn";
    public static final String tradingnameS = "tradingnameS";
    public static final String tradeofiicenoS = "tradeofiicenoS";
    public static final String trademobileee = "trademobileee";
    public static final String buisness_owner_name = "buisness_owner_name";
    public static final String buisness_address = "buisness_address";
    public static final String companywebsiteS = "companywebsiteS";
    public static final String tradmenemailS = "tradmenemailS";
    public static final String confirm_email = "confirm_email";
    public static final String FName = "FName";
    public static final String LName = "LName";
    public static final String HouseNo = "HouseNo";
    public static final String HomePhoneNo = "HomePhoneNo";
    public static final String MobileNo = "MobileNo";
    public static final String Email = "Email";
    public static final String Username = "Username";
    public static final String Password = "Password";
    public static final String Confirm_Password = "Confirm_Password";
    public static final String Confirm_Email = "Confirm_Email";
    public static final String otp_status = "otp_status";
    public static final String OTPStatus = "OTPStatus";


    public static String writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();
        return key;
    }


    public static String writeBoolean(Context context, String key, boolean value) {
        getEditor(context).putBoolean(key, value).commit();

        return key;
    }


    public static boolean readBoolean(Context context, String key, boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }


    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    /**
     * clear shared preferences all data
     */
    public static void clearAllData(String name, Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        sharedpreferences.edit().clear().apply();
    }

    public static Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void remove(Context context, String key) {
        getEditor(context).remove(key);
    }


}