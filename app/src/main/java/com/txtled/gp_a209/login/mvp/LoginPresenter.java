package com.txtled.gp_a209.login.mvp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

import androidx.core.location.LocationManagerCompat;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.ProfileScope;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.txtled.gp_a209.R;
import com.txtled.gp_a209.application.MyApplication;
import com.txtled.gp_a209.base.CommonSubscriber;
import com.txtled.gp_a209.base.RxPresenter;
import com.txtled.gp_a209.model.DataManagerModel;
import com.txtled.gp_a209.utils.RxUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.WIFI_SERVICE;
import static com.txtled.gp_a209.base.BaseActivity.TAG;

/**
 * Created by Mr.Quan on 2020/3/12.
 */
public class LoginPresenter extends RxPresenter<LoginContract.View> implements LoginContract.Presenter {

    private DataManagerModel mDataManagerModel;
    private RequestContext mRequestContext;
    private String userId;
    private CognitoCachingCredentialsProvider provider;
    private AmazonDynamoDB client;
    private Activity activity;
    private boolean back = false;

    @Inject
    public LoginPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public void init(Activity activity) {
        this.activity = activity;
        provider = MyApplication.getCredentialsProvider();
        //client = new AmazonDynamoDBClient(provider);
        userId = mDataManagerModel.getUserId();
        mRequestContext = RequestContext.create(activity);
        mRequestContext.registerListener(new AuthorizeListenerImpl());
        registerBroadcast(activity);
    }

    public boolean isSDKAtLeastP() {
        return Build.VERSION.SDK_INT >= 28;
    }

    //广播监听网络变化
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null) {
                return;
            }

            WifiManager wifiManager = (WifiManager) context.getApplicationContext()
                    .getSystemService(WIFI_SERVICE);
            assert wifiManager != null;

            switch (action) {
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                case LocationManager.PROVIDERS_CHANGED_ACTION:
                    onWifiChanged(context,wifiManager.getConnectionInfo());
                    break;
            }
        }
    };

    /**
     * 注册广播
     * @param activity
     */
    private void registerBroadcast(Activity activity) {
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        if (isSDKAtLeastP()) {
            filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        }
        activity.registerReceiver(mReceiver, filter);
    }


    /**
     * 判断网络状态变化
     * @param context
     * @param info
     */
    private void onWifiChanged(Context context,WifiInfo info) {
        view.hidSnackBar();
        boolean disconnected = info == null
                || info.getNetworkId() == -1
                || "<unknown ssid>".equals(info.getSSID());
        if (disconnected) {

            if (isSDKAtLeastP()) {
                LocationManager locationManager = (LocationManager) context
                        .getSystemService(Context.LOCATION_SERVICE);

                if (!(locationManager != null && LocationManagerCompat
                        .isLocationEnabled(locationManager))){
                    view.checkLocation();
                }else {
                    view.setNoWifiView();
                }
            }else {
                view.setNoWifiView();
            }
        } else {
            String ssid = info.getSSID();
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
            view.setInfo(ssid, info);
        }
    }

    /**
     * 所有按钮点击监听事件
     * @param id 控件id
     * @param type 类型
     */
    @Override
    public void viewClick(int id, int type) {
        switch (id){
            case R.id.abt_login:
                view.showLoadingView();
                if (type == 0){
                    userId = "";
                    mDataManagerModel.setUserId(userId);
                    mDataManagerModel.setEmail("");
                    AuthorizationManager.authorize(
                            new AuthorizeRequest.Builder(mRequestContext)
                                    .addScopes(ProfileScope.profile(),ProfileScope.postalCode())
                                    .build());
                }else {
                    back = true;
                    AuthorizationManager.signOut(activity, new Listener<Void, AuthError>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            userId = "";
                            mDataManagerModel.setUserId(userId);
                            mDataManagerModel.setEmail("");
                            view.signOut();
                        }

                        @Override
                        public void onError(AuthError authError) {
                            view.signOutFail();
                            hidSnackBarDelay();
                        }
                    });
                }

                break;
        }
    }

    /**
     * 延时关闭底部弹窗
     */
    private void hidSnackBarDelay(){
        addSubscribe(Flowable.timer(3, TimeUnit.SECONDS)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<Long>(view) {
                    @Override
                    public void onNext(Long aLong) {
                        view.hidSnackBar();
                    }
                }));
    }

    @Override
    public void onResume(boolean isShowing) {
        mRequestContext.onResume();
        if (isShowing){
            Observable.timer(8, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
                if (userId.equals("")){
                    view.showLoginFail();
                    addSubscribe(Flowable.timer(3,TimeUnit.SECONDS)
                            .compose(RxUtil.rxSchedulerHelper())
                            .subscribeWith(new CommonSubscriber<Long>(view){

                                @Override
                                public void onNext(Long aLong) {
                                    view.hidSnackBar();
                                }
                            }));

                }
            });
        }
    }

    @Override
    public void onDestroy(Activity activity) {
        activity.unregisterReceiver(mReceiver);
    }

    /**
     * 亚马逊登录返回监听
     */
    private class AuthorizeListenerImpl extends AuthorizeListener {
        @Override
        public void onSuccess(final AuthorizeResult authorizeResult) {
            //Utils.Logger(TAG,"userId:",authorizeResult.getUser().getUserId());
            String[] values = authorizeResult.getUser().getUserId().split("\\.");
            userId = values[values.length - 1];
            String token = authorizeResult.getAccessToken();

            //authorizeResult.getUser().getUserId();
            if (null != token) {

                /* 用户已登录，联合登录Cognito*/
                Map<String, String> logins = new HashMap<String, String>();
                logins.put("www.amazon.com", token);
                provider.setLogins(logins);
                client = new AmazonDynamoDBClient(provider);
                //getIdentity();
            } else {

                /* The user is not signed in */

            }
            mDataManagerModel.setUserId(userId);

            mDataManagerModel.setEmail(authorizeResult.getUser().getUserEmail());
            //view.setUserId(userId);
            view.toMainView(back);
        }

        @Override
        public void onError(final AuthError authError) {
            view.hidLoadingView();
            Log.e(TAG, "AuthError during authorization", authError);
            //activity.runOnUiThread(() -> view.showAlertDialog(authError));
        }

        @Override
        public void onCancel(final AuthCancellation authCancellation) {
            view.hidLoadingView();
            Log.e(TAG, "User cancelled authorization");
            //view.hidProgress();
        }
    }
}
