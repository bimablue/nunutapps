package com.blackbeltcoder.nunut.component;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.blackbeltcoder.nunut.R;
import com.blackbeltcoder.nunut.util.StringUtil;

import java.util.Hashtable;

/**
 * Created by ainozenbook on 10/9/2016.
 */

public class CustomFormDialog extends Dialog {

    private Activity a;
    private OnResultFormDialogListener onResultFormDialogListener;
    private EditText etEmailValue, etPasswordValue;
    private Button btnClose, btnOk;

    public interface OnResultFormDialogListener {
        public void OnResultFormDialog(String email, String password);
    }

    public CustomFormDialog(Activity a, Hashtable<String, String> param, OnResultFormDialogListener onResultFormDialogListener) {
        super(a);
        this.a = a;
        this.onResultFormDialogListener = onResultFormDialogListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_form_dialog);

        /*
        * INIT COMPONENT
        * */
        etEmailValue = (EditText) findViewById(R.id.etEmailValue);
        etPasswordValue = (EditText) findViewById(R.id.etPasswordValue);
        btnClose = (Button) findViewById(R.id.btnClose);
        btnOk = (Button) findViewById(R.id.btnOk);

        /*
        * BIND COMPONENT
        * */
        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = etEmailValue.getText().toString();
                String password = etPasswordValue.getText().toString();

                if(StringUtil.isNotNull(email) && StringUtil.isNotNull(password)){
                    onResultFormDialogListener.OnResultFormDialog(email, password);
                    dismiss();
                }
            }
        });

        /*
        * PREPARE COMPONENT
        * */
        Window window = getWindow();
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        window.getAttributes().windowAnimations = R.style.dialog_animation;
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = window.getAttributes();
        lp.dimAmount = 0.7f;
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(lp);
    }
}
