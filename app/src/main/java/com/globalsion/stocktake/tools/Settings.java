package com.globalsion.stocktake.tools;

import android.content.Context;
import android.content.SharedPreferences;

public class Settings {
    //    private static SettingPath settingPath;
    private final SharedPreferences sharedPreferences;

//    public static SettingPath getInstance(Context context) {
//        if (settingPath == null) {
//            settingPath = new SettingPath(context);
//        }
//        return settingPath;
//    }

    public Settings(Context context) {
        sharedPreferences = context.getSharedPreferences("ASSETTRACKER",Context.MODE_PRIVATE);
    }

    public void saveData(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putString(key, value);
        prefsEditor.commit();
    }

    public String getData(String key) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }
}
