package com.example.duantotnghiep.Utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.duantotnghiep.Model.User;
import com.google.gson.Gson;

public class LocalStorage {
    private static LocalStorage instance;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static LocalStorageManager localStorageManager;
    private final Context context;

    private LocalStorage(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static LocalStorage getInstance(Context context) {
        if (instance == null) {
            instance = new LocalStorage(context);
        }
        return instance;
    }

    public LocalStorageManager getLocalStorageManager() {
        return localStorageManager;
    }

    public static class LocalStorageManager {

        public static User getUserInfo() {
            Gson gson = new Gson();
            String json = sharedPreferences.getString("UserInfo", "");
            Log.d("get User Info ==>", json);
            return gson.fromJson(json, User.class);
        }

        public static void setUserInfo(User user) {
            if (editor != null){
                editor.clear();
            }
            Gson gson = new Gson();
            String json = gson.toJson(user);
            editor.putString("UserInfo", json);
            Log.d("====> ", "User : " + json);
            editor.apply();
        }
    }
}
