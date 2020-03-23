package com.txtled.gp_a209.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.txtled.gp_a209.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr.Quan on 2020/3/23.
 */
public class MyView extends RelativeLayout {
    @BindView(R.id.atv_left_text)
    ArialRoundTextView atvLeftText;
    @BindView(R.id.atv_right_text)
    ArialRoundTextView atvRightText;
    @BindView(R.id.img_point)
    TextView imgPoint;
    private Context mContext;
    private boolean isShow;
    private String leftText,rightText;

    public MyView(Context context) {
        this(context, null, 0);
    }

    public MyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MyViewStyle,
                defStyleAttr, 0);

        for (int i = 0; i < a.getIndexCount(); i++) {
            switch (a.getIndex(i)){
                case R.styleable.MyViewStyle_show_type:
                    isShow = a.getBoolean(a.getIndex(i),false);
                    break;
                case R.styleable.MyViewStyle_left_text:
                    leftText = a.getString(a.getIndex(i));
                    break;
                case R.styleable.MyViewStyle_right_text:
                    rightText = a.getString(a.getIndex(i));
                    break;
            }
        }
        a.recycle();
        init(context);
        showPoint(isShow);
        atvLeftText.setText(leftText);
        atvRightText.setText(rightText);
    }

    public void init(Context c) {
        this.mContext = c;
        LayoutInflater.from(mContext).inflate(R.layout.my_view, this, true);
        ButterKnife.bind(this);
    }

    public void setLeftText(int str){
        atvLeftText.setText(str);
    }

    public void setRightText(String str){
        atvRightText.setText(str);
    }

    public void showPoint(boolean isShow){
        this.isShow = isShow;
        if (isShow){
            imgPoint.setVisibility(VISIBLE);
        }else {
            imgPoint.setVisibility(INVISIBLE);
        }
    }
}
