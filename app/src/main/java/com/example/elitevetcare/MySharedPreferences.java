package com.example.elitevetcare;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class MySharedPreferences {
    final static  String MY_SHARED_FREFERENCES = "ELITEVETCARE_FREFERENCES";
    Context mContext;
    public MySharedPreferences(Context mContext){
        this.mContext = mContext;
    }

    public void putBooleanValue(String key, Boolean value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_FREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }
    public Boolean getBooleanValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_FREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);
    }

    public void putStringValue(String key, String value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_FREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }
    public String getStringValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_FREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }
    public void putStringSetValue(String key, Set<String> value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_FREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(key,value);
        editor.apply();
    }
    public Set<String> getStringSetValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_FREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getStringSet(key,null);
    }
    public void putIntValue(String key, Integer value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_FREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }
    public Integer getIntValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_FREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,-1);
    }
    public void putLongValue(String key, Long value){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_FREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key,value);
        editor.apply();
    }
    public Long getLongValue(String key){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARED_FREFERENCES,Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key,0);
    }
}
