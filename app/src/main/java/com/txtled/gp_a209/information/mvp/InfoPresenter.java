package com.txtled.gp_a209.information.mvp;

import android.app.Activity;
import android.view.View;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.txtled.gp_a209.R;
import com.txtled.gp_a209.application.MyApplication;
import com.txtled.gp_a209.base.RxPresenter;
import com.txtled.gp_a209.model.DataManagerModel;

import javax.inject.Inject;

/**
 * Created by Mr.Quan on 2020/3/23.
 */
public class InfoPresenter extends RxPresenter<InfoContract.View> implements InfoContract.Presenter {
    private DataManagerModel dataManagerModel;
    private CognitoCachingCredentialsProvider provider;
    private AmazonDynamoDB client;
    private String userId;

    @Inject
    public InfoPresenter(DataManagerModel dataManagerModel) {
        this.dataManagerModel = dataManagerModel;
    }

    @Override
    public void init(Activity activity) {
        provider = MyApplication.getCredentialsProvider();
        client = new AmazonDynamoDBClient(provider);

        userId = dataManagerModel.getUserId();
    }
}
