package com.yujongu.socialserviceagent;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreference {

    public static final String PREF_NAME = "Profile";

    public SharedPreference(){
        super();
    }

    public void saveData(Context context, String dataName, String data){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(dataName, data);
        editor.apply();
    }

    public String loadStringData(Context context, String dataName){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String dataResult;
        dataResult = sharedPreferences.getString(dataName, null);
        return dataResult;
    }

}
