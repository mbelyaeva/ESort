package com.example.brett.esort;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Maria on 11/17/2015.
 */
public class customFont extends TextView{
        public customFont(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/SEGOEUIL"));
        }

}
