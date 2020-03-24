package com.txtled.gp_a209.add.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;

import androidx.appcompat.app.AlertDialog;
import androidx.core.location.LocationManagerCompat;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import com.amazonaws.services.iot.AWSIot;
import com.amazonaws.services.iot.AWSIotClient;
import com.amazonaws.services.iot.model.AttachPolicyRequest;
import com.amazonaws.services.iot.model.AttachThingPrincipalRequest;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateRequest;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateResult;
import com.amazonaws.services.iot.model.CreatePolicyRequest;
import com.amazonaws.services.iot.model.CreateThingRequest;
import com.amazonaws.services.iot.model.CreateThingResult;
import com.amazonaws.services.iot.model.DeleteThingRequest;
import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouch.util.ByteUtil;
import com.espressif.iot.esptouch.util.TouchNetUtil;
import com.txtled.gp_a209.R;
import com.txtled.gp_a209.add.udp.UDPBuild;
import com.txtled.gp_a209.add.listener.OnUdpSendRequest;
import com.txtled.gp_a209.application.MyApplication;
import com.txtled.gp_a209.base.CommonSubscriber;
import com.txtled.gp_a209.base.RxPresenter;
import com.txtled.gp_a209.bean.DeviceInfo;
import com.txtled.gp_a209.bean.WWADeviceInfo;
import com.txtled.gp_a209.broadcast.MyBroadcastReceiver;
import com.txtled.gp_a209.model.DataManagerModel;
import com.txtled.gp_a209.utils.AlertUtils;
import com.txtled.gp_a209.utils.Constants;
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

import static com.txtled.gp_a209.base.BaseActivity.TAG;
import static com.txtled.gp_a209.utils.Constants.CA;
import static com.txtled.gp_a209.utils.Constants.DB_NAME;
import static com.txtled.gp_a209.utils.Constants.DISCOVERY;
import static com.txtled.gp_a209.utils.Constants.FRIENDLY_NAME;
import static com.txtled.gp_a209.utils.Constants.REBOOT;
import static com.txtled.gp_a209.utils.Constants.REST_API;
import static com.txtled.gp_a209.utils.Constants.SEND_CA_ONE;
import static com.txtled.gp_a209.utils.Constants.SEND_CA_TWO;
import static com.txtled.gp_a209.utils.Constants.SEND_CERT_ONE;
import static com.txtled.gp_a209.utils.Constants.SEND_CERT_TWO;
import static com.txtled.gp_a209.utils.Constants.SEND_KEY_ONE;
import static com.txtled.gp_a209.utils.Constants.SEND_KEY_TWO;
import static com.txtled.gp_a209.utils.Constants.SEND_THING_NAME;
import static com.txtled.gp_a209.utils.Constants.THING_DIR;
import static com.txtled.gp_a209.utils.Constants.USER_ID;
import static com.txtled.gp_a209.utils.ForUse.ACCESS_KEY;
import static com.txtled.gp_a209.utils.ForUse.SECRET_ACCESS_KEY;

/**
 * Created by Mr.Quan on 2020/3/17.
 */
public class AddPresenter extends RxPresenter<AddContract.View> implements AddContract.Presenter {
    private DataManagerModel dataManagerModel;
    private Activity activity;
    private EsptouchAsyncTask4 mTask;
    private WifiManager myWifiManager;
    private UDPBuild udpBuild;
    private DhcpInfo dhcpInfo;
    private String broadCast = "";
    private WifiInfo wifiInfo;
    private String name;
    private CognitoCachingCredentialsProvider provider;
    private AmazonDynamoDB client;
    private String userId;
    private List<DeviceInfo> data;
    private AWSIot awsIot;
    private String[] names;
    private CreateThingResult iotThing;
    private CreateKeysAndCertificateResult keysAndCertificate;
    private WWADeviceInfo info;
    private AttributeValue cert_data;
    private HashMap<String, AttributeValue> key;
    private AlertDialog dialog;
    private String strReceive;
    private MyBroadcastReceiver mReceiver;
    @Inject
    public AddPresenter(DataManagerModel dataManagerModel) {
        this.dataManagerModel = dataManagerModel;
    }

