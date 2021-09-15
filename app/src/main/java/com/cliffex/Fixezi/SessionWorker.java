package com.cliffex.Fixezi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by user on 11/04/2016.
 */
public class SessionWorker {


    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Context _context;

    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "EmployeeLogin";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "Name";
    public static final String KEY_USERNAME = "Username";
    public static final String KEY_TRADEMAN_ID = "Trademan_id";


    public SessionWorker(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String id, String name, String username, String trademan_id) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_ID, id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_TRADEMAN_ID, trademan_id);
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

    public String getTrademanId() {
        return pref.getString(KEY_TRADEMAN_ID, "");
    }


    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent intent = new Intent(_context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(intent);
    }

    public boolean IsLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

}