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

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.base.MvpBaseActivity;
import com.txtled.gp_a209.control.mvp.ControlContract;
import com.txtled.gp_a209.control.mvp.ControlPresenter;
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
    private String name;
    private String endpoint;
    private int position;
    private boolean isClicked;
    private int line;
    private int oldLine;
    private int oldPosition;

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

        presenter.init(endpoint,this);
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
                drawable = getResources().getDrawable(R.mipmap.alarm_soundoff_xhdpi);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                return drawable;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.rb_lullaby:
                rgSoundTwo.clearCheck();
                rbNone.setChecked(false);
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(0, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    buttonView.setCompoundDrawables(null, getImgUnResources(0, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_sleeves:
                rgSoundTwo.clearCheck();
                rbNone.setChecked(false);
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(0, 2), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    buttonView.setCompoundDrawables(null, getImgUnResources(0, 2), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_canon:
                rgSoundTwo.clearCheck();
                rbNone.setChecked(false);
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(0, 3), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    buttonView.setCompoundDrawables(null, getImgUnResources(0, 3), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_waves:
                rgSoundOne.clearCheck();
                rbNone.setChecked(false);
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(1, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    buttonView.setCompoundDrawables(null, getImgUnResources(1, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_rain:
                rgSoundOne.clearCheck();
                rbNone.setChecked(false);
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(1, 2), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    buttonView.setCompoundDrawables(null, getImgUnResources(1, 2), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_noise:
                rgSoundOne.clearCheck();
                rbNone.setChecked(false);
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(1, 3), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    buttonView.setCompoundDrawables(null, getImgUnResources(1, 3), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_none:
                rgSoundTwo.clearCheck();
                rgSoundOne.clearCheck();
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(2, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    buttonView.setCompoundDrawables(null, getImgUnResources(2, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_never:
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(3, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    buttonView.setCompoundDrawables(null, getImgUnResources(3, 1), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_fifteen:
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(3, 2), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    buttonView.setCompoundDrawables(null, getImgUnResources(3, 2), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_thirteen:
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(3, 3), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    buttonView.setCompoundDrawables(null, getImgUnResources(3, 3), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
            case R.id.rb_sixty:
                if (isChecked) {
                    buttonView.setCompoundDrawables(null, getImgResources(3, 4), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.white));
                } else {
                    buttonView.setCompoundDrawables(null, getImgUnResources(3, 4), null, null);
                    buttonView.setTextColor(getResources().getColor(R.color.text_un));
                }
                break;
        }
        presenter.sendMqtt(buttonView.getId());
    }

    @Override
    public void onClick(View v) {

    }
}
