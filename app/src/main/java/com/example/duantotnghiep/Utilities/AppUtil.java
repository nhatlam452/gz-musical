package com.example.duantotnghiep.Utilities;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.example.duantotnghiep.Model.User;
import com.example.duantotnghiep.R;
import com.google.gson.Gson;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtil {
    public static String formatPhoneNumber(String phoneNumber) {
        return String.format("%s %s %s",phoneNumber.subSequence(0,3),"****",phoneNumber.subSequence(7,10));
    }

    public static User getUserInfo(Context context) {
        SharedPreferences mPrefs = context.getSharedPreferences("USER_INFO", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = mPrefs.getString("UserInfo", "");
        Log.d("==>", json);
        return gson.fromJson(json, User.class);
    }

    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Network network = connectivityManager.getActiveNetwork();
            if (network == null) {
                return false;
            }
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        } else {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
    }

    public static class ValidateInput {
        public static boolean isValidPhoneNumber(String phoneNumber) {
            final String PHONE_REGEX = "0" + "\\d{9}";
            Pattern pattern = Pattern.compile(PHONE_REGEX);
            Matcher matcher = pattern.matcher(phoneNumber);
            return matcher.matches();
        }


        public static boolean isValidPassword(String string) {
            String PATTERN;
            //The password must contain at least one lowercase character, one uppercase character, one digit, one special character, and a length between 8 to 15
            PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–{}:;',?/*~$^+=<>]).{8,15}$";
            Pattern pattern = Pattern.compile(PATTERN);
            Matcher matcher = pattern.matcher(string);
            return matcher.matches();
        }
//        public String getHashMD(String string) {
//            final String MD5 = "MD5";
//            try {
//                // Create MD5 Hash
//                MessageDigest digest = java.security.MessageDigest
//                        .getInstance(MD5);
//                digest.update(string.getBytes());
//                byte messageDigest[] = digest.digest();
//
//                // Create Hex String
//                StringBuilder hexString = new StringBuilder();
//                for (byte aMessageDigest : messageDigest) {
//                    String h = Integer.toHexString(0xFF & aMessageDigest);
//                    while (h.length() < 2)
//                        h = "0" + h;
//                    hexString.append(h);
//                }
//                return hexString.toString();
//
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            }
//            return "";
//        }
    }

    public static class showDialog {
        static Dialog progressDialog;

        public static void show(Context context) {
            progressDialog = new Dialog(context);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(R.layout.progess_dialog);
            Window window = progressDialog.getWindow();
            if (window == null) {
                return;
            }

            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams windowAttributes = window.getAttributes();
            windowAttributes.gravity = Gravity.CENTER;
            window.setAttributes(windowAttributes);
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        public static void dismiss() {
            progressDialog.dismiss();
        }
    }
}
