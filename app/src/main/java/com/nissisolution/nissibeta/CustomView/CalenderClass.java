package com.nissisolution.nissibeta.CustomView;

import android.graphics.drawable.Drawable;

public class CalenderClass {
    public long timestamp;
    public Drawable drawable;
    public int backgroundColor, textColor;

    public CalenderClass(long timestamp, Drawable drawable, int backgroundColor, int textColor) {
        this.timestamp = timestamp;
        this.drawable = drawable;
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }
}
