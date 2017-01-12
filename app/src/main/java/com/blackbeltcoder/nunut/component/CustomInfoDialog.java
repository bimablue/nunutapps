package com.blackbeltcoder.nunut.component;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.blackbeltcoder.nunut.R;
import com.blackbeltcoder.nunut.global.ConstantVariable;

/**
 * Created by ainozenbook on 10/9/2016.
 */

public class CustomInfoDialog extends Dialog {
    private String title, content;

    private Button btnClose, btnOk;
    private CustomTextView tvTitle, tvDescription;
    private ImageView ivInfoLogo;
    private View.OnClickListener onClickOkListener;

    public CustomInfoDialog(Context c, String title, String content, View.OnClickListener onClickOkListener) {
        super(c);
        this.title = title;
        this.content = content;
        this.onClickOkListener = onClickOkListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_info_dialog);

        btnClose = (Button) findViewById(R.id.btnClose);
        btnOk = (Button) findViewById(R.id.btnOk);
        tvTitle = (CustomTextView) findViewById(R.id.tvTitle);
        tvDescription = (CustomTextView) findViewById(R.id.tvDescription);
        ivInfoLogo = (ImageView) findViewById(R.id.ivInfoLogo);

        tvTitle.setText(title);
        tvDescription.setText(content);
        if(title.equals(ConstantVariable.TITLE_INFO)){
            ivInfoLogo.setImageResource(R.drawable.ic_info);
        } else if(title.equals(ConstantVariable.TITLE_WARNING)){
            ivInfoLogo.setImageResource(R.drawable.ic_warning_white);
        }

        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnOk.setOnClickListener(onClickOkListener);

        Window window = getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.dialog_animation;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.7f;
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(lp);

        setCancelable(false);
    }
}
