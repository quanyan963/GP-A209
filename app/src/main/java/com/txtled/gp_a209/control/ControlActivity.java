package com.txtled.gp_a209.control;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.appcompat.app.AlertDialog;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.base.MvpBaseActivity;
import com.txtled.gp_a209.bean.IotCoreData;
import com.txtled.gp_a209.control.mvp.ControlContract;
import com.txtled.gp_a209.control.mvp.ControlPresenter;
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
    private String name;
    private String endpoint;
    private AlertDialog loading;
    private boolean power,result,offDuration;
    private int beforeProgress;

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
        sbVolume.setOnTouchListener((v, event) -> !power);
        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                beforeProgress = seekBar.getProgress();
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                    result = true;
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
                    result = true;
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
                    result = true;
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
                    result = true;
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
                    result = true;
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
                    result = true;
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
                } else {
                    result = true;
                    buttonView.setCompoundDrawables(null, getImgUnResources(2, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_never:
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(3, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    result = true;
                    buttonView.setCompoundDrawables(null, getImgUnResources(3, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_fifteen:
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(3, 2), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    result = true;
                    buttonView.setCompoundDrawables(null, getImgUnResources(3, 2), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_thirteen:
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(3, 3), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    result = true;
                    buttonView.setCompoundDrawables(null, getImgUnResources(3, 3), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_sixty:
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(3, 4), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    result = true;
                    buttonView.setCompoundDrawables(null, getImgUnResources(3, 4), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
        }
        if (!result)
            presenter.sendMqtt(buttonView.getId());
        result = false;
    }

    @Override
    public void onClick(View v) {
        presenter.onClick(v.getId(), power);
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
            powerChanged(iotCoreData.getDevice().equals("on"));
            sbVolume.setProgress(iotCoreData.getVolume());
            if (power) {
                resetView(iotCoreData);
            }
        });
    }

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
            presenter.enableView();
            sbVolume.setProgressDrawable(getResources().getDrawable(R.drawable.progress));
            sbVolume.setThumb(getResources().getDrawable(R.drawable.seekbar_selector));
            atvDuration.setTextColor(getResources().getColor(R.color.white));
        } else {
            abtPower.setTextColor(getResources().getColor(R.color.yellow));
            abtPower.setText(R.string.turn_on);
            abtPower.setBackgroundColor(getResources().getColor(R.color.black));
            result = true;
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
    public void volumeFail() {
        sbVolume.setProgress(beforeProgress);
    }

    @Override
    public void initFail() {
        hidLoadingView();

    }

    @Override
    public void resetView(IotCoreData iotCoreData) {
        offDuration = false;
        switch (iotCoreData.getSound()) {
            case 0:
                result = true;
                rbNone.setChecked(true);
                offDuration = true;
                break;
            case 1:
                result = true;
                rbLullaby.setChecked(true);
                break;
            case 2:
                result = true;
                rbSleeves.setChecked(true);
                break;
            case 3:
                result = true;
                rbCanon.setChecked(true);
                break;
            case 4:
                result = true;
                rbWaves.setChecked(true);
                break;
            case 5:
                result = true;
                rbRain.setChecked(true);
                break;
            case 6:
                result = true;
                rbNoise.setChecked(true);
                break;
        }
        if (!offDuration){
            switch (iotCoreData.getDuration()) {
                case 0:
                    result = true;
                    rbNever.setChecked(true);
                    break;
                case 15:
                    result = true;
                    rbFifteen.setChecked(true);
                    break;
                case 30:
                    result = true;
                    rbThirteen.setChecked(true);
                    break;
                case 60:
                    result = true;
                    rbSixty.setChecked(true);
                    break;
            }
        }else {
            rgDuration.clearCheck();
            for (int i = 0; i < rgDuration.getChildCount(); i++) {
                rgDuration.getChildAt(i).setEnabled(true);
            }
        }

    }

    @Override
    public void mqttFail(int id) {
        result = true;
        runOnUiThread(() -> ((ArialRoundRadioButton) findViewById(id)).setChecked(false));

    }
}
