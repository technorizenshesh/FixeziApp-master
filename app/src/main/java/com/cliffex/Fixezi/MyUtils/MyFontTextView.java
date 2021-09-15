package com.cliffex.Fixezi.MyUtils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyFontTextView extends TextView {

    public MyFontTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public MyFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public MyFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("DroidSerif.ttf", context);
        setTypeface(customFont);
    }
}