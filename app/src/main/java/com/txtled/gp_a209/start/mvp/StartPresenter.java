package com.txtled.gp_a209.start.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.base.CommonSubscriber;
import com.txtled.gp_a209.base.RxPresenter;
import com.txtled.gp_a209.main.MainActivity;
import com.txtled.gp_a209.model.DataManagerModel;
import com.txtled.gp_a209.model.operate.OperateHelper;
import com.txtled.gp_a209.utils.AlertUtils;
import com.txtled.gp_a209.utils.Constants;
import com.txtled.gp_a209.utils.RxUtil;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Flowable;

/**
 * Created by Mr.Quan on 2020/3/11.
 */
public class StartPresenter extends RxPresenter<StartContract.View> implements StartContract.Presenter {

    private DataManagerModel mDataManagerModel;

    @Inject
    public StartPresenter(DataManagerModel mDataManagerModel) {
        this.mDataManagerModel = mDataManagerModel;
    }

    @Override
    public void startTimer(Activity activity) {
        addSubscribe(Flowable.timer(3, TimeUnit.SECONDS)
                .compose(RxUtil.<Long>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<Long>(view){

                    @Override
                    public void onNext(Long aLong) {
                        String[] permissions = {Constants.permissions[0], Constants.permissions[1]};
                        if (!checkPermission(activity,permissions)){
                            AlertUtils.showAlertDialog(activity, R.string.permissions_hint,
                                    (dialog, which) -> mDataManagerModel
                                            .requestPermissions(activity, permissions,
                                                    new OperateHelper.OnPermissionsListener() {
                                @Override
                                public void onSuccess(String name) {
                                    if (name.equals(Constants.permissions[1])) {
                                        view.toMain();
                                    }
                                }

                                @Override
                                public void onFailure() {
                                    view.showPermissionHint();
                                }

                                @Override
                                public void onAskAgain() {

                                }
                            }));
                        }else {
                            view.toMain();
                        }
                    }
                }));
    }

    public static boolean checkPermission(Context context, String[] permissions) {
        PackageManager packageManager = context.getPackageManager();
        String packageName = context.getPackageName();

        for (String permission : permissions) {
            int per = packageManager.checkPermission(permission, packageName);
            if (PackageManager.PERMISSION_DENIED == per) {
                return false;
            }
        }
        return true;
    }
}
