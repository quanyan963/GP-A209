package com.txtled.gp_a209.control.mvp;

import android.app.Activity;

import com.amazonaws.services.iot.client.AWSIotDevice;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import com.txtled.gp_a209.R;
import com.txtled.gp_a209.base.RxPresenter;
import com.txtled.gp_a209.bean.IotCoreData;
import com.txtled.gp_a209.control.mqtt.MqttClient;
import com.txtled.gp_a209.control.mqtt.MyShadowMessage;
import com.txtled.gp_a209.control.mqtt.listener.OnConnectListener;
import com.txtled.gp_a209.control.mqtt.listener.OnMessageListener;
import com.txtled.gp_a209.control.mqtt.listener.OnSuccessListener;
import com.txtled.gp_a209.model.DataManagerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import static com.txtled.gp_a209.utils.Constants.DATA_DEVICE;
import static com.txtled.gp_a209.utils.Constants.DATA_DURATION;
import static com.txtled.gp_a209.utils.Constants.DATA_LIGHT;
import static com.txtled.gp_a209.utils.Constants.DATA_SOUND;
import static com.txtled.gp_a209.utils.Constants.DATA_VOLUME;
import static com.txtled.gp_a209.utils.Constants.GET_DATA;
import static com.txtled.gp_a209.utils.Constants.PUBLISH;

/**
 * Created by Mr.Quan on 2020/3/19.
 */
public class ControlPresenter extends RxPresenter<ControlContract.View> implements ControlContract.Presenter {
    private DataManagerModel dataManagerModel;
    private String topic;
    private String message;
    private String endpoint;
    private Activity activity;
    private AWSIotDevice iotDevice;
    private MyShadowMessage myMessage;
    private IotCoreData iotCoreData;

    @Inject
    public ControlPresenter(DataManagerModel dataManagerModel) {
        this.dataManagerModel = dataManagerModel;
    }

    @Override
    public void init(String endpoint, Activity activity) {
        this.endpoint = endpoint;
        this.activity = activity;

        MqttClient.getClient().initClient(endpoint, new OnConnectListener() {
            @Override
            public void onSuccess(AWSIotDevice device) {
                iotDevice = device;
                initData();
            }

            @Override
            public void onFail() {

            }
        });
        MqttClient.getClient().subscribe(endpoint, new OnSuccessListener() {
            @Override
            public void onSuccess() {
                //view.hidLoadingView();
            }

            @Override
            public void onFailure() {
            }

            @Override
            public void onTimeout() {
            }

            @Override
            public void onMessage() {

            }
        });
    }

