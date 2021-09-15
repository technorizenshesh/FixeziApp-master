package com.cliffex.Fixezi.MyUtils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyFontBoldTextView extends TextView {

    public MyFontBoldTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public MyFontBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public MyFontBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {

        Typeface customFont = FontCache.getTypeface("DroidSerif.ttf", context);
        Typeface bold = Typeface.create(customFont, Typeface.BOLD);
        setTypeface(bold);

    }
}