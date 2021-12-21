package com.example.puzzle.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

public final class Utils {

    private Utils() {
    }

    /**
     * Hides soft keyboard.
     *
     * @param activity Activity for getting IMS and current focus.
     */
    public static void hideSoftKeyboard(@NonNull Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    /**
     * Show soft keyboard.
     *
     * @param activity Activity for getting IMS and current focus.
     */
    public static void showSoftKeyboard(@NonNull Activity activity, @NonNull EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null) {
            inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static boolean isNetworkConnectionAvailable(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info == null) return false;
            NetworkInfo.State network = info.getState();
            return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
        return false;
    }
}
