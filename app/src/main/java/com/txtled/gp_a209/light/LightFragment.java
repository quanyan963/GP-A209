package com.txtled.gp_a209.light;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.txtled.gp_a209.R;
import com.txtled.gp_a209.base.MvpBaseFragment;
import com.txtled.gp_a209.light.mvp.LightContract;
import com.txtled.gp_a209.light.mvp.LightPresenter;
import com.txtled.gp_a209.widget.ArialRoundButton;
import com.txtled.gp_a209.widget.ArialRoundRadioButton;

import butterknife.BindView;

/**
 * Created by Mr.Quan on 2020/5/25.
 */
public class LightFragment extends MvpBaseFragment<LightPresenter> implements LightContract.View {

    @BindView(R.id.button_light_red)
    ArialRoundRadioButton cbRed;
    @BindView(R.id.button_light_orange)
    ArialRoundRadioButton cbOrange;
    @BindView(R.id.button_light_yellow)
    ArialRoundRadioButton cbYellow;
    @BindView(R.id.button_light_green)
    ArialRoundRadioButton cbGreen;
    @BindView(R.id.button_light_cy_blue)
    ArialRoundRadioButton cbCyblue;
    @BindView(R.id.button_light_blue)
    ArialRoundRadioButton cbBlue;
    @BindView(R.id.button_light_purple)
    ArialRoundRadioButton cbPurple;
    @BindView(R.id.button_light_white)
    ArialRoundRadioButton cbWhite;
    @BindView(R.id.button_light_cycle)
    ArialRoundRadioButton cbCycle;
    @BindView(R.id.light_button_switch)
    ArialRoundButton cbSwitch;

    //activity设置灯光回调
    public interface LightStateCall{
        public void lightCallVotic(int state);
    }


    private LightStateCall lightCall;


    public void setLightCall(LightStateCall lightCall) {
        this.lightCall = lightCall;
    }

    private int currentid;
    private int lastid;
    private int lightState;
    private View view;
    //参数用来判断：界面初始化完成之前activity有设置灯光状态
    boolean setState;

    //设置灯光失败
    public void setLightFail(){
        setLightBtn(lastid,false);
    }

