package com.txtled.gp_a209.model.prefs;

/**
 * Created by Mr.Quan on 2018/4/17.
 */

public interface PreferencesHelper {

    boolean isFirstIn();

    void setFirstIn(boolean first);

    String getUserId();

    void setUserId(String id);
}