    @Override
    public void sendMqtt(int id) {
        try {
            MyShadowMessage myMessage = new MyShadowMessage(String.format(PUBLISH,endpoint), AWSIotQos.QOS0,getData(id));
            myMessage.setListener(new OnMessageListener() {
                @Override
                public void onSuccess() {
                    setData(id);
                }

                @Override
                public void onFailure() {
                    view.mqttFail(id);
                }

                @Override
                public void onTimeout() {
                    view.mqttFail(id);
                }
            });
            iotDevice.update(myMessage,5000);
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
    }

    private void setData(int id) {

        switch (id){
            case R.id.rb_lullaby:
                iotCoreData.setSound(1);
                break;
            case R.id.rb_sleeves:
                iotCoreData.setSound(2);
                break;
            case R.id.rb_canon:
                iotCoreData.setSound(3);
                break;
            case R.id.rb_waves:
                iotCoreData.setSound(4);
                break;
            case R.id.rb_rain:
                iotCoreData.setSound(5);
                break;
            case R.id.rb_noise:
                iotCoreData.setSound(6);
                break;
            case R.id.rb_none:
                iotCoreData.setSound(0);
                break;
            case R.id.rb_never:
                iotCoreData.setDuration(0);
                break;
            case R.id.rb_fifteen:
                iotCoreData.setDuration(15);
                break;
            case R.id.rb_thirteen:
                iotCoreData.setDuration(30);
                break;
            case R.id.rb_sixty:
                iotCoreData.setDuration(60);
                break;
        }
    }

    private String getData(int id) {
        String data = "";
        switch (id){
            case R.id.rb_lullaby:
                data = String.format(DATA_SOUND,1);
                break;
            case R.id.rb_sleeves:
                data = String.format(DATA_SOUND,2);
                break;
            case R.id.rb_canon:
                data = String.format(DATA_SOUND,3);
                break;
            case R.id.rb_waves:
                data = String.format(DATA_SOUND,4);
                break;
            case R.id.rb_rain:
                data = String.format(DATA_SOUND,5);
                break;
            case R.id.rb_noise:
                data = String.format(DATA_SOUND,6);
                break;
            case R.id.rb_none:
                data = String.format(DATA_SOUND,0);
                break;
            case R.id.rb_never:
                data = String.format(DATA_DURATION,0);
                break;
            case R.id.rb_fifteen:
                data = String.format(DATA_DURATION,15);
                break;
            case R.id.rb_thirteen:
                data = String.format(DATA_DURATION,30);
                break;
            case R.id.rb_sixty:
                data = String.format(DATA_DURATION,60);
                break;
        }
        return data;
    }

    @Override
    public void onClick(int id,boolean power) {
        switch (id){
            case R.id.abt_power:
                view.powerChanged(!power);
                sendPower(!power);
                break;
        }
    }

    private void sendPower(boolean power) {
        try {
            MyShadowMessage myMessage = new MyShadowMessage(String.format(PUBLISH,endpoint),
                    AWSIotQos.QOS0,String.format(DATA_DEVICE,power == true ? "\"on\"" : "\"off\""));
            myMessage.setListener(new OnMessageListener() {
                @Override
                public void onSuccess() {
                    iotCoreData.setDevice(power == true ? "on" : "off");
                }

                @Override
                public void onFailure() {
                    view.powerChanged(!power);
                }

                @Override
                public void onTimeout() {
                    view.powerChanged(!power);
                }
            });
            iotDevice.update(myMessage,5000);
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initData() {
        try {
            myMessage = new MyShadowMessage(String.format(GET_DATA,endpoint),AWSIotQos.QOS0);
            myMessage.setListener(new OnMessageListener() {
                @Override
                public void onSuccess() {
                    myMessage.getStringPayload();
                    try {
                        JSONObject data = new JSONObject(myMessage.getStringPayload());
                        JSONObject state = data.getJSONObject("state").getJSONObject("reported");
                        iotCoreData = new IotCoreData(
                                state.optInt("light",0),
                                state.optInt("sound",1),
                                state.optInt("duration",0),
                                state.optInt("volume",2),
                                state.optString("device","on"));
                        view.setData(iotCoreData);
                        view.hidLoadingView();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    myMessage = null;
                }

                @Override
                public void onFailure() {
                    view.initFail();
                }

                @Override
                public void onTimeout() {
                    view.initFail();
                }
            });
            iotDevice.get(myMessage,5000);
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendVolume(int progress) {
        MyShadowMessage myMessage = new MyShadowMessage(String.format(PUBLISH,endpoint),
                AWSIotQos.QOS0,String.format(DATA_VOLUME,progress));
        myMessage.setListener(new OnMessageListener() {
            @Override
            public void onSuccess() {
                iotCoreData.setVolume(progress);
            }

            @Override
            public void onFailure() {
                view.volumeFail(iotCoreData.getVolume());
            }

            @Override
            public void onTimeout() {
                view.volumeFail(iotCoreData.getVolume());
            }
        });
        try {
            iotDevice.update(myMessage,5000);
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        MqttClient.getClient().closeClient();
    }

    @Override
    public void enableView() {
        view.resetView(iotCoreData);
    }
}
