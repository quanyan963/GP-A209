package com.txtled.gp_a209.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.google.android.material.textview.MaterialTextView;
import com.txtled.gp_a209.R;

import static com.txtled.gp_a209.utils.Constants.BOLD;
import static com.txtled.gp_a209.utils.Constants.THIN;

/**
 * Created by Mr.Quan on 2020/3/11.
 */
public class ArialRoundTextView extends MaterialTextView {
    public ArialRoundTextView(Context context) {
        super(context);
        init(context,null,0);
    }

    public ArialRoundTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs,0);
    }

    public ArialRoundTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (isInEditMode()) return;
        String fontName = "ArialRoundedMTStd.otf";
        if (attrs != null) {
            TypedArray typedArray = context.getTheme().
                    obtainStyledAttributes(attrs, R.styleable.ArialRound, defStyleAttr, 0);
            int fontType = typedArray.getInteger(R.styleable.ArialRound_text_type, 0);
            switch (fontType) {
                case THIN:
                    fontName = "ArialRoundedMTStd.otf";
                    break;
                case BOLD:
                    fontName = "ArialRoundedMTStd-ExtraBold.otf";
                    break;
            }
        }
        super.setTypeface(Typeface.createFromAsset(getContext().getAssets(),
                "fonts/" + fontName), defStyleAttr);
    }


}
