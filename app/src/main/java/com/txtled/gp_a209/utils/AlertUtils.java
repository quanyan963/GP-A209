package com.txtled.gp_a209.utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.txtled.gp_a209.R;
import com.txtled.gp_a209.widget.ArialRoundButton;
import com.txtled.gp_a209.widget.ArialRoundTextView;


public class AlertUtils {
    //private static OnCreateThingListener thingListener;
    private static boolean canClose = false;
    public static OnConfirmClickListener clickListener;

    public static void showErrorMessage(Context context, int titleRes,
                                        String errorCode, DialogInterface.OnClickListener listener) {
//        if (!((Activity) context).isFinishing()) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(context)
//                    .setMessage(context.getResources().getIdentifier("ERROR_CODE_" + errorCode,
//                            "string", context.getPackageName()));
//            if (titleRes != 0) {
//                builder.setTitle(titleRes);
//            }
//            if (listener == null) {
//                builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//            } else {
//                builder.setNegativeButton(R.string.ok, listener);
//            }
//            Dialog dialog = builder.create();
//            dialog.setCancelable(true);
//            dialog.show();
//        }
    }

    public static void showErrorMessage(Context context, String errorCode) {
        showErrorMessage(context, 0, errorCode, null);
    }

    public static void showErrorMessage(Context context,
                                        String errorCode, DialogInterface.OnClickListener listener) {
        showErrorMessage(context, 0, errorCode, listener);
    }

    public static void showAlertDialog(Context context, String message,
                                       DialogInterface.OnClickListener listener0,
                                       DialogInterface.OnClickListener listener1) {
//        if (!((Activity) context).isFinishing()) {
//            AlertDialog dialog = new AlertDialog.Builder(context)
//                    .setMessage(message)
//                    .setNegativeButton(R.string.cancel, listener0)
//                    .setPositiveButton(R.string.confirm, listener1)
//                    .create();
//            dialog.setCancelable(true);
//            dialog.show();
//        }
    }

    public interface OnConfirmClickListener{
        void onConfirmClick(boolean b);
    }

    public static void setListener(OnConfirmClickListener listener){
        clickListener = listener;
    }

//    public static OnCreateThingListener getThingListener(){
//        return thingListener;
//    }

    public static int width;

