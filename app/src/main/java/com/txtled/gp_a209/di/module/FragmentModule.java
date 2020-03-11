package com.txtled.gp_a209.di.module;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.txtled.gp_a209.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by KomoriWu
 *  on 2017/9/15.
 */
@Module
public class FragmentModule {
    private Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @Provides
    @FragmentScope
    Activity provideActivity() {
        return fragment.getActivity();
    }

}
