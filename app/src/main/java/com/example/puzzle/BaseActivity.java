package com.example.puzzle;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.puzzle.dialog.CustomDialog;
import com.example.puzzle.dialog.CustomDialogWithTwoButtons;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    public static boolean isForeground;
    protected static final String DIALOG_TAG = "DIALOG_TAG";
    @Override
    public void setContentView(final int layoutResID) {
        try {
            super.setContentView(layoutResID);
            ButterKnife.bind(this);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Override
    public void setContentView(final View view) {
        try {
            super.setContentView(view);
            ButterKnife.bind(this);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Override
    public void setContentView(final View view, final ViewGroup.LayoutParams params) {
        try {
            super.setContentView(view, params);
            ButterKnife.bind(this);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Override
    public void addContentView(final View view, final ViewGroup.LayoutParams params) {
        try {
            super.addContentView(view, params);
            ButterKnife.bind(this);
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try{
            isForeground = true;
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            isForeground = false;
        }catch (IllegalArgumentException exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    protected void showForegroundToast(String text) {
        try {
            if (isForeground) {
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    protected boolean isForeground() {
        return isForeground;
    }


    protected void showCustomDialog(String title, String message, String action, String status, CustomDialog.OnDismissListener listener) {
        try {
            CustomDialog customDialog = CustomDialog.newInstance(title, message, action, status);
            customDialog.setListener(null);
            if(listener != null) {
                customDialog.setListener(listener);
            }
            getFragmentManager().beginTransaction()
                    .add(customDialog, DIALOG_TAG)
                    .commitAllowingStateLoss();
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    protected void showCustomDialogWithTwoButtons(String title, String message, String action, String status, CustomDialogWithTwoButtons.OnDismissListener listener) {
        try {
            CustomDialogWithTwoButtons customDialog = CustomDialogWithTwoButtons.newInstance(title, message, action, status);
            customDialog.setListener(null);
            if(listener != null) {
                customDialog.setListener(listener);
            }
            getFragmentManager().beginTransaction()
                    .add(customDialog, DIALOG_TAG)
                    .commitAllowingStateLoss();
        } catch (Exception exception){
            Log.e("Error ==> ", "" + exception);
        }
    }

    public boolean isNetworkConnectionAvailable() {
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
