package com.txtled.gp_a209.di.component;

import android.app.Activity;

import com.txtled.gp_a209.di.module.FragmentModule;
import com.txtled.gp_a209.di.scope.FragmentScope;

import dagger.Component;

/**
 * Created by KomoriWu
 * on 2017-09-01.
 */

@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {
    Activity getActivity();

    //void inject(IntroductionFragment introductionFragment);
}
