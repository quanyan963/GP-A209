package com.txtled.gp_a209.control.mqtt;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.txtled.gp_a209.control.mqtt.listener.OnSuccessListener;

public class MyTopic extends AWSIotTopic {
    private OnSuccessListener listener;

    public MyTopic(String topic, AWSIotQos qos) {
        super(topic, qos);
    }

    public void setListener(OnSuccessListener listener) {
        this.listener = listener;
    }

    @Override
    public void onSuccess() {
        listener.onSuccess();
        System.out.println("MyTopiconononSuccess");

        super.onSuccess();
    }

    @Override
    public void onMessage(AWSIotMessage message) {
        System.out.println("TopicMessagePayload"+message.getStringPayload());
        System.out.println("TopicMessageTopic"+message.getTopic());
        listener.onMessage(message);
        super.onMessage(message);
    }

    @Override
    public void onFailure() {
        listener.onFailure();
        System.out.println("MyTopiconFailure");

        super.onFailure();
    }

    @Override
    public void onTimeout() {
        listener.onTimeout();
        System.out.println("MyTopicononTimeout");

        super.onTimeout();
    }
}
