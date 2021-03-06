package com.txtled.gp_a209.base;

import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;
import com.txtled.gp_a209.R;
import com.txtled.gp_a209.application.MyApplication;
import com.txtled.gp_a209.widget.ArialRoundTextView;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getSimpleName();
    public ArialRoundTextView tvTitle;
    public boolean isBack = true;
    public boolean changeColor = true;
    private long mExitTime;
    private MyApplication mApplication;
    Toolbar toolbar;
    public Snackbar snackbar;
    private ImageView ivRight;

    public abstract void init();

    public abstract int getLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        beforeContentView();
        setContentView(getLayout());
        ButterKnife.bind(this);
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this);
//        }

        mApplication = MyApplication.getInstance();
        addActivity();
        onCreateView();
        init();
    }

    protected abstract void beforeContentView();

    public void onCreateView() {

    }


    public void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            tvTitle = (ArialRoundTextView) findViewById(R.id.tv_title);
            ivRight = (ImageView) findViewById(R.id.iv_right);
            setSupportActionBar(toolbar);
            setTitle("");

            toolbar.setOnMenuItemClickListener(onMenuItemClick);
            toolbar.setNavigationOnClickListener(v -> {
                if (isBack){
                    onBackPressed();
                }else {
                    onLeftClick();
                }

            });
        }
    }

    public void onLeftClick() {
    }

    public void setNavigationIcon(boolean isBack) {
        this.isBack = isBack;
        if (isBack) {
            toolbar.setNavigationIcon(R.mipmap.logwifi_back_xhdpi);
        } else {
            toolbar.setNavigationIcon(R.mipmap.devicelist_left_xhdpi);
        }

    }

    public void setRightImg(boolean isShow, @Nullable int drawable, View.OnClickListener listener) {
        if (isShow) {
            ivRight.setVisibility(View.VISIBLE);
            ivRight.setImageResource(drawable);
            ivRight.setOnClickListener(listener);
        } else {
            ivRight.setVisibility(View.GONE);
        }

    }

    public Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            OnMenuItemClick(menuItem.getItemId());
            return true;
        }
    };

    public void OnMenuItemClick(int itemId) {

    }

    public boolean onExitActivity(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, R.string.exit_program_hint,
                        Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                removeAllActivity();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this);
//        }
    }

    public void addActivity() {
        mApplication.addActivity(this);
    }


    public void removeAllActivity() {
        mApplication.removeAllActivity();
    }

    public void showSnackBar(View view, int str) {
        if (snackbar == null) {
            snackbar = Snackbar.make(view, str, Snackbar.LENGTH_INDEFINITE);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.bg_snack));
        }
        snackbar.show();
    }

    public void showSnackBar(View view, int str, int btnStr, View.OnClickListener listener) {
        if (snackbar == null) {
            snackbar = Snackbar.make(view, str, Snackbar.LENGTH_INDEFINITE).setAction(btnStr,listener);
            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.bg_snack));
            snackbar.setActionTextColor(getResources().getColor(R.color.white));
        }
        snackbar.show();
    }

//    public void showSnackBar(View view, int layoutId, int str) {
//        if (snackbar == null) {
//            snackbar = Snackbar.make(view, str, Snackbar.LENGTH_INDEFINITE);
//            snackbar.getView().setBackgroundColor(getResources().getColor(R.color.bg_snack));
//            View snackbarview = snackbar.getView();//获取snackbar的View(其实就是SnackbarLayout)
//
//            Snackbar.SnackbarLayout snackbarLayout=(Snackbar.SnackbarLayout)snackbarview;//将获取的View转换成SnackbarLayout
//
//            View add_view = LayoutInflater.from(snackbarview.getContext()).inflate(layoutId,null);//加载布局文件新建View
//
//            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);//设置新建布局参数
//
//            p.gravity= Gravity.CENTER_VERTICAL;//设置新建布局在Snackbar内垂直居中显示
//
//            snackbarLayout.addView(add_view,1,p);//将新建布局添加进snackbarLayout相应位置
//        }
//        snackbar.show();
//    }

//    public void changeSnackBar(int str,int btnStr, boolean isShow, View.OnClickListener listener){
//        snackbar.setText(str);
//        View snackbarview = snackbar.getView();
//        ProgressBar bar = (ProgressBar) snackbarview.findViewById(R.id.clp_loading);
//        if (isShow){
//            bar.setVisibility(View.VISIBLE);
//            snackbar.setAction(null,null);
//        }else {
//            bar.setVisibility(View.GONE);
//            snackbar.setAction(btnStr,listener);
//        }
//
//    }

    public void hideSnackBar() {
        if (snackbar != null && snackbar.isShown()) {
            snackbar.dismiss();
            snackbar = null;
        }
    }

//    @Subscribe
//    public void onEventMainThread(String str) {
//
//    }
}
