package com.txtled.gp_a209.main.mvp;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.amazonaws.services.iot.model.CreateKeysAndCertificateResult;
import com.txtled.gp_a209.application.MyApplication;
import com.txtled.gp_a209.base.CommonSubscriber;
import com.txtled.gp_a209.base.RxPresenter;
import com.txtled.gp_a209.bean.DeviceInfo;
import com.txtled.gp_a209.model.DataManagerModel;
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

import static com.txtled.gp_a209.base.BaseFragment.TAG;
import static com.txtled.gp_a209.utils.Constants.DB_NAME;
import static com.txtled.gp_a209.utils.Constants.THING_DIR;
import static com.txtled.gp_a209.utils.Constants.USER_ID;

/**
 * Created by Mr.Quan on 2019/12/9.
 */
public class MainPresenter extends RxPresenter<MainContract.View> implements MainContract.Presenter {

    private DataManagerModel mDataManagerModel;
    private CognitoCachingCredentialsProvider provider;
    private AmazonDynamoDB client;
    private String userId;

    @Inject
    public MainPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public void init() {
        provider = MyApplication.getCredentialsProvider();
        client = new AmazonDynamoDBClient(provider);
        userId = mDataManagerModel.getUserId();
    }

    @Override
    public void onRefresh() {
        addSubscribe(Flowable.create((FlowableOnSubscribe<List<DeviceInfo>>) e -> {
                    //查数据
                    e.onNext(getDeviceData());

                },
                BackpressureStrategy.BUFFER).compose(RxUtil.rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<List<DeviceInfo>>(view) {
                    @Override
                    public void onNext(List<DeviceInfo> data) {
                        //返回数据
                        view.getDeviceData(data);

                    }
                }));

    }

    private List<DeviceInfo> getDeviceData() {
        List<DeviceInfo> data = new ArrayList<>();
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
                data.add(new DeviceInfo(names[i],cert_data.getM().get(names[i]).getS()));
            }
        }
        return data;
    }
}
