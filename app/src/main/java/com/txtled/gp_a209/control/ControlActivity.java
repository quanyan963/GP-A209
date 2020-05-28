package com.txtled.gp_a209.control;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.base.MvpBaseActivity;
import com.txtled.gp_a209.bean.IotCoreData;
import com.txtled.gp_a209.control.mvp.ControlContract;
import com.txtled.gp_a209.control.mvp.ControlPresenter;
import com.txtled.gp_a209.light.LightFragment;
import com.txtled.gp_a209.utils.AlertUtils;
import com.txtled.gp_a209.widget.ArialRoundButton;
import com.txtled.gp_a209.widget.ArialRoundRadioButton;
import com.txtled.gp_a209.widget.ArialRoundTextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.txtled.gp_a209.utils.Constants.ENDPOINT;
import static com.txtled.gp_a209.utils.Constants.NAME;

/**
 * Created by Mr.Quan on 2020/3/19.
 */
public class ControlActivity extends MvpBaseActivity<ControlPresenter> implements ControlContract.View,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    @BindView(R.id.atv_alarm)
    ArialRoundTextView atvAlarm;
    @BindView(R.id.atv_repeat)
    ArialRoundTextView atvRepeat;
    @BindView(R.id.rb_lullaby)
    ArialRoundRadioButton rbLullaby;
    @BindView(R.id.rb_sleeves)
    ArialRoundRadioButton rbSleeves;
    @BindView(R.id.rb_canon)
    ArialRoundRadioButton rbCanon;
    @BindView(R.id.rg_sound_one)
    RadioGroup rgSoundOne;
    @BindView(R.id.rb_waves)
    ArialRoundRadioButton rbWaves;
    @BindView(R.id.rb_rain)
    ArialRoundRadioButton rbRain;
    @BindView(R.id.rb_noise)
    ArialRoundRadioButton rbNoise;
    @BindView(R.id.rg_sound_two)
    RadioGroup rgSoundTwo;
    @BindView(R.id.rb_none)
    ArialRoundRadioButton rbNone;
    @BindView(R.id.img_volume_down)
    ImageButton imgVolumeDown;
    @BindView(R.id.sb_volume)
    SeekBar sbVolume;
    @BindView(R.id.img_volume_up)
    ImageButton imgVolumeUp;
    @BindView(R.id.rl_volume)
    RelativeLayout rlVolume;
    @BindView(R.id.rb_never)
    ArialRoundRadioButton rbNever;
    @BindView(R.id.rb_fifteen)
    ArialRoundRadioButton rbFifteen;
    @BindView(R.id.rb_thirteen)
    ArialRoundRadioButton rbThirteen;
    @BindView(R.id.rb_sixty)
    ArialRoundRadioButton rbSixty;
    @BindView(R.id.rg_duration)
    RadioGroup rgDuration;
    @BindView(R.id.abt_power)
    ArialRoundButton abtPower;
    @BindView(R.id.atv_duration)
    ArialRoundTextView atvDuration;
    @BindView(R.id.light_bottom_alarm)
    RadioButton rbalarm;
    @BindView(R.id.light_bottom_light)
    RadioButton rblight;

    private boolean selectLight;
    private String name;
    private String endpoint;
    private AlertDialog loading;
    private boolean power,result,offDuration;
    private LightFragment lightFrag;
    private int methodCount = 1;
    private FragmentManager fragmentManager;
    @Override
    public void setInject() {
        getActivityComponent().inject(this);
    }

    @Override
    public void init() {
        initToolbar();
        Intent intent = getIntent();
        name = intent.getStringExtra(NAME);
        endpoint = intent.getStringExtra(ENDPOINT);
        tvTitle.setText(name);
        setNavigationIcon(true);
        selectLight = false;

/*
        replaceFragment(lightFrag);
*/

        presenter.init(endpoint, this);
        rbLullaby.setOnCheckedChangeListener(this);
        rbSleeves.setOnCheckedChangeListener(this);
        rbCanon.setOnCheckedChangeListener(this);
        rbWaves.setOnCheckedChangeListener(this);
        rbRain.setOnCheckedChangeListener(this);
        rbNoise.setOnCheckedChangeListener(this);
        rbNone.setOnCheckedChangeListener(this);
        rbNever.setOnCheckedChangeListener(this);
        rbFifteen.setOnCheckedChangeListener(this);
        rbThirteen.setOnCheckedChangeListener(this);
        rbSixty.setOnCheckedChangeListener(this);
        abtPower.setOnClickListener(this);
        rbalarm.setOnClickListener(this);
        rblight.setOnClickListener(this);

        sbVolume.setOnTouchListener((v, event) -> !power);
        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                presenter.sendVolume(seekBar.getProgress());
            }
        });
        loading = AlertUtils.showLoadingDialog(this, R.layout.alert_progress);
        loading.show();

    }

    @Override
    public int getLayout() {
        return R.layout.activity_control;
    }

    @Override
    protected void beforeContentView() {

    }

    /**
     * 获取选中控件的drawable
     * @param line 行
     * @param position 列
     * @return Drawable
     */
    private Drawable getImgResources(int line, int position) {
        Drawable drawable;
        switch (line) {
            case 0:
                switch (position) {
                    case 1:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_brahm_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                    case 2:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_greens_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                    default:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_cano_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                }
            case 1:
                switch (position) {
                    case 1:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_wav_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                    case 2:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_rain_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                    default:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_nois_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                }
            case 3:
                switch (position) {
                    case 1:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_00_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                    case 2:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_15_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                    case 3:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_30_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                    default:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_60_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                }
            default:
                drawable = getResources().getDrawable(R.mipmap.alarm_soundoff_xhdpi);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                return drawable;
        }
    }
    /**
     * 获取需要变为未选中控件的drawable
     * @param line 行
     * @param position 列
     * @return Drawable
     */
    private Drawable getImgUnResources(int line, int position) {
        Drawable drawable;
        switch (line) {
            case 0:
                switch (position) {
                    case 1:
                        drawable = getResources().getDrawable(R.mipmap.afcafcalarm_brahmdes_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                    case 2:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_greensldes_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                    default:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_canondes_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                }
            case 1:
                switch (position) {
                    case 1:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_wavedes_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                    case 2:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_raindes_1_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                    default:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_noisedes_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                }
            case 3:
                switch (position) {
                    case 1:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_00des_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                    case 2:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_15des_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                    case 3:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_30des_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                    default:
                        drawable = getResources().getDrawable(R.mipmap.afcalarm_60des_xhdpi);
                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                        return drawable;
                }
            default:
                drawable = getResources().getDrawable(R.mipmap.alarm_soundxhdpi);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                return drawable;
        }
    }

    /**
     * 按钮互斥
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        System.out.println("触发onCheckedChanged函数");

        switch (buttonView.getId()) {
            case R.id.rb_lullaby:

                if (isChecked) {
                    rgSoundTwo.clearCheck();
                    rbNone.setChecked(false);
                    rbNone.setCompoundDrawables(null, getImgUnResources(2, 1), null, null);
                    rbNone.setTextColor(getResources().getColor(R.color.text_un));
                    buttonView.setCompoundDrawables(null, getImgResources(0, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    setResult(true);
                    buttonView.setCompoundDrawables(null, getImgUnResources(0, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_sleeves:

                if (isChecked) {
                    rgSoundTwo.clearCheck();
                    rbNone.setChecked(false);
                    rbNone.setCompoundDrawables(null, getImgUnResources(2, 1), null, null);
                    rbNone.setTextColor(getResources().getColor(R.color.text_un));
                    buttonView.setCompoundDrawables(null, getImgResources(0, 2), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    setResult(true);
                    buttonView.setCompoundDrawables(null, getImgUnResources(0, 2), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_canon:

                if (isChecked) {
                    rgSoundTwo.clearCheck();
                    rbNone.setChecked(false);
                    rbNone.setCompoundDrawables(null, getImgUnResources(2, 1), null, null);
                    rbNone.setTextColor(getResources().getColor(R.color.text_un));
                    buttonView.setCompoundDrawables(null, getImgResources(0, 3), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    setResult(true);
                    buttonView.setCompoundDrawables(null, getImgUnResources(0, 3), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_waves:

                if (isChecked) {
                    rgSoundOne.clearCheck();
                    rbNone.setChecked(false);
                    rbNone.setCompoundDrawables(null, getImgUnResources(2, 1), null, null);
                    rbNone.setTextColor(getResources().getColor(R.color.text_un));
                    buttonView.setCompoundDrawables(null, getImgResources(1, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    setResult(true);
                    buttonView.setCompoundDrawables(null, getImgUnResources(1, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_rain:

                if (isChecked) {
                    rgSoundOne.clearCheck();
                    rbNone.setChecked(false);
                    rbNone.setCompoundDrawables(null, getImgUnResources(2, 1), null, null);
                    rbNone.setTextColor(getResources().getColor(R.color.text_un));
                    buttonView.setCompoundDrawables(null, getImgResources(1, 2), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    setResult(true);
                    buttonView.setCompoundDrawables(null, getImgUnResources(1, 2), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_noise:

                if (isChecked) {
                    rgSoundOne.clearCheck();
                    rbNone.setChecked(false);
                    rbNone.setCompoundDrawables(null, getImgUnResources(2, 1), null, null);
                    rbNone.setTextColor(getResources().getColor(R.color.text_un));
                    buttonView.setCompoundDrawables(null, getImgResources(1, 3), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    setResult(true);
                    buttonView.setCompoundDrawables(null, getImgUnResources(1, 3), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_none:
                if (isChecked) {
                    rgSoundTwo.clearCheck();
                    rgSoundOne.clearCheck();
                    buttonView.setCompoundDrawables(null, getImgResources(2, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                    rgDuration.clearCheck();
                    for (int i = 0; i < rgDuration.getChildCount(); i++) {
                        rgDuration.getChildAt(i).setEnabled(false);
                    }
                } else {
                    buttonView.setCompoundDrawables(null, getImgUnResources(2, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));

                    for (int i = 0; i < rgDuration.getChildCount(); i++) {
                        rgDuration.getChildAt(i).setEnabled(true);
                    }
                    setResult(true);
                    rbNever.setChecked(true);
                }
                break;
            case R.id.rb_never:
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(3, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    setResult(true);
                    buttonView.setCompoundDrawables(null, getImgUnResources(3, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_fifteen:
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(3, 2), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    setResult(true);
                    buttonView.setCompoundDrawables(null, getImgUnResources(3, 2), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_thirteen:
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(3, 3), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    setResult(true);
                    buttonView.setCompoundDrawables(null, getImgUnResources(3, 3), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_sixty:
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(3, 4), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    setResult(true);
                    buttonView.setCompoundDrawables(null, getImgUnResources(3, 4), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
        }
        System.out.println("执行onCheckedChanged函数result==="+result);

        if (!result)presenter.sendMqtt(buttonView.getId());
        result = false;
        if (methodCount==1)methodCount+=1;
        else methodCount = 1;
        System.out.println("222执行onCheckedChanged函数result==="+result);

    }



    public int getLight(){

        return presenter.getLightState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.light_bottom_alarm:
                if (selectLight){
                    selectLight = false;
                    rbalarm.setBackgroundColor(Color.parseColor("#00CED1"));
                    rbalarm.setTextColor(Color.BLACK);
                    rblight.setBackgroundColor(Color.parseColor("#E6E6FA"));
                    rblight.setTextColor(Color.GRAY);
                    cancleLight();
                }

                break;
            case R.id.light_bottom_light:
                if (!selectLight) {
                    replaceFragment();
                    lightFrag.setCurrentid(getLight());
                    lightFrag.setLightCall(new LightFragment.LightStateCall() {
                        @Override
                        public void lightCallVotic(int state) {
                            presenter.sendLight(state);
                            System.out.println("设置灯光回调lightCallVotic====="+state);
                        }
                    });
                    selectLight = true;
                    rbalarm.setBackgroundColor(Color.parseColor("#E6E6FA"));
                    rbalarm.setTextColor(Color.GRAY);
                    rblight.setBackgroundColor(Color.parseColor("#00CED1"));
                    rblight.setTextColor(Color.BLACK);
                }

                break;
            default:
                presenter.onClick(v.getId(), power);
                break;
        }

    }


    public void cancleLight(){
/*
       transaction.hide(lightFrag);
*/
        FragmentTransaction transaction = fragmentManager.beginTransaction();
       transaction.hide(lightFrag);
       transaction.commit();
    }

    private void replaceFragment() {
        FragmentTransaction transaction;
        fragmentManager = getSupportFragmentManager();

        if (lightFrag==null){
            lightFrag = new LightFragment();
            transaction = fragmentManager.beginTransaction();   // 开启一个事务
            transaction.add(R.id.light_fragment, lightFrag);
        }
        else transaction = fragmentManager.beginTransaction();
        transaction.show(lightFrag);
        transaction.commit();
    }

    @Override
    public void mqttSuccess(int id) {

    }

    @Override
    public void hidLoadingView() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        presenter.destroy();
        super.onDestroy();
    }

    @Override
    public void setData(IotCoreData iotCoreData) {
        runOnUiThread(() -> {

            System.out.println("activity调用setData");
            hideSnackBar();
            abtPower.setEnabled(true);
            powerChanged(iotCoreData.getDevice().equals("on"));
            sbVolume.setProgress(iotCoreData.getVolume());
            if (power) {
                resetView(iotCoreData);
            }

            if (lightFrag!=null)lightFrag.setCurrentid(iotCoreData.getLight());
        });
    }

    @Override
    public void lightFail(){
        System.out.println("灯光发送失败");
        if (lightFrag!=null)lightFrag.setLightFail();
    }

    /**
     * 控制开关按钮
     * @param b true:开  false：关
     */
    @Override
    public void powerChanged(boolean b) {
        power = b;
        if (b) {
            abtPower.setTextColor(getResources().getColor(R.color.bg_snack));
            abtPower.setText(R.string.turn_off);
            abtPower.setBackgroundColor(getResources().getColor(R.color.gray));
            for (int i = 0; i < rgSoundOne.getChildCount(); i++) {
                rgSoundOne.getChildAt(i).setEnabled(true);
                rgSoundTwo.getChildAt(i).setEnabled(true);
            }
            for (int i = 0; i < rgDuration.getChildCount(); i++) {
                rgDuration.getChildAt(i).setEnabled(true);
            }
            rbNone.setEnabled(true);
/*
            presenter.enableView();
*/
            sbVolume.setProgressDrawable(getResources().getDrawable(R.drawable.progress));
            sbVolume.setThumb(getResources().getDrawable(R.drawable.seekbar_selector));
            atvDuration.setTextColor(getResources().getColor(R.color.white));
        } else {
            abtPower.setTextColor(getResources().getColor(R.color.yellow));
            abtPower.setText(R.string.turn_on);
            abtPower.setBackgroundColor(getResources().getColor(R.color.black));
            setResult(true);

            System.out.println("powerChanged重新设置result");
            rbNone.setChecked(false);
            rbNone.setEnabled(false);
            rgSoundOne.clearCheck();
            rgSoundTwo.clearCheck();
            rgDuration.clearCheck();
            for (int i = 0; i < rgSoundOne.getChildCount(); i++) {
                rgSoundOne.getChildAt(i).setEnabled(false);
                rgSoundTwo.getChildAt(i).setEnabled(false);
            }
            for (int i = 0; i < rgDuration.getChildCount(); i++) {
                rgDuration.getChildAt(i).setEnabled(false);
            }
            sbVolume.setProgressDrawable(getResources().getDrawable(R.drawable.progress_un));
            sbVolume.setThumb(getResources().getDrawable(R.drawable.seekbar_selector_un));
            atvDuration.setTextColor(getResources().getColor(R.color.text_un));
        }
    }

    @Override
    public void volumeFail(int progress) {
        Toast.makeText(this,R.string.change_fail,Toast.LENGTH_SHORT).show();
        sbVolume.setProgress(progress);
    }

    /**
     * 初始化失败
     */
    @Override
    public void initFail() {
        hidLoadingView();
        hideSnackBar();
        abtPower.setEnabled(false);
        rbNone.setEnabled(false);
        for (int i = 0; i < rgSoundOne.getChildCount(); i++) {
            rgSoundOne.getChildAt(i).setEnabled(false);
            rgSoundTwo.getChildAt(i).setEnabled(false);
        }
        for (int i = 0; i < rgDuration.getChildCount(); i++) {
            rgDuration.getChildAt(i).setEnabled(false);
        }
        showSnackBar(abtPower, R.string.sync_fail, R.string.retry, v -> {
            presenter.initData();
            hideSnackBar();
            loading.show();
        });

    }

    public void setResult(boolean result){
        if (result==true)System.out.println("重新设置为真");
        this.result = result;
    }

    /**
     * 重新设置控件状态
     * @param iotCoreData
     */
    @Override
    public void resetView(IotCoreData iotCoreData) {
        System.out.println("resetView重新设置");

        offDuration = false;
        switch (iotCoreData.getSound()) {
            case 0:
                setResult(true);

                rbNone.setChecked(true);
                offDuration = true;
                break;
            case 1:
                setResult(true);
                rbLullaby.setChecked(true);
                break;
            case 2:
                setResult(true);
                rbSleeves.setChecked(true);
                break;
            case 3:
                setResult(true);
                rbCanon.setChecked(true);
                break;
            case 4:
                setResult(true);
                rbWaves.setChecked(true);
                break;
            case 5:
                setResult(true);
                rbRain.setChecked(true);
                break;
            case 6:
                setResult(true);
                rbNoise.setChecked(true);
                break;
        }
        if (!offDuration){
            System.out.println("根据开关重新设置");
            switch (iotCoreData.getDuration()) {
                case 0:
                    setResult(true);
                    rbNever.setChecked(true);
                    break;
                case 15:
                    System.out.println("15分钟根据开关重新设置");
                    setResult(true);
                    rbFifteen.setChecked(true);
                    break;
                case 30:
                    setResult(true);
                    rbThirteen.setChecked(true);
                    break;
                case 60:
                    setResult(true);
                    rbSixty.setChecked(true);
                    break;
            }
        }else {
            rgDuration.clearCheck();
            for (int i = 0; i < rgDuration.getChildCount(); i++) {
                rgDuration.getChildAt(i).setEnabled(false);
            }
        }

    }

    @Override
    public void mqttFail(int id) {
        setResult(true);
        System.out.println("获取失败重置resultmqttFail\n");
        runOnUiThread(() -> {
            ((ArialRoundRadioButton) findViewById(id)).setChecked(false);
            Toast.makeText(this,R.string.change_fail,Toast.LENGTH_SHORT).show();
        });

    }
}
