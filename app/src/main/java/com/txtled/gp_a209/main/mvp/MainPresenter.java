package com.txtled.gp_a209.main.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.view.View;

import androidx.core.location.LocationManagerCompat;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.client.AWSIotDevice;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.model.CertificateStatus;
import com.amazonaws.services.iot.model.DeleteThingRequest;
import com.amazonaws.services.iot.model.DeleteThingResult;
import com.amazonaws.services.iot.model.DetachPolicyRequest;
import com.amazonaws.services.iot.model.DetachSecurityProfileRequest;
import com.amazonaws.services.iot.model.DetachThingPrincipalRequest;
import com.amazonaws.services.iot.model.UpdateCertificateRequest;
import com.txtled.gp_a209.R;
import com.txtled.gp_a209.add.listener.OnUdpSendRequest;
import com.txtled.gp_a209.add.udp.UDPBuild;
import com.txtled.gp_a209.application.MyApplication;
import com.txtled.gp_a209.base.CommonSubscriber;
import com.txtled.gp_a209.base.RxPresenter;
import com.txtled.gp_a209.bean.DeviceInfo;
import com.txtled.gp_a209.bean.WWADeviceInfo;
import com.txtled.gp_a209.broadcast.MyBroadcastReceiver;
import com.txtled.gp_a209.control.mqtt.MqttClient;
import com.txtled.gp_a209.control.mqtt.MyShadowMessage;
import com.txtled.gp_a209.control.mqtt.listener.OnConnectListener;
import com.txtled.gp_a209.control.mqtt.listener.OnMessageListener;
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
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.txtled.gp_a209.base.BaseActivity.TAG;
import static com.txtled.gp_a209.utils.Constants.DATA_DEVICE;
import static com.txtled.gp_a209.utils.Constants.DB_NAME;
import static com.txtled.gp_a209.utils.Constants.DISCOVERY;
import static com.txtled.gp_a209.utils.Constants.FRIENDLY_NAME;
import static com.txtled.gp_a209.utils.Constants.MY_OIT_CE;
import static com.txtled.gp_a209.utils.Constants.PUBLISH;
import static com.txtled.gp_a209.utils.Constants.REST_API;
import static com.txtled.gp_a209.utils.Constants.SEND_THING_NAME;
import static com.txtled.gp_a209.utils.Constants.THING_DIR;
import static com.txtled.gp_a209.utils.Constants.USER_ID;
import static com.txtled.gp_a209.utils.ForUse.ACCESS_KEY;
import static com.txtled.gp_a209.utils.ForUse.SECRET_ACCESS_KEY;

/**
 * Created by Mr.Quan on 2019/12/9.
 */
