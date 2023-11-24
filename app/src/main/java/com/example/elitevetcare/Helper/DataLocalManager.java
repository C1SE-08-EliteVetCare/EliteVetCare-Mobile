package com.example.elitevetcare.Helper;

import android.content.Context;

public class DataLocalManager {
    public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    public static final String REFRESH_TOKEN_TIMEUP = "REFRESH_TOKEN_TIME";
    public static final String ACCESS_TOKEN_TIMEUP = "ACCESS_TOKEN_TIME";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static DataLocalManager instance = null;
    public static MySharedPreferences mySharedPreferences;
    public static void init(Context mContext){
        instance = new DataLocalManager();
        mySharedPreferences = new MySharedPreferences(mContext);
    }

    public static DataLocalManager getInstance(){
        if (instance == null)
            instance = new DataLocalManager();
        return instance;
    }

    public static void setRefreshToken(String Token){
        Long TimeUP = System.currentTimeMillis()+ 29 * 24 * 60 * 60 * 1000L;
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(REFRESH_TOKEN,Token);
        DataLocalManager.getInstance().mySharedPreferences.putLongValue(REFRESH_TOKEN_TIMEUP,TimeUP);
    }
    public static String GetRefreshToken(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(REFRESH_TOKEN);
    }
    public static Long GetRefreshTokenTimeUp(){
        return DataLocalManager.getInstance().mySharedPreferences.getLongValue(REFRESH_TOKEN_TIMEUP);
    }
    public static void ResetRefreshToken(){
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(REFRESH_TOKEN,"");
        DataLocalManager.getInstance().mySharedPreferences.putLongValue(REFRESH_TOKEN_TIMEUP, Long.valueOf(0));
    }
    public static void ResetAccessTokens(){
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(ACCESS_TOKEN,"");
        DataLocalManager.getInstance().mySharedPreferences.putLongValue(ACCESS_TOKEN_TIMEUP,Long.valueOf(0));
    }

    public static void setAccessTokens(String Token){
        Long TimeUP = System.currentTimeMillis()+ 29 * 60 * 1000L;
        DataLocalManager.getInstance().mySharedPreferences.putStringValue(ACCESS_TOKEN,Token);
        DataLocalManager.getInstance().mySharedPreferences.putLongValue(ACCESS_TOKEN_TIMEUP,TimeUP);
    }
    public static String GetAccessToken(){
        return DataLocalManager.getInstance().mySharedPreferences.getStringValue(ACCESS_TOKEN);
    }
    public static Long GetAccessTokenTimeUp(){
        return DataLocalManager.getInstance().mySharedPreferences.getLongValue(ACCESS_TOKEN_TIMEUP);
    }
}
