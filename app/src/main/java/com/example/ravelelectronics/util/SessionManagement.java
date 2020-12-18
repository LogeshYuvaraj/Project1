package com.example.ravelelectronics.util;



import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.example.ravelelectronics.LoginActivity;

import java.util.HashMap;

public class SessionManagement {

    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE=0;


    private static final String PREF_NAME="ravel";
    private static final String IS_LOGIN="LoginSuccessfully";
    public static final String KEY_CODE="empID";
    public static final String KEY_NAME="firstName";
    public static final String KEY_SALESPERSON="salesPrson";
    public static final String KEY_MOBILE="mobile";
    public static final String KEY_EMAIL="email";
    public static final String KEY_JOBTITLE="jobTitle";
    public static final String KEY_SLPNAME="sessionSlpName";
    public static final String KEY_MANAGER="sessionmanager";
    public static final String KEY_CRMAdmin="sessionCRMAdmin";

    @SuppressLint("CommitPrefEdits")
    public SessionManagement(Context context){
        this._context=context;
        pref=_context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor=pref.edit();
    }

    public void createLoginSession(String empID, String firstName, String salesPrson, String mobile,
                                   String email, String jobTitle, String sessionSlpName, String sessionmanager, String sessionCRMAdmin){

        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_CODE, empID);
        editor.putString(KEY_NAME, firstName);
        editor.putString(KEY_SALESPERSON, salesPrson);
        editor.putString(KEY_MOBILE, mobile);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_JOBTITLE, jobTitle);
        editor.putString(KEY_SLPNAME, sessionSlpName);
        editor.putString(KEY_MANAGER, sessionmanager);
        editor.putString(KEY_CRMAdmin, sessionCRMAdmin);
        editor.commit();
    }

    public void checkLogin(){
        if (!this.isLoggedIn()) {
            Intent i=new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user=new HashMap<String, String>();
        user.put(KEY_CODE, pref.getString(KEY_CODE, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_SALESPERSON, pref.getString(KEY_SALESPERSON, null));
        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_JOBTITLE, pref.getString(KEY_JOBTITLE, null));
        user.put(KEY_SLPNAME, pref.getString(KEY_SLPNAME, null));
        user.put(KEY_MANAGER, pref.getString(KEY_MANAGER, null));
        user.put(KEY_CRMAdmin, pref.getString(KEY_CRMAdmin, null));
        return user;
    }
    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent in = new Intent(_context, LoginActivity.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        _context.startActivity(in);
    }
    public boolean isLoggedIn()
    {
        return pref.getBoolean(IS_LOGIN, false);

    }
}