public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {

    private DataManagerModel mDataManagerModel;
    private CognitoCachingCredentialsProvider provider;
    private AmazonDynamoDB client;
    private String userId;
    private List<WWADeviceInfo> data;
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
    private AWSIotClient awsIot;
    private boolean allOff;

    @Inject
    public MainPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public String init(Activity activity) {
        this.activity = activity;
        provider = MyApplication.getCredentialsProvider();
        client = new AmazonDynamoDBClient(provider);
        userId = mDataManagerModel.getUserId();
        createIotService();

        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        if (isSDKAtLeastP()) {
            filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        }

        mReceiver = new MyBroadcastReceiver((context, info) -> onChanged(context, info));
        activity.registerReceiver(mReceiver, filter);

        return userId;
    }

    /**
     * 创建service
     */
    private void createIotService() {
        try {
            AWSCredentials credentials = new BasicAWSCredentials(ACCESS_KEY, SECRET_ACCESS_KEY);

            ClientConfiguration clientConfig = new ClientConfiguration();

            clientConfig.setProtocol(Protocol.HTTPS);

            awsIot = new AWSIotClient(credentials, clientConfig);

        } catch (Exception e) {
            Utils.Logger(TAG, "IotServiceUtil.createIotService aws-iot创建连接异常", e.getMessage());
            //LOGGER.error("IotServiceUtil.createIotService aws-iot创建连接异常",e);
            awsIot = null;
        }
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
                    //view.checkLocation();
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

    /**
     * 根据本机ip得出广播地址
     */
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

    /**
     * 列表刷新
     */
    @Override
    public void onRefresh() {
        addSubscribe(Flowable.create((FlowableOnSubscribe<List<WWADeviceInfo>>) e -> {
            //查数据
            try {
                discovery();
                e.onNext(getDeviceData());
            }catch (Exception e1){
                refreshData = new ArrayList<>();
                e.onNext(refreshData);
            }
        }, BackpressureStrategy.BUFFER).compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<List<WWADeviceInfo>>(view) {
                    @Override
                    public void onNext(List<WWADeviceInfo> data) {
                        //返回数据
                        view.getDeviceData(data);
                    }
                }));

    }

    private List<WWADeviceInfo> getDeviceData() {
        refreshData = new ArrayList<>();
        HashMap<String, AttributeValue> key = new HashMap<>();
        key.put(USER_ID, new AttributeValue().withS(userId));
        //获取数据
        GetItemResult itemResult = client.getItem(new GetItemRequest()
                .withTableName(DB_NAME).withKey(key));
        if (itemResult.getItem() != null) {
            Map<String, AttributeValue> resultItem = itemResult.getItem();
            AttributeValue cert_data = resultItem.get(THING_DIR);
            //设备名称
            String[] names = cert_data.getM().keySet().toArray(new String[cert_data.getM().size()]);
            //endpointId
            for (int i = 0; i < names.length; i++) {
                WWADeviceInfo info = new WWADeviceInfo();
                info.setFriendlyNames(userId+"_"+names[i]);
                info.setThing(cert_data.getM().get(names[i]).getS());
                refreshData.add(info);
            }
        }
        return refreshData;
    }

    /**
     * 删除设备
     * @param data
     * @param name
     */
    @Override
    public void deleteDevice(WWADeviceInfo data, String name) {
        broadCast = data.getIp();
        addSubscribe(Flowable.just(name).subscribeOn(Schedulers.io()).observeOn(Schedulers.io())
                    .subscribeWith(new CommonSubscriber<String>(view) {
                        @Override
                        public void onNext(String name) {
                            //查数据
                            try {
                                //delete DB
                                String[] names = data.getFriendlyNames().split(",");
                                if (names.length > 1){
                                    //只把自己的friendlyName部分删掉
                                    if (deleteDB(name)){
                                        StringBuffer buffer = new StringBuffer();
                                        for (int i = 0; i < names.length; i++) {
                                            if (!names[i].contains(userId)){
                                                buffer.append(names[i] + (i == names.length - 1 ? "" : ","));
                                            }
                                        }
                                        udpSend(String.format(FRIENDLY_NAME, buffer.toString()), result -> {
                                            if (result.contains("\"friendlyname\":1")){
                                                view.deleteSuccess();
                                            }else {
                                                udpBuild.sendMessage(String.format(FRIENDLY_NAME,
                                                        buffer.toString()),broadCast);
                                            }
                                        });
                                    }else {
                                        view.deleteError();
                                        hidSnackBarDelay();
                                    }
                                }else {
                                    //删除事物 未完成
                                    view.deleteSuccess();
//                                    awsIot.detachThingPrincipal(new DetachThingPrincipalRequest()
//                                            .withThingName(data.getThing())
//                                            .withPrincipal("1f29341c16b40760b8d2d6c8783682bcc6ecec6d828746fe459321f85e79b243"));
//                                    awsIot.updateCertificate(new UpdateCertificateRequest()
//                                            .withCertificateId("1f29341c16b40760b8d2d6c8783682bcc6ecec6d828746fe459321f85e79b243")
//                                            .withNewStatus("INACTIVE"));
//                                    awsIot.detachPolicy(new DetachPolicyRequest()
//                                            .withPolicyName(MY_OIT_CE)
//                                            .withTarget("arn:aws:iot:us-east-1:612535970613:cert/1f29341c16b40760b8d2d6c8783682bcc6ecec6d828746fe459321f85e79b243"));
//                                    //awsIot.detachSecurityProfile(new DetachSecurityProfileRequest().)
//                                    awsIot.detachThingPrincipal(new DetachThingPrincipalRequest().withThingName(data.getThing()));

//                                    DeleteThingResult request = awsIot.deleteThing(new DeleteThingRequest()
//                                            .withThingName(data.getThing()));
//                                    //删除设备所有信息
//                                    if (deleteDB(name)){
//                                        //delete thing
//
//                                        if (!request.toString().isEmpty()) {
//                                            //写入设备
//                                            writeToDevice();
//                                            view.deleteSuccess();
//                                        }else {
//                                            view.deleteError();
//                                            hidSnackBarDelay();
//                                        }
//                                    }else {
//                                        view.deleteError();
//                                        hidSnackBarDelay();
//                                    }
                                }
                            } catch (Exception e1) {
                                view.deleteError();
                                hidSnackBarDelay();
                            }
                        }
                    }));

    }

    /**
     * 按钮点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.abt_off_all:
                view.showLoading();
                if (!refreshData.isEmpty()){
                    for (WWADeviceInfo info : refreshData) {
                        MqttClient.getClient().initClient(info.getThing(), new OnConnectListener() {
                            @Override
                            public void onSuccess(AWSIotDevice device) {
                                AWSIotDevice iotDevice = device;
                                powerOff(iotDevice,info.getThing());
                            }
                            @Override
                            public void onFail() {
                                view.mqttInitFail();
                                hidSnackBarDelay();
                            }
                        });
                    }
                }
                break;
        }
    }

    @Override
    public void discovery() {
        if (!isNoWifi) {
            getBroadCastIp();
            view.showSearching();
            if (udpBuild != null){
                System.out.println("discovery执行111111111");
                udpBuild.stopUDPSocket();
            }
            udpSend(DISCOVERY, result -> {
            });

        }
    }

    @Override
    public void onDestroy() {
        activity.unregisterReceiver(mReceiver);
    }

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

    private void powerOff(AWSIotDevice iotDevice, String endpoint) {
        try {
            MyShadowMessage myMessage = new MyShadowMessage(String.format(PUBLISH,endpoint),
                    AWSIotQos.QOS0,String.format(DATA_DEVICE,allOff == true ? "\"on\"" : "\"off\""));
            myMessage.setListener(new OnMessageListener() {
                @Override
                public void onSuccess() {
                    allOff = !allOff;
                    view.success(allOff);
                }

                @Override
                public void onFailure() {
                    view.fail();
                    hidSnackBarDelay();
                }

                @Override
                public void onTimeout() {
                    view.fail();
                    hidSnackBarDelay();
                }
            });
            iotDevice.update(myMessage,5000);
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
    }

    private void writeToDevice() {
        udpSend(String.format(FRIENDLY_NAME, ""), result -> {
            if (result.contains("\"friendlyname\"")){
                udpBuild.sendMessage(result.contains("1") ?
                        String.format(SEND_THING_NAME, REST_API, "") :
                        String.format(FRIENDLY_NAME,"") ,broadCast);
            }else {
                if (result.contains("\"endpoint\":1")){
                    view.deleteSuccess();
                }else {
                    udpBuild.sendMessage(String.format(SEND_THING_NAME, REST_API, ""),broadCast);
                }
            }
        });
    }

    /**
     * 发送udp数据
     * @param message 内容
     * @param listener 监听
     */
    private void udpSend(String message, OnUdpSendRequest listener) {
        data = new ArrayList<>();
        strReceive = "";
        udpBuild = UDPBuild.getUdpBuild();
        myWifiManager = ((WifiManager) activity.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE));
        dhcpInfo = myWifiManager.getDhcpInfo();
        udpBuild.setIgnoreIp(Utils.getWifiIp(dhcpInfo.ipAddress));
        udpBuild.setUdpReceiveCallback(result -> {
            strReceive = new String(result.getData(), 0, result.getLength());
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
                    data.add(info);
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
                            if (!data.isEmpty()) {
                                for (int i = 0; i < data.size(); i++) {
                                    if (data.get(i).getFriendlyNames() != null) {
                                        if (!data.get(i).getFriendlyNames()
                                                .contains(userId)) {
                                            data.remove(i);
                                        }
                                    } else {
                                        data.remove(i);
                                    }
                                }
                                view.setData(data);
                                udpBuild.stopUDPSocket();
                                System.out.println("discovery执行");

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
                        if (data.isEmpty()) {
                            count += 1;
                            if (count < 3) {
                                udpSend(message, listener);
                            } else {
                                count = 0;
                                view.noDevice();
                                //hidSnackBarDelay();
                            }
                            //udpBuild.sendMessage(message,broadCast);
                        } else {
                            count = 0;
                        }
                    }
                }));
    }

    /**
     * 删除数据库数据
     * @param name
     * @return
     */
    private boolean deleteDB(String name) {
        HashMap<String, AttributeValue> key = new HashMap<>();
        key.put(USER_ID, new AttributeValue().withS(userId));
        //获取数据
        GetItemResult itemResult = client.getItem(new GetItemRequest()
                .withTableName(DB_NAME).withKey(key));
        if (itemResult.getItem() != null) {
            Map<String, AttributeValue> resultItem = itemResult.getItem();
            AttributeValue cert_data = resultItem.get(THING_DIR);
            //删除设备
            cert_data.getM().remove(name);
            try{
                client.updateItem(new UpdateItemRequest().withTableName(DB_NAME)
                        .withKey(key).addAttributeUpdatesEntry(THING_DIR,
                                new AttributeValueUpdate()
                                        .withValue(cert_data)));
                return true;
            }catch (Exception e){
                view.deleteError();
                hidSnackBarDelay();
                return false;
            }
        }else {
            return false;
        }
    }
}
