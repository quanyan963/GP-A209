package com.txtled.gp_a209.add.mvp;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;
import androidx.core.location.LocationManagerCompat;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.espressif.iot.esptouch.EsptouchTask;
import com.espressif.iot.esptouch.IEsptouchResult;
import com.espressif.iot.esptouch.IEsptouchTask;
import com.espressif.iot.esptouch.util.ByteUtil;
import com.espressif.iot.esptouch.util.TouchNetUtil;
import com.txtled.gp_a209.R;
import com.txtled.gp_a209.application.MyApplication;
import com.txtled.gp_a209.base.CommonSubscriber;
import com.txtled.gp_a209.base.RxPresenter;
import com.txtled.gp_a209.bean.DeviceInfo;
import com.txtled.gp_a209.model.DataManagerModel;
import com.txtled.gp_a209.utils.AlertUtils;
import com.txtled.gp_a209.utils.RxUtil;
import com.txtled.gp_a209.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;

import static android.content.Context.WIFI_SERVICE;
import static com.txtled.gp_a209.base.BaseActivity.TAG;
import static com.txtled.gp_a209.utils.Constants.DB_NAME;
import static com.txtled.gp_a209.utils.Constants.THING_DIR;
import static com.txtled.gp_a209.utils.Constants.USER_ID;

/**
 * Created by Mr.Quan on 2020/3/17.
 */
public class AddPresenter extends RxPresenter<AddContract.View> implements AddContract.Presenter {
    private DataManagerModel dataManagerModel;
    private Activity activity;
    private EsptouchAsyncTask4 mTask;
    private WifiManager myWifiManager;
    private DhcpInfo dhcpInfo;
    private String broadCast = "";
    private WifiInfo wifiInfo;
    private String name;
    private CognitoCachingCredentialsProvider provider;
    private AmazonDynamoDB client;
    private String userId;

    @Inject
    public AddPresenter(DataManagerModel dataManagerModel) {
        this.dataManagerModel = dataManagerModel;
    }

    @Override
    public void init(Activity activity) {
        this.activity = activity;
        registerBroadcast(activity);
        provider = MyApplication.getCredentialsProvider();
        client = new AmazonDynamoDBClient(provider);
        userId = dataManagerModel.getUserId();
    }

    @Override
    public void onClick(int id) {
        switch (id){
            case R.id.abt_collect:
            case R.id.atv_no_pass:
                view.showConnectHint();
                break;
        }
    }

    @Override
    public void configWifi(byte[] s, String pass) {
        byte[] ssid = s;
        byte[] password = ByteUtil.getBytesByString(pass);
        byte[] bssid = TouchNetUtil.parseBssid2bytes(wifiInfo.getBSSID());
        byte[] deviceCount = {(byte)1};
        byte[] broadcast = {(byte)1};
        if (mTask != null) {
            mTask.cancelEsptouch();
        }
        mTask = new EsptouchAsyncTask4(wifiInfo.getSSID().substring(1,wifiInfo.getSSID().length()-1),pass);
        mTask.execute(ssid, bssid, password, deviceCount, broadcast);
    }

    @Override
    public void connDevice(String address) {

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

    private String[] getDeviceData() {
        String[] names;
        HashMap<String, AttributeValue> key = new HashMap<>();
        key.put(USER_ID, new AttributeValue().withS(userId));
        //获取数据
        GetItemResult itemResult = client.getItem(new GetItemRequest()
                .withTableName(DB_NAME).withKey(key));
        if (itemResult.getItem() != null) {
            Map<String, AttributeValue> resultItem = itemResult.getItem();
            AttributeValue cert_data = resultItem.get(THING_DIR);
            //设备名称
            names = cert_data.getM().keySet().toArray(new String[cert_data.getM().size()]);
        }else {
            names = null;
        }
        return names;
    }

    public boolean isSDKAtLeastP() {
        return Build.VERSION.SDK_INT >= 28;
    }

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

    private void onWifiChanged(Context context, WifiInfo info) {
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
        activity.registerReceiver(mReceiver, filter);
    }

    public class EsptouchAsyncTask4 extends AsyncTask<byte[], IEsptouchResult, List<IEsptouchResult>> {

        private final Object mLock = new Object();
        private IEsptouchTask mEsptouchTask;
        private String name;
        private String pass;
        private AlertDialog dialog;
        private AlertDialog mResultDialog;

        public EsptouchAsyncTask4(String name, String pass) {
            this.name = name;
            this.pass = pass;
        }

        public void cancelEsptouch() {
            cancel(true);
            if (dialog !=null){
                dialog.dismiss();
                dialog = null;
            }
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
            dialog.dismiss();
            dialog = null;
            if (result == null) {

                mResultDialog = new AlertDialog.Builder(activity)
                        .setMessage(R.string.configure_result_failed_port)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                Window window = mResultDialog.getWindow();
                window.setWindowAnimations(R.style.dialogWindowAnimInToOut);
                window.setBackgroundDrawable(activity.getResources()
                        .getDrawable(R.drawable.background_white));
                mResultDialog.setCanceledOnTouchOutside(false);
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
                mResultDialog = new AlertDialog.Builder(activity)
                        .setMessage(R.string.configure_result_failed)
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
                Window window = mResultDialog.getWindow();
                window.setWindowAnimations(R.style.dialogWindowAnimInToOut);
                window.setBackgroundDrawable(activity.getResources()
                        .getDrawable(R.drawable.background_white));
                mResultDialog.setCanceledOnTouchOutside(false);
                return;
            }

            String address = "";
            for (IEsptouchResult touchResult : result) {
                address = touchResult.getInetAddress().getHostAddress();
                dataManagerModel.setDeviceAddress(address);
            }

            //activity.insertWWAInfo(result);

            //CharSequence[] items = new CharSequence[resultMsgList.size()];
            String finalAddress = address;
            mResultDialog = new AlertDialog.Builder(activity)
                    .setMessage(R.string.configure_result_success)
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        view.configureSuccess(finalAddress);
                        cancelEsptouch();
                    })
                    .show();
            Window window = mResultDialog.getWindow();
            window.setWindowAnimations(R.style.dialogWindowAnimInToOut);
            window.setBackgroundDrawable(activity.getResources()
                    .getDrawable(R.drawable.background_white));
            mResultDialog.setCanceledOnTouchOutside(false);
        }
    }
}
