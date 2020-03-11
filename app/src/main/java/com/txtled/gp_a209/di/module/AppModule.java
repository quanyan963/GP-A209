package com.txtled.gp_a209.di.module;

import com.txtled.gp_a209.application.MyApplication;
import com.txtled.gp_a209.model.DataManagerModel;
import com.txtled.gp_a209.model.db.DBHelper;
import com.txtled.gp_a209.model.db.DBHelperImpl;
import com.txtled.gp_a209.model.net.NetHelper;
import com.txtled.gp_a209.model.net.NetHelperImpl;
import com.txtled.gp_a209.model.operate.OperateHelper;
import com.txtled.gp_a209.model.operate.OperateHelperImpl;
import com.txtled.gp_a209.model.prefs.PreferencesHelper;
import com.txtled.gp_a209.model.prefs.PreferencesHelperImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by KomoriWu
 * on 2017/9/15.
 */
@Module
public class AppModule {
    private MyApplication myApplication;

    public AppModule(MyApplication myApplication) {
        this.myApplication = myApplication;
    }

    @Provides
    @Singleton
    MyApplication provideMyApplication() {
        return myApplication;
    }

    @Provides
    @Singleton
    DBHelper provideDBHelper(DBHelperImpl dbHelper) {
        return dbHelper;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(PreferencesHelperImpl preferencesHelper) {
        return preferencesHelper;
    }

    @Provides
    @Singleton
    NetHelper provideNetHelper(NetHelperImpl netHelper) {
        return netHelper;
    }

    @Provides
    @Singleton
    OperateHelper provideOperateHelper(OperateHelperImpl operateHelper) {
        return operateHelper;
    }

    @Provides
    @Singleton
    DataManagerModel provideDataManagerModel(DBHelperImpl dbHelper,
                                             PreferencesHelperImpl preferencesHelper,
                                             NetHelperImpl netHelper, OperateHelperImpl operateHelper) {
        return new DataManagerModel(dbHelper, preferencesHelper,netHelper,operateHelper);
    }
}
