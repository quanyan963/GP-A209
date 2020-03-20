package com.txtled.gp_a209.control.mqtt;

import com.amazonaws.services.iot.client.AWSIotDevice;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTimeoutException;
import com.amazonaws.services.iot.client.core.AbstractAwsIotClient;
import com.amazonaws.services.iot.client.core.AwsIotCompletion;
import com.amazonaws.services.iot.client.core.AwsIotConnection;
import com.amazonaws.services.iot.client.core.AwsIotConnectionCallback;
import com.amazonaws.services.iot.client.core.AwsIotMessageCallback;
import com.amazonaws.services.iot.client.core.AwsIotRetryableException;
import com.txtled.gp_a209.control.mqtt.listener.OnConnectListener;
import com.txtled.gp_a209.control.mqtt.listener.OnMessageListener;
import com.txtled.gp_a209.control.mqtt.listener.OnSuccessListener;
import com.txtled.gp_a209.utils.ForUse;

import org.json.JSONObject;

import static com.txtled.gp_a209.utils.Constants.REST_API;
import static com.txtled.gp_a209.utils.Constants.SUBSCRIBE;

public class MqttClient {
    private static MqttClient mqttClient;
    private AWSIotDevice device;
    private AWSIotMqttClient client;
    private MyTopic myTopic;
    private AwsIotConnection connection;

    public static MqttClient getClient() {
        if (mqttClient == null){
            mqttClient = new MqttClient();
        }
        return mqttClient;
    }

    public void initClient(String thingName, OnConnectListener listener) {
        String clientId = String.valueOf(System.currentTimeMillis());// replace with your own client ID. Use unique client IDs for concurrent connections.

// AWS IAM credentials could be retrieved from AWS Cognito, STS, or other secure sources
        client = new AWSIotMqttClient(REST_API, clientId, ForUse.ACCESS_KEY, ForUse.SECRET_ACCESS_KEY){

            @Override
            public void onConnectionSuccess() {
                listener.onSuccess(device);
                super.onConnectionSuccess();
            }
        };

        device = new AWSIotDevice(thingName);
        try {
            client.attach(device);

            client.connect(5000,false);
//            if (client.getConnectionStatus().equals(AWSIotConnectionStatus.DISCONNECTED)){
//                System.out.println("ConnectStatue:"+client.getConnectionStatus());
//                client.connect();
//            }
        } catch (AWSIotException e) {
            listener.onFail();
        } catch (AWSIotTimeoutException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String endpoint, OnSuccessListener listener){

        try {
            myTopic = new MyTopic(String.format(SUBSCRIBE,endpoint));
            myTopic.setListener(listener);
            client.subscribe(myTopic);
        } catch (AWSIotException e) {

        }
    }

    public void closeClient(){
        try {
            client.detach(device);
            client.unsubscribe(myTopic);
            client.disconnect(3000,false);
        } catch (AWSIotException e) {

        } catch (AWSIotTimeoutException e) {

        }

    }
}
