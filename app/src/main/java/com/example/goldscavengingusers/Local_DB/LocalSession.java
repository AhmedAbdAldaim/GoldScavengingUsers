package com.example.goldscavengingusers.Local_DB;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class LocalSession {

    private static final String PREF_NAME = "Data_clinic";
    private static final String TOKEN = "token";
    private static final String ID = "id";
    private static final String NAME = "name";
    private static final String PHONE = "phone";
    private static final String PASSWORD = "password";
    private static final String SHOP = "shop";
    private static final String ROLE = "role";
    private static final String MACTADDRESS = "macaddress";
    private static final String isSessionCreated = "isSessionCreated";
    private static final String LASTLOGIN = "lastlogin";
    private static final String LOCAL = "LOCAL";

    private static final String ISAddTOLIST = "isaddtolist";


    private static SharedPreferences mPreferences;
    private static SharedPreferences.Editor editor;


    public LocalSession(Context context) {
        mPreferences = context.getSharedPreferences(PREF_NAME, 0);
        editor = mPreferences.edit();
    }


    public void createSession(String token,String Id,String name,String phone,String shop,String role,String macaddress,String password) {
        editor.putBoolean(LocalSession.isSessionCreated, true);
        editor.putString(LocalSession.TOKEN,token);
        editor.putString(LocalSession.ID, Id);
        editor.putString(LocalSession.NAME,name);
        editor.putString(LocalSession.PHONE,phone);
        editor.putString(LocalSession.SHOP,shop);
        editor.putString(LocalSession.ROLE,role);
        editor.putString(LocalSession.MACTADDRESS,macaddress);
        editor.putString(LocalSession.PASSWORD,password);
        editor.putString(LocalSession.LOCAL, Locale.getDefault().getLanguage());
        editor.apply();
        editor.commit();
    }
    public void isaddtolist(Boolean ch){
        editor.putBoolean(LocalSession.ISAddTOLIST,ch);
        editor.apply();
        editor.commit();
    }

    public void lastDtae(String date){
        editor.putString(LocalSession.LASTLOGIN,date);
        editor.apply();
        editor.commit();
    }

    public static Boolean getIsSessionCreated() {
        return mPreferences.getBoolean(LocalSession.isSessionCreated, false);
    }


    public static String getId() {
        return mPreferences.getString(ID,"");
    }
    public static String getName() {
        return mPreferences.getString(NAME,"");
    }
    public static String getPhone() {
        return mPreferences.getString(PHONE,"");
    }
    public static String getPassword() {
        return mPreferences.getString(PASSWORD,"");
    }
    public static String getShop() {
        return mPreferences.getString(SHOP,"");
    }
    public static String getRole() {
        return mPreferences.getString(ROLE,"");
    }
    public static String getMactaddress() {
        return mPreferences.getString(MACTADDRESS,"");
    }
    public static String getToken() {
        return mPreferences.getString(TOKEN,"");
    }
    public static String getLastlogin() {
        return mPreferences.getString(LASTLOGIN,"");
    }

    public static Boolean getISAddTOLIST() {
        return mPreferences.getBoolean(ISAddTOLIST,true);
    }

    public static String getLocal() {
        return mPreferences.getString(LOCAL,"");
    }

    public static void setLocal(String lan,Context context) {
        editor.putString(LOCAL,lan);
        editor.apply();
        editor.commit();

        Locale locale = new Locale(lan);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config,
                context.getResources().getDisplayMetrics());
    }

    public static void clearSession() {
        editor.clear();
        editor.apply();
        editor.commit();


    }


}