    /**
     * 初始化
     * @param activity
     */
    @Override
    public void init(Activity activity) {
        this.activity = activity;
        registerBroadcast(activity);
        provider = MyApplication.getCredentialsProvider();
        client = new AmazonDynamoDBClient(provider);
        userId = dataManagerModel.getUserId();

        key = new HashMap<>();
        key.put(USER_ID, new AttributeValue().withS(userId));
        //初始化awsIot
        try {
            if (awsIot == null) {
                createIotService();
            }
        } catch (Exception e) {
            Utils.Logger(TAG, "IotServiceUtil.getAmazonIotService", e.getMessage());
        }
    }

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

    /**
     * 控件点击
     * @param id viewId
     * @param isCommit 根据true/false执行不同方法
     */
    @Override
    public void onClick(int id, boolean isCommit) {
        switch (id){
            case R.id.abt_collect:
            case R.id.atv_no_pass:
                if (isCommit){
                    connDevice();
                }else {
                    view.showConnectHint();
//                    broadCast = dataManagerModel.getDeviceAddress();
//                    udpSend(String.format(SEND_THING_NAME, REST_API,""), new OnUdpSendRequest() {
//                        @Override
//                        public void OnRequestListener(String result) {
//                            result.length();
//                            udpBuild.stopUDPSocket();
//                        }
//                    });
                }
                break;
        }
    }

    /**
     * 配网
     * @param s wifi名
     * @param pass wifi密码
     */
    @Override
    public void configWifi(byte[] s, String pass) {
        byte[] ssid = s;
        byte[] password = ByteUtil.getBytesByString(pass);
        byte[] bssid = TouchNetUtil.parseBssid2bytes(wifiInfo.getBSSID());
        byte[] deviceCount = {(byte)1};
        byte[] broadcast = {(byte)1};
        if (mTask != null) {
            mTask.cancelEsptouch();
            mTask = null;
        }
        mTask = new EsptouchAsyncTask4(wifiInfo.getSSID().substring(1,wifiInfo.getSSID().length()-1),pass);
        mTask.execute(ssid, bssid, password, deviceCount, broadcast);
    }

