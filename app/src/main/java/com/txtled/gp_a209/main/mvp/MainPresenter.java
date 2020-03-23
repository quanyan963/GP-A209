package com.txtled.gp_a209.main.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import androidx.core.location.LocationManagerCompat;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateResult;
import com.txtled.gp_a209.add.adp.UDPBuild;
import com.txtled.gp_a209.add.listener.OnUdpSendRequest;
import com.txtled.gp_a209.application.MyApplication;
import com.txtled.gp_a209.base.CommonSubscriber;
import com.txtled.gp_a209.base.RxPresenter;
import com.txtled.gp_a209.bean.DeviceInfo;
import com.txtled.gp_a209.bean.WWADeviceInfo;
import com.txtled.gp_a209.broadcast.MyBroadcastReceiver;
import com.txtled.gp_a209.model.DataManagerModel;
import com.txtled.gp_a209.utils.RxUtil;
import com.txtled.gp_a209.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.txtled.gp_a209.base.BaseFragment.TAG;
import static com.txtled.gp_a209.utils.Constants.DB_NAME;
import static com.txtled.gp_a209.utils.Constants.DISCOVERY;
import static com.txtled.gp_a209.utils.Constants.THING_DIR;
import static com.txtled.gp_a209.utils.Constants.USER_ID;

/**
 * Created by Mr.Quan on 2019/12/9.
 */
