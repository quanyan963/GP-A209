package com.txtled.gp_a209.di.component;


import com.txtled.gp_a209.application.MyApplication;
import com.txtled.gp_a209.di.module.AppModule;
import com.txtled.gp_a209.model.DataManagerModel;
import com.txtled.gp_a209.model.db.DBHelper;
import com.txtled.gp_a209.model.net.NetHelper;
import com.txtled.gp_a209.model.operate.OperateHelper;
import com.txtled.gp_a209.model.prefs.PreferencesHelper;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by KomoriWu
 * on 2017-09-01.
 */

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    MyApplication getContext();

    DataManagerModel getDataManagerModel();

    DBHelper getDbHelper();

    PreferencesHelper getPreferencesHelper();

    NetHelper getNetHelper();

    OperateHelper getOperateHelper();
}
