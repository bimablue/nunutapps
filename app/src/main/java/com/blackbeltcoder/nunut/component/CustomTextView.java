package com.blackbeltcoder.nunut.component;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.blackbeltcoder.nunut.R;

public class CustomTextView extends TextView {
	
	public CustomTextView(Context context) {
		super(context);
		init(0);
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomTextView,
                0 ,0);
		int styleFont = attributes.getInt(R.styleable.CustomTextView_textStyle, 0);
		
		init(styleFont);
	}

	public CustomTextView(Context context, AttributeSet attrs,
						  int defStyle) {
		super(context, attrs, defStyle);
		
		final TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs,R.styleable.CustomTextView,
                0 ,0);
		int styleFont = attributes.getInt(R.styleable.CustomTextView_textStyle, 0);
		
		init(styleFont);
	}

	public void init(int styleFont) {
		Typeface tf;
		
		if (styleFont == 1){
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");
        }else if(styleFont == 2){
			tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Light.ttf");
		}else if(styleFont == 3){
			tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-LightItalic.ttf");
		}else{
            tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        }
		
		setTypeface(tf);
	}
}