public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {

    private DataManagerModel mDataManagerModel;
    //private CognitoCachingCredentialsProvider provider;
    //private AmazonDynamoDB client;
    private String userId;
    //private List<DeviceInfo> data;
    private boolean isNoWifi;
    private MyBroadcastReceiver mReceiver;
    private WifiInfo wifiInfo;
    private WifiManager myWifiManager;
    private DhcpInfo dhcpInfo;
    private Activity activity;
    private String broadCast;
    private List<WWADeviceInfo> refreshData;
    private String strReceive;
    private UDPBuild udpBuild;
    private Disposable timeCount;
    private int count;

    @Inject
    public MainPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public String init(Activity activity) {
        this.activity = activity;
        //provider = MyApplication.getCredentialsProvider();
        //client = new AmazonDynamoDBClient(provider);
        userId = mDataManagerModel.getUserId();

        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        if (isSDKAtLeastP()) {
            filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        }

        mReceiver = new MyBroadcastReceiver((context, info) -> onChanged(context, info));
        activity.registerReceiver(mReceiver, filter);

        return userId;
    }

    private void onChanged(Context context, WifiInfo info) {
        view.hidSnackBar();
        boolean disconnected = info == null
                || info.getNetworkId() == -1
                || "<unknown ssid>".equals(info.getSSID());
        if (disconnected) {
            isNoWifi = true;
            if (isSDKAtLeastP()) {
                LocationManager locationManager = (LocationManager) context
                        .getSystemService(Context.LOCATION_SERVICE);

                if (!(locationManager != null && LocationManagerCompat
                        .isLocationEnabled(locationManager))) {
                    view.checkLocation();
                } else {

                    view.setNoWifiView();
                }
            } else {
                view.setNoWifiView();
            }
        } else {
            isNoWifi = false;
            wifiInfo = info;
            String ssid = info.getSSID();
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
            view.getWifiName(ssid);
        }
    }

    public boolean isSDKAtLeastP() {
        return Build.VERSION.SDK_INT >= 28;
    }

    private void getBroadCastIp() {
        myWifiManager = ((WifiManager) activity.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE));
        dhcpInfo = myWifiManager.getDhcpInfo();
        String ip = Utils.getWifiIp(dhcpInfo.ipAddress);
        String netMask = Utils.getWifiIp(dhcpInfo.netmask);
        String[] ipTemp = ip.split("\\.");
        String[] maskTemp = netMask.split("\\.");
        broadCast = "";
        for (int i = 0; i < maskTemp.length; i++) {
            if (maskTemp[i].equals("255")) {
                broadCast += (ipTemp[i] + ".");
            } else {
                broadCast += (255 - Integer.parseInt(maskTemp[i])) + (i == maskTemp.length - 1 ? "" : ".");

            }
        }
    }

    @Override
    public void onRefresh() {
        if (!isNoWifi) {
            getBroadCastIp();
            refreshData = new ArrayList<>();
            udpSend(DISCOVERY, result -> {
            });
//            addSubscribe(Flowable.create((FlowableOnSubscribe<List<WWADeviceInfo>>) e -> {
//                //查数据
//                try {
//
//                    //data = getDeviceData();
//                    e.onNext();
//                }catch (Exception e1){
//                    e.onNext(data);
//                }
//            }, BackpressureStrategy.BUFFER).compose(RxUtil.rxSchedulerHelper())
//                    .subscribeWith(new CommonSubscriber<List<WWADeviceInfo>>(view) {
//                        @Override
//                        public void onNext(List<WWADeviceInfo> data) {
//                            //返回数据
//                            view.getDeviceData(data);
//                        }
//                    }));
        } else {
            view.closeRefresh();
        }

    }

    private void udpSend(String message, OnUdpSendRequest listener) {
        strReceive = "";
        udpBuild = UDPBuild.getUdpBuild();
        myWifiManager = ((WifiManager) activity.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE));
        dhcpInfo = myWifiManager.getDhcpInfo();
        udpBuild.setIgnoreIp(Utils.getWifiIp(dhcpInfo.ipAddress));
        udpBuild.setUdpReceiveCallback(data -> {
            strReceive = new String(data.getData(), 0, data.getLength());
            try {
                JSONObject deviceInfo = new JSONObject(strReceive);
                if (message.equals(DISCOVERY)) {
                    WWADeviceInfo info = new WWADeviceInfo(
                            deviceInfo.optString("ip"),
                            deviceInfo.optString("netmask"),
                            deviceInfo.optString("gw"),
                            deviceInfo.optString("host"),
                            deviceInfo.optString("port"),
                            deviceInfo.optString("cid"),
                            deviceInfo.optString("thing"),
                            deviceInfo.optString("friendlyname"),
                            deviceInfo.optString("ver")
                    );
                    refreshData.add(info);
                    willStop();
                } else {
                    listener.OnRequestListener(strReceive);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        udpBuild.sendMessage(message, broadCast);
        setTime(message, listener);
    }

    private void willStop() {
        if (timeCount != null) {
            timeCount.dispose();
        }
        timeCount = Observable.timer(5, TimeUnit.SECONDS).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(aLong -> {
                            if (!refreshData.isEmpty()) {
                                for (int i = 0; i < refreshData.size(); i++) {
                                    if (refreshData.get(i).getFriendlyNames() != null) {
                                        if (!refreshData.get(i).getFriendlyNames()
                                                .contains(userId)) {
                                            refreshData.remove(i);
                                        }
                                    } else {
                                        refreshData.remove(i);
                                    }
                                }
                                view.setData(refreshData);
                                udpBuild.stopUDPSocket();
                            }else {
                                view.noDevice();
                            }
                        }
                );
    }

    private void setTime(String message, OnUdpSendRequest listener) {

        addSubscribe(Flowable.timer(3, TimeUnit.SECONDS)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<Long>(view) {

                    @Override
                    public void onNext(Long aLong) {
                        if (refreshData.isEmpty()) {
                            count += 1;
                            if (count < 5) {
                                udpSend(message, listener);
                            } else {
                                count = 0;
                                view.noDevice();
                                addSubscribe(Flowable.timer(3, TimeUnit.SECONDS)
                                        .compose(RxUtil.rxSchedulerHelper())
                                        .subscribeWith(new CommonSubscriber<Long>(view) {

                                            @Override
                                            public void onNext(Long aLong) {
                                                view.hidSnackBar();
                                            }
                                        }));
                            }
                            //udpBuild.sendMessage(message,broadCast);
                        } else {
                            count = 0;
                        }
                    }
                }));
    }

//    private List<DeviceInfo> getDeviceData() {
//        data = new ArrayList<>();
//        HashMap<String, AttributeValue> key = new HashMap<>();
//        key.put(USER_ID, new AttributeValue().withS(userId));
//        //获取数据
//        GetItemResult itemResult = client.getItem(new GetItemRequest()
//                .withTableName(DB_NAME).withKey(key));
//        if (itemResult.getItem() != null) {
//            Map<String, AttributeValue> resultItem = itemResult.getItem();
//            AttributeValue cert_data = resultItem.get(THING_DIR);
//            //设备名称
//            String[] names = cert_data.getM().keySet().toArray(new String[cert_data.getM().size()]);
//            //endpointId
//            for (int i = 0; i < names.length; i++) {
//                data.add(new DeviceInfo(names[i], cert_data.getM().get(names[i]).getS()));
//            }
//        }
//        return data;
//    }
}
