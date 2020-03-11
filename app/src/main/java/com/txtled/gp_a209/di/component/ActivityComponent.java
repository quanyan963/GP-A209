package com.txtled.gp_a209.di.component;

import android.app.Activity;


import com.txtled.gp_a209.di.module.ActivityModule;
import com.txtled.gp_a209.di.scope.ActivityScope;
import com.txtled.gp_a209.main.MainActivity;
import com.txtled.gp_a209.start.StartActivity;

import dagger.Component;

/**
 * Created by KomoriWu
 * on 2017-09-01.
 */

@ActivityScope
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    Activity getActivity();

    void inject(MainActivity mainActivity);

    void inject(StartActivity startActivity);
}