    //设置当前的被选中按钮，提供给activity使用的
    public void setCurrentid(int currentid){
        System.out.println("当前的灯光id"+currentid);
        if (currentid<0||currentid>9){
            return;
        }
        this.lightState = currentid;
        setState = true;
        if (view==null){
            return;
        }
        switch (currentid){
            case 0:
                setLightBtn(R.id.light_button_switch,false);
                break;
            case 1:
                setLightBtn(R.id.button_light_red,false);
                break;
            case 2:
                setLightBtn(R.id.button_light_orange,false);
                break;
            case 3:
                setLightBtn(R.id.button_light_yellow,false);
                break;
            case 4:
                setLightBtn(R.id.button_light_green,false);
                break;
            case 5:
                setLightBtn(R.id.button_light_cy_blue,false);
                break;
            case 6:
                setLightBtn(R.id.button_light_blue,false);
                break;
            case 7:
                setLightBtn(R.id.button_light_purple,false);
                break;
            case 8:
                setLightBtn(R.id.button_light_white,false);
                break;
            default:
                setLightBtn(R.id.button_light_cycle,false);
                break;
        }
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_light_fragment, container, false);
        return view;
    }

    //设置按钮的背景色和透明度
    public void setBtnColor(ArialRoundRadioButton btn, int color, int alpha) {
        GradientDrawable myGrad = (GradientDrawable) btn.getBackground();
        myGrad.setColor(color);
        myGrad.setAlpha(alpha);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
/*

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

    }
*/

    public void onClicks(View v) {
        setLightBtn(v.getId(),true);
    }

    //改变被选中的灯光按钮状态，先把原来的按钮状态清空，在设置选中的按钮
    public void setLightBtn(int btnid ,boolean isClick){
            System.out.println("调用setLightBtn"+btnid);
            if (currentid != btnid) {
                if (btnid!=R.id.light_button_switch){
                    lastid = currentid;
                    currentid = btnid;
                    System.out.println("currentid"+currentid);
                    if (lastid!=0){
                        changeBtnState(lastid, false);
                    }
                    changeBtnState(currentid, true);
                }
                else {
                    if (currentid!=0)changeBtnState(currentid, false);
                    currentid = btnid;
                    lastid = currentid;
                    lightState = 0;
                    cbSwitch.setText(R.string.turn_off);
                }
                if (lightCall!=null&&isClick){
                    btnIdToLightState(btnid);
                    lightCall.lightCallVotic(lightState);
                }
            }

    }

    //按钮的id转换成灯光的状态
    public void btnIdToLightState(int btnid){
            switch (btnid){
                case R.id.light_button_switch:
                    lightState=0;
                    break;
                case R.id.button_light_red:
                    lightState=1;
                    break;
                case R.id.button_light_orange:
                    lightState=2;
                    break;
                case R.id.button_light_yellow:
                    lightState=3;
                    break;
                case R.id.button_light_green:
                    lightState=4;
                    break;
                case R.id.button_light_cy_blue:
                    lightState=5;
                    break;
                case R.id.button_light_blue:
                    lightState=6;
                    break;
                case R.id.button_light_purple:
                    lightState=7;
                    break;
                case R.id.button_light_white:
                    lightState=8;
                    break;
                case R.id.button_light_cycle:
                    lightState=9;
                    break;

            }

    }

    //改变按钮的状态
    public void changeBtnState(int btnid, boolean select) {
        if (btnid==R.id.light_button_switch)return;
        System.out.println("调用changeBtnState"+btnid);
        int colors = Color.BLACK;
        switch (btnid){
            case R.id.button_light_red:
                System.out.println("红色red");
                colors = Color.RED;
                lightState = 1;
                break;
            case R.id.button_light_orange:
                System.out.println("橙色red2");
                lightState = 2;
                colors = Color.rgb(255, 165, 0);
                break;
            case R.id.button_light_yellow:
                System.out.println("黄色red3");
                lightState = 3;
                colors = Color.YELLOW;
                break;
            case R.id.button_light_green:
                colors = Color.GREEN;
                lightState = 4;
                System.out.println("红色red4");

                break;
            case R.id.button_light_cy_blue:
                colors = Color.rgb(75, 0, 130);
                lightState = 5;
                System.out.println("红色red5");

                break;
            case R.id.button_light_blue:
                colors = Color.BLUE;
                lightState = 6;
                System.out.println("红色red6");

                break;
            case R.id.button_light_purple:
                System.out.println("红色red7");
                lightState = 7;
                colors = Color.rgb(128, 0, 128);
                break;
            case R.id.button_light_white:
                System.out.println("红色red8");
                lightState = 8;
                colors = Color.WHITE;
                break;
            case R.id.button_light_cycle:
                System.out.println("红色red9");
                lightState = 9;
                colors = Color.GRAY;
                break;

        }

        int alpha = select == true ? 255 : 50;
        if (view==null)System.out.println("view是空的");
        ArialRoundRadioButton btn = (ArialRoundRadioButton)view.findViewById(btnid);

        GradientDrawable myGrad = (GradientDrawable) btn.getBackground();
/*
            myGrad.setColor(colors);
*/
        myGrad.setAlpha(alpha);

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void init() {

        setBtnColor(cbRed, Color.RED, 50);
        setBtnColor(cbOrange, Color.rgb(255, 165, 0), 50);
        setBtnColor(cbYellow, Color.YELLOW, 50);
        setBtnColor(cbGreen, Color.GREEN, 50);
        setBtnColor(cbCyblue, Color.rgb(75, 0, 130), 50);
        setBtnColor(cbBlue, Color.BLUE, 50);
        setBtnColor(cbPurple, Color.rgb(128, 0, 128), 50);
        setBtnColor(cbWhite, Color.WHITE, 50);
        setBtnColor(cbCycle,Color.GRAY,50);
        currentid  = R.id.button_light_red;
        lastid = R.id.button_light_red;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击了按钮1");
                onClicks(v);
            }
        };
        cbRed.setOnClickListener(listener);
        cbOrange.setOnClickListener(listener);
        cbYellow.setOnClickListener(listener);
        cbGreen.setOnClickListener(listener);
        cbCyblue.setOnClickListener(listener);
        cbBlue.setOnClickListener(listener);
        cbPurple.setOnClickListener(listener);
        cbWhite.setOnClickListener(listener);
        cbCycle.setOnClickListener(listener);
        cbSwitch.setOnClickListener(listener);
        if (setState==true){
            setCurrentid(lightState);
        }

/*
        cbRed.setOnClickListener(this);
*/


//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext()) {
//            @Override
//            public boolean canScrollVertically() {
//                // 直接禁止垂直滑动
//                return false;
//            }
//        };


    }


}

