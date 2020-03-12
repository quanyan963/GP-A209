package com.txtled.gp_a209.utils;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.txtled.gp_a209.R;


public class AlertUtils {
    //private static OnCreateThingListener thingListener;
    private static boolean canClose = false;

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
        void onConfirmClick(String friendlyName);
    }

//    public static OnCreateThingListener getThingListener(){
//        return thingListener;
//    }
//
//    public static void showAlertDialog(Context context, int viewId,
//                                       OnConfirmClickListener listener) {
//        canClose = false;
//        if (!((Activity) context).isFinishing()) {
//            LayoutInflater layoutInflater = LayoutInflater.from(context);
//            View view = layoutInflater.inflate(viewId, null);
//            TextInputEditText editText = (TextInputEditText) view.findViewById(R.id.alert_wwa_item);
//            TextView textView = (TextView) view.findViewById(R.id.tv_alert_create);
//            AlertDialog dialog = new AlertDialog.Builder(context)
//                    .setView(view)
//                    .setNegativeButton(R.string.cancel, (dialog12, which) -> dialog12.dismiss())
//                    .setPositiveButton(R.string.confirm,null)
//                    .create();
//            editText.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                    textView.setText("");
//                    if (!s.toString().trim().isEmpty()){
//                        if (!s.toString().matches("^[[A-Za-z]|\\s]+$")){
//                            textView.setHint(R.string.contain_hint);
//                            //setAlphaAnimation(textView);
//                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//                        }else {
//                            textView.setHint(R.string.wake_up_hint);
//                            //setAlphaAnimation(textView);
//                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
//                        }
//                    }else {
//                        textView.setHint(R.string.wake_up_hint);
//                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//                    }
//                }
//            });
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.setCancelable(true);
//            dialog.show();
//            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
//                if (canClose){
//                    dialog.dismiss();
//                }else {
//                    //editText.clearFocus();
//                    editText.setFocusable(false);
//                    listener.onConfirmClick(editText.getText().toString().trim());
//                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(false);
//                    Utils.hideSoftKeyboard(context,editText);
//                    dialog.setCancelable(false);
//
//                }
//            });
//            thingListener = new OnCreateThingListener() {
//                @Override
//                public void onStatueChange(int str) {
//                    ((Activity) context).runOnUiThread(() -> {
//                        if (str == R.string.device_name_used){
//                            editText.setFocusable(true);
//                            editText.setFocusableInTouchMode(true);
//                            editText.requestFocus();
//
//                        }else if (str == R.string.create_thing_fail){
//                            editText.setFocusable(true);
//                            editText.setFocusableInTouchMode(true);
//                            editText.requestFocus();
//                            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
//                            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(true);
//                            dialog.setCancelable(true);
//                        }
//                        textView.setText(str);
//                        setAlphaAnimation(textView);
//                    });
//                }
//
//                @Override
//                public void dismiss() {
//                    canClose = true;
//                    ((Activity) context).runOnUiThread(() -> {
//                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
//                        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setEnabled(true);
//                        dialog.setCancelable(true);
//                    });
//
//                }
//            };
//        }
//    }

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
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            dialog.setCancelable(true);
            if (!dialog.isShowing()) {
                dialog.show();
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