    public static void showHintDialog(Context context, int viewId,String title,
                                      int msg, boolean isConfig){
        if (!((Activity) context).isFinishing()){
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(viewId,null);
            ArialRoundButton abtOk = view.findViewById(R.id.abt_ok);
            ArialRoundTextView atvHintTitle = view.findViewById(R.id.atv_hint_title);
            ArialRoundTextView atvHintMsg = view.findViewById(R.id.atv_hint_msg);
            atvHintTitle.setText(title);
            atvHintMsg.setText(msg);
            abtOk.setText(isConfig ? R.string.next : R.string.ok);
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setView(view)
                    .create();
            dialog.setCancelable(true);
            dialog.show();
            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.dialogWindowAnimInToOut);
            window.setBackgroundDrawable(context.getResources()
                    .getDrawable(R.drawable.background_white));
            abtOk.setOnClickListener(v -> {
                dialog.dismiss();
                clickListener.onConfirmClick(isConfig);
            });
        }
    }

    public static AlertDialog showProgressDialog(Context context,String wifiName,String pass){
        if (!((Activity) context).isFinishing()){
            LayoutInflater inflater = LayoutInflater.from(context);
            View config = inflater.inflate(R.layout.alert_configure,null);
            ArialRoundTextView atvWifi = config.findViewById(R.id.atv_wifi);
            ArialRoundTextView atvPass = config.findViewById(R.id.atv_pass);
            atvWifi.setText(context.getString(R.string.config_wifi,wifiName));
            atvPass.setText(pass);
            AlertDialog configDialog = new AlertDialog.Builder(context,R.style.TransparentDialog)
                    .setView(config)
                    .create();
            configDialog.setCancelable(false);
            configDialog.show();
            Window cWindow = configDialog.getWindow();

            ViewTreeObserver vto = atvWifi.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    atvWifi.getViewTreeObserver().removeOnPreDrawListener(this);
                    width = atvWifi.getMeasuredWidth();
                    return true;
                }
            });

            ViewTreeObserver passVto = atvPass.getViewTreeObserver();
            passVto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    atvPass.getViewTreeObserver().removeOnPreDrawListener(this);
                    width = atvPass.getMeasuredWidth() > width ?
                            atvPass.getMeasuredWidth() : width;
                    cWindow.setLayout(width+60, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    return true;
                }
            });
            cWindow.setWindowAnimations(R.style.dialogWindowAnimInToOut);
            cWindow.setBackgroundDrawable(context.getResources()
                    .getDrawable(R.drawable.background_white));
            return configDialog;
        }else {
            return null;
        }
    }

    public static AlertDialog showProgressDialog(Context context){
        if (!((Activity) context).isFinishing()){
            LayoutInflater inflater = LayoutInflater.from(context);
            View config = inflater.inflate(R.layout.alert_configure,null);
            ArialRoundTextView atvWifi = config.findViewById(R.id.atv_wifi);
            ArialRoundTextView atvPass = config.findViewById(R.id.atv_pass);
            atvWifi.setText(R.string.uploading);
            atvPass.setVisibility(View.INVISIBLE);
            AlertDialog configDialog = new AlertDialog.Builder(context,R.style.TransparentDialog)
                    .setView(config)
                    .create();
            configDialog.setCancelable(false);
            configDialog.show();
            Window cWindow = configDialog.getWindow();

            ViewTreeObserver vto = atvWifi.getViewTreeObserver();
            vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                public boolean onPreDraw() {
                    atvWifi.getViewTreeObserver().removeOnPreDrawListener(this);
                    width = atvWifi.getMeasuredWidth();
                    cWindow.setLayout(width+60, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    return true;
                }
            });
            cWindow.setWindowAnimations(R.style.dialogWindowAnimInToOut);
            cWindow.setBackgroundDrawable(context.getResources()
                    .getDrawable(R.drawable.background_yellow));
            return configDialog;
        }else {
            return null;
        }
    }

    private static void setAlphaAnimation(View view){
        AlphaAnimation animation = new AlphaAnimation(0f, 1f);
        animation.setDuration(500);
        view.setAnimation(animation);
        animation.start();
    }

    public static AlertDialog showLoadingDialog(Context context, int viewId){
        if (!((Activity) context).isFinishing()) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View view = layoutInflater.inflate(viewId, null);
            AlertDialog dialog = new AlertDialog.Builder(context, R.style.TransparentDialog)
                    .setView(view)
                    .create();
            dialog.setCancelable(false);
            return dialog;
        }else {
            return null;
        }
    }

    public static void showAlertDialog(Context context, int title, int message) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
            dialog.setCancelable(true);
            dialog.show();
        }
    }

    public static void showAlertDialog(Context context, String message) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setMessage(message)
                    .setPositiveButton(R.string.ok, (dialog1, which) -> dialog1.dismiss())
                    .create();
            dialog.setCancelable(true);
            dialog.show();
        }
    }

    public static void showAlertDialog(Context context, int messageRes) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setMessage(messageRes)
                    .setPositiveButton(R.string.ok, (dialog1, which) -> dialog1.dismiss())
                    .create();
            dialog.setCancelable(true);
            if (!dialog.isShowing()) {
                dialog.show();
                Window window = dialog.getWindow();
                window.setWindowAnimations(R.style.dialogWindowAnimInToOut);
                window.setBackgroundDrawable(context.getResources()
                        .getDrawable(R.drawable.background_white));
            }
        }
    }

    public static void showAlertDialog(Context context, int messageRes,
                                       DialogInterface.OnClickListener listener) {
        if (!((Activity) context).isFinishing()) {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setMessage(messageRes)
                    .setPositiveButton(R.string.ok, listener)
                    .create();
            dialog.setCancelable(false);
            dialog.show();
            Window window = dialog.getWindow();
            window.setWindowAnimations(R.style.dialogWindowAnimInToOut);
            window.setBackgroundDrawable(context.getResources()
                    .getDrawable(R.drawable.background_white));
        }
    }

    public static void showProgressDialog(Context context, int id) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getString(id));
        progressDialog.show();
    }
}