    /**
     * 创建事务
     */
    public void connDevice() {
        addSubscribe(Flowable.create((FlowableOnSubscribe<String>) e -> {
            createIotCore(name);
            e.onNext("success");
        },BackpressureStrategy.BUFFER)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<String>(view){
                    @Override
                    public void onNext(String s) {

                    }
                }));
    }

    /**
     * 创建iotCore并写入设备
     * @param friendlyName friendlyName
     */
    private void createIotCore(String friendlyName) {
        view.onStatueChange();
        try{
            if (names != null){

                if (info.getThing().isEmpty()){
                    //创建事物
                    createIotThing();

                    //HashMap<String, AttributeValue> cert = createData(keysAndCertificate,iotThing,friendlyName);
                    cert_data.addMEntry(friendlyName, new AttributeValue()
                            .withS(iotThing.getThingName()));

                    client.updateItem(new UpdateItemRequest().withTableName(DB_NAME)
                            .withKey(key).addAttributeUpdatesEntry(THING_DIR,
                                    new AttributeValueUpdate()
                                            .withValue(cert_data)));
                }else {
                    //改名字

                    Map<String, AttributeValue> thingNames = cert_data.getM();
                    for (int i = 0; i < names.length; i++) {
                        if (info.getThing()
                                .equals(thingNames.get(names[i]).getS())){
                            //listener.onStatueChange(R.string.changing);
                            thingNames.remove(names[i]);
                            thingNames.put(friendlyName,new AttributeValue()
                                    .withS(info.getThing()));
                            break;
                        }
                        if (i == names.length - 1){
                            thingNames.put(friendlyName, new AttributeValue()
                                    .withS(info.getThing()));
                        }
                    }

                    client.updateItem(new UpdateItemRequest().withTableName(DB_NAME)
                            .withKey(key).addAttributeUpdatesEntry(THING_DIR,
                                    new AttributeValueUpdate()
                                            .withValue(new AttributeValue().withM(thingNames))));

                    String[] friendlyNames = info
                            .getFriendlyNames().split(",");

                    String newNames = getNewName(friendlyNames,friendlyName);

                    String finalNewNames = newNames;
                    udpSend(String.format(FRIENDLY_NAME, newNames), result -> {
                        if (result.contains("1")){
                            view.changeName(friendlyName);
                            udpBuild.stopUDPSocket();
                            //listener.onStatueChange(R.string.complete_change);

                        }else {
                            udpBuild.sendMessage(String.format(FRIENDLY_NAME, finalNewNames),
                                    info.getIp());
                        }
                    });

                    info.setFriendlyNames(newNames);
                    //view.updateAdapter(position, info);
                    return;
                }
            }else {
                //创建事物
                if (info.getThing().isEmpty()){
                    createIotThing();
                    //创建数据
                    HashMap<String, AttributeValue> certs = new HashMap<>();
                    certs.put(friendlyName, new AttributeValue()
                            .withS(iotThing.getThingName()));//createData(keysAndCertificate,iotThing,friendlyName)

                    PutItemRequest request = new PutItemRequest();
                    request.withTableName(Constants.DB_NAME);
                    request.addItemEntry(USER_ID, new AttributeValue().withS(userId));
                    request.addItemEntry(THING_DIR, new AttributeValue().withM(certs));
                    client.putItem(request);
                }else {
                    //创建数据
                    HashMap<String, AttributeValue> certs = new HashMap<>();
                    certs.put(friendlyName, new AttributeValue()
                            .withS(info.getThing()));//createData(keysAndCertificate,iotThing,friendlyName)

                    PutItemRequest request = new PutItemRequest();
                    request.withTableName(Constants.DB_NAME);
                    request.addItemEntry(USER_ID, new AttributeValue().withS(userId));
                    request.addItemEntry(THING_DIR, new AttributeValue().withM(certs));
                    client.putItem(request);

                    String[] friendlyNames = info
                            .getFriendlyNames().split(",");

                    String newNames = getNewName(friendlyNames,friendlyName);

                    udpSend(String.format(FRIENDLY_NAME, newNames), result -> {
                        if (result.contains("1")){
                            udpBuild.stopUDPSocket();
                            //listener.onStatueChange(R.string.complete_change);
                            view.dismiss();
                        }else {
                            udpBuild.sendMessage(String.format(FRIENDLY_NAME, newNames),
                                    info.getIp());
                        }
                    });

                    info.setFriendlyNames(newNames);
                    //view.updateAdapter(position, refreshData);
                    return;
                }
            }
        }catch (Exception e){
            //创建表
            CreateTableRequest tableRequest = new CreateTableRequest()
                    .withTableName(DB_NAME)
                    .withKeySchema(new KeySchemaElement().withAttributeName(USER_ID)
                            .withKeyType(KeyType.HASH))
                    .withAttributeDefinitions(new AttributeDefinition()
                            .withAttributeName(USER_ID)
                            .withAttributeType(ScalarAttributeType.S))
                    .withProvisionedThroughput(
                            new ProvisionedThroughput(10L, 10L));
            tableRequest.setGeneralProgressListener(progressEvent -> {
                if (progressEvent.getEventCode() == 4) {
                    //创建事物
                    createIotThing();
                    //创建数据
                    HashMap<String, AttributeValue> certs = new HashMap<>();
                    certs.put(friendlyName, new AttributeValue()
                            .withS(iotThing.getThingName()));//createData(keysAndCertificate,iotThing,friendlyName)

                    PutItemRequest request = new PutItemRequest();
                    request.withTableName(Constants.DB_NAME);
                    request.addItemEntry(USER_ID, new AttributeValue().withS(userId));
                    request.addItemEntry(THING_DIR, new AttributeValue().withM(certs));
                    client.putItem(request);
                }
            });
            client.createTable(tableRequest);
        }

        //写入设备
        writeToDevice(friendlyName);
    }

    private void writeToDevice(String friendlyName) {
        //listener.onStatueChange(R.string.transmitting_data);
        String friendlyNames = getNewName(info.getFriendlyNames().split(","),friendlyName);
        broadCast = info.getIp();
        udpSend(String.format(SEND_THING_NAME, REST_API,
                iotThing.getThingName()), result -> {
            if (result.contains("\"ca0\"")){
                udpBuild.sendMessage(result.contains("1") ?
                        String.format(SEND_CA_TWO,CA.substring(CA.length()/2)) :
                        String.format(SEND_CA_ONE,CA.substring(0,CA.length()/2)),broadCast);

            }else if (result.contains("\"ca1\"")){
                udpBuild.sendMessage(result.contains("\"ca1\":1") ?
                        String.format(SEND_CERT_ONE,keysAndCertificate.getCertificatePem()
                                .substring(0,keysAndCertificate.getCertificatePem().length()/2)) :
                        String.format(SEND_CA_TWO,CA.substring(CA.length()/2)),broadCast);

            }else if (result.contains("\"cert0\"")){
                udpBuild.sendMessage(result.contains("1") ?
                        String.format(SEND_CERT_TWO,keysAndCertificate.getCertificatePem()
                                .substring(keysAndCertificate.getCertificatePem().length()/2)):
                        String.format(SEND_CERT_ONE,keysAndCertificate.getCertificatePem()
                                .substring(0,keysAndCertificate.getCertificatePem().length()/2)),broadCast);

            }else if (result.contains("\"cert1\"")){
                udpBuild.sendMessage(result.contains("\"cert1\":1") ?
                        String.format(SEND_KEY_ONE,keysAndCertificate.getKeyPair().getPrivateKey()
                                .substring(0,keysAndCertificate.getKeyPair().getPrivateKey().length()/2)) :
                        String.format(SEND_CERT_TWO,keysAndCertificate.getCertificatePem()
                                .substring(keysAndCertificate.getCertificatePem().length()/2)),broadCast);

            }else if (result.contains("\"key0\"")){
                udpBuild.sendMessage(result.contains("1") ?
                        String.format(SEND_KEY_TWO,keysAndCertificate.getKeyPair().getPrivateKey()
                                .substring(keysAndCertificate.getKeyPair().getPrivateKey().length()/2)):
                        String.format(SEND_KEY_ONE,keysAndCertificate.getKeyPair().getPrivateKey()
                                .substring(0,keysAndCertificate.getKeyPair()
                                        .getPrivateKey().length()/2)),broadCast);

            }else if (result.contains("\"key1\"")){
                udpBuild.sendMessage(result.contains("\"key1\":1") ?
                        String.format(FRIENDLY_NAME, friendlyNames) :
                        String.format(SEND_KEY_TWO,keysAndCertificate.getKeyPair().getPrivateKey()
                                .substring(keysAndCertificate.getKeyPair()
                                        .getPrivateKey().length()/2)),broadCast);
            }else if (result.contains("\"friendlyname\"")){
                udpBuild.sendMessage(result.contains("1") ? REBOOT :
                        String.format(FRIENDLY_NAME, friendlyNames),broadCast);
            }else if (result.contains("\"reboot\"")){
                if (result.contains("1")){
                    udpBuild.stopUDPSocket();
                    //listener.onStatueChange(R.string.transmit_completed);
                    view.dismiss();
                    info.setThing(iotThing.getThingName());
                    info.setFriendlyNames(friendlyNames);
                    //view.updateAdapter(position,refreshData);
                }else {
                    udpBuild.sendMessage(REBOOT,broadCast);
                }
            } else {
                udpBuild.sendMessage(result.contains("1") ?
                        String.format(SEND_CA_ONE,CA.substring(0,CA.length()/2)) :
                        String.format(SEND_THING_NAME, REST_API, iotThing.getThingName()),broadCast);
            }
        });
    }

    private String getNewName(String[] friendlyNames, String friendlyName){
        String newNames = "";
        boolean changed = false;

        for (int i = 0; i < friendlyNames.length; i++) {
            if (friendlyNames[i].contains(userId)){
                friendlyNames[i] = userId+"_"+friendlyName;
                changed = true;
            }
            newNames += friendlyNames[i] + (i == friendlyNames.length - 1 ? "" : ",");
        }
        if (!changed){
            newNames += (newNames.isEmpty() ? "" : ",") + userId + "_" + friendlyName;
        }
        return newNames;
    }

    private void udpSend(String message, OnUdpSendRequest listener){
        strReceive = "";
        udpBuild = UDPBuild.getUdpBuild();
        myWifiManager = ((WifiManager) activity.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE));
        dhcpInfo = myWifiManager.getDhcpInfo();
        udpBuild.setIgnoreIp(Utils.getWifiIp(dhcpInfo.ipAddress));
        udpBuild.setUdpReceiveCallback(data -> {
            strReceive = new String(data.getData(), 0, data.getLength());
            listener.OnRequestListener(strReceive);
        });
        setTime(message,listener);
        udpBuild.sendMessage(message,broadCast);
    }

    /**
     * 搜索超时判断
     */
    private void setTime(String message, OnUdpSendRequest listener) {
        addSubscribe(Flowable.timer(3,TimeUnit.SECONDS)
                .compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<Long>(view){

                    @Override
                    public void onNext(Long aLong) {
                        if (strReceive.equals("")){
                            udpSend(message,listener);
                            //udpBuild.sendMessage(message,broadCast);
                        }
                    }
                }));
    }

    private void createIotThing() {
        //listener.onStatueChange(R.string.hint_create_thing);
        //创建事物
        iotThing = awsIot.createThing(new CreateThingRequest()
                .withThingName(dataManagerModel.getUid()+"_"+System.currentTimeMillis()));
//        Utils.Logger(TAG, "CreateThingResult:", "\nthingArn:" + iotThing.getThingArn()
//                + "\nname:" + iotThing.getThingName() + "\nid:" + iotThing.getThingId());
        //创建证书
        keysAndCertificate = awsIot
                .createKeysAndCertificate(new CreateKeysAndCertificateRequest()
                        .withSetAsActive(true));
//        Utils.Logger(TAG, "KeysAndCertificateResult:",
//                "\narn:" + keysAndCertificate.getCertificateArn()
//                        + "\nCertificateId:" + keysAndCertificate.getCertificateId()
//                        + "\nCertificatePem:" + keysAndCertificate.getCertificatePem()
//                        + "\nPrivateKey:" + keysAndCertificate.getKeyPair().getPrivateKey()
//                        + "\nPublicKey:" + keysAndCertificate.getKeyPair().getPublicKey());
        //关联证书
        awsIot.attachThingPrincipal(new AttachThingPrincipalRequest()
                .withThingName(iotThing.getThingName())
                .withPrincipal(keysAndCertificate.getCertificateArn()));

//                    awsIot.attachPrincipalPolicy(new AttachPrincipalPolicyRequest()
//                            .withPolicyName(keysAndCertificate.getCertificateId())
//                            .withPrincipal(keysAndCertificate.getCertificateArn()));
        //创建策略，以下是默认策略文档
        try {
            CreatePolicyRequest policyRequest = new CreatePolicyRequest();
            policyRequest.setPolicyName(Constants.MY_OIT_CE);
            policyRequest.setPolicyDocument(Constants.POLICY_JSON);
            awsIot.createPolicy(policyRequest);
        } catch (Exception e1) {

        }

        //证书附加策略
        awsIot.attachPolicy(new AttachPolicyRequest()
                .withPolicyName(Constants.MY_OIT_CE)
                .withTarget(keysAndCertificate.getCertificateArn()));
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void getConfiguredData() {
        addSubscribe(Flowable.create((FlowableOnSubscribe<String[]>) e -> {
                    //查数据
                    e.onNext(getDeviceData());
                },
                BackpressureStrategy.BUFFER).compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<String[]>(view) {
                    @Override
                    public void onNext(String[] data) {
                        //返回数据
                        view.setData(data);

                    }
                }));
    }

    /**
     * 向设备获取信息
     */
    @Override
    public void getDeviceInfo() {
        udpSend(DISCOVERY, result -> {
            if (result.contains("\"discovery\":0")){
                udpBuild.sendMessage(DISCOVERY,broadCast);
            }else {
                try {
                    JSONObject deviceInfo = new JSONObject(result);
                    info = new WWADeviceInfo(
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onDestroy() {
        activity.unregisterReceiver(mReceiver);
    }

    @Override
    public void initData(WWADeviceInfo info) {
        this.info = info;
        broadCast = info.getIp();
    }

    private String[] getDeviceData() {
        data = new ArrayList<>();
        //获取数据
        GetItemResult itemResult = client.getItem(new GetItemRequest()
                .withTableName(DB_NAME).withKey(key));
        if (itemResult.getItem() != null) {
            Map<String, AttributeValue> resultItem = itemResult.getItem();
            cert_data = resultItem.get(THING_DIR);
            //设备名称
            names = cert_data.getM().keySet().toArray(new String[cert_data.getM().size()]);

            for (int i = 0; i < names.length; i++) {
                data.add(new DeviceInfo(names[i],cert_data.getM().get(names[i]).getS()));
            }
        }else {
            names = null;
        }
        return names;
    }

    public boolean isSDKAtLeastP() {
        return Build.VERSION.SDK_INT >= 28;
    }

//    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action == null) {
//                return;
//            }
//
//            WifiManager wifiManager = (WifiManager) context.getApplicationContext()
//                    .getSystemService(WIFI_SERVICE);
//            assert wifiManager != null;
//
//            switch (action) {
//                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
//                case LocationManager.PROVIDERS_CHANGED_ACTION:
//                    onWifiChanged(context,wifiManager.getConnectionInfo());
//                    break;
//            }
//        }
//    };

    private void onChanged(Context context, WifiInfo info) {
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
            wifiInfo = info;
            String ssid = info.getSSID();
            if (ssid.startsWith("\"") && ssid.endsWith("\"")) {
                ssid = ssid.substring(1, ssid.length() - 1);
            }
            view.setInfo(ssid);
        }
    }

    private void registerBroadcast(Activity activity) {
        IntentFilter filter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        if (isSDKAtLeastP()) {
            filter.addAction(LocationManager.PROVIDERS_CHANGED_ACTION);
        }

        mReceiver = new MyBroadcastReceiver((context, info) -> onChanged(context,info));
        activity.registerReceiver(mReceiver, filter);
    }

    public class EsptouchAsyncTask4 extends AsyncTask<byte[], IEsptouchResult, List<IEsptouchResult>> {

        private final Object mLock = new Object();
        private IEsptouchTask mEsptouchTask;
        private String name;
        private String pass;

        public EsptouchAsyncTask4(String name, String pass) {
            this.name = name;
            this.pass = pass;
        }

        public void cancelEsptouch() {
            cancel(true);
            if (mEsptouchTask != null) {
                mEsptouchTask.interrupt();
            }
        }

        @Override
        protected void onPreExecute() {
            dialog = AlertUtils.showProgressDialog(activity,name,pass);
        }

        @Override
        protected void onProgressUpdate(IEsptouchResult... values) {

            if (activity != null) {
                IEsptouchResult result = values[0];
                Utils.Logger(TAG, "EspTouchResult: ", String.valueOf(result));
//                String text = result.getBssid() + " is connected to the wifi";
//                Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected List<IEsptouchResult> doInBackground(byte[]... params) {
            int taskResultCount;
            synchronized (mLock) {
                byte[] apSsid = params[0];
                byte[] apBssid = params[1];
                byte[] apPassword = params[2];
                byte[] deviceCountData = params[3];
                byte[] broadcastData = params[4];
                taskResultCount = deviceCountData.length == 0 ? -1 : broadcastData[0];
                Context context = activity.getApplicationContext();
                mEsptouchTask = new EsptouchTask(apSsid, apBssid, apPassword, context);
                mEsptouchTask.setPackageBroadcast(broadcastData[0] == 1);
                mEsptouchTask.setEsptouchListener(this::publishProgress);
            }
            return mEsptouchTask.executeForResults(taskResultCount);
        }

        @Override
        protected void onPostExecute(List<IEsptouchResult> result) {
            if (result == null) {
                dialog.dismiss();
                dialog = null;
                AlertUtils.showAlertDialog(activity,R.string.configure_result_failed_port);
//                mResultDialog = new AlertDialog.Builder(activity)
//                        .setMessage(R.string.configure_result_failed_port)
//                        .setPositiveButton(android.R.string.ok, null)
//                        .show();
//                Window window = mResultDialog.getWindow();
//                window.setWindowAnimations(R.style.dialogWindowAnimInToOut);
//                window.setBackgroundDrawable(activity.getResources()
//                        .getDrawable(R.drawable.background_white));
//                mResultDialog.setCanceledOnTouchOutside(false);
                return;
            }

            // check whether the task is cancelled and no results received
            IEsptouchResult firstResult = result.get(0);
            if (firstResult.isCancelled()) {
                return;
            }
            // the task received some results including cancelled while
            // executing before receiving enough results

            if (!firstResult.isSuc()) {
                dialog.dismiss();
                dialog = null;
                AlertUtils.showAlertDialog(activity,R.string.configure_result_failed);
//                mResultDialog = new AlertDialog.Builder(activity)
//                        .setMessage()
//                        .setPositiveButton(android.R.string.ok, null)
//                        .show();
//                Window window = mResultDialog.getWindow();
//                window.setWindowAnimations(R.style.dialogWindowAnimInToOut);
//                window.setBackgroundDrawable(activity.getResources()
//                        .getDrawable(R.drawable.background_white));
//                mResultDialog.setCanceledOnTouchOutside(false);
                return;
            }

//            for (IEsptouchResult touchResult : result) {
//
//            }
            broadCast = result.get(0).getInetAddress().getHostAddress();
            dataManagerModel.setDeviceAddress(broadCast);

            //activity.insertWWAInfo(result);
            try {
                getDeviceInfo();
                Thread.sleep(5000);
                dialog.dismiss();
                dialog = null;
                AlertUtils.showAlertDialog(activity, R.string.configure_result_success,
                        (dialog, which) -> view.configureSuccess());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            cancelEsptouch();
            //CharSequence[] items = new CharSequence[resultMsgList.size()];

        }
    }
}
