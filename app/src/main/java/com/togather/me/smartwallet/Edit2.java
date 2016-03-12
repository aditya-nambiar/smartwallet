package com.togather.me.smartwallet;

/**
 * Created by aditya on 3/12/16.
 */

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;


public class Edit2 extends Dialog {
    //private InterfaceUtils.EditDialogListener mOtpDialogListener;
    Button btnSubmit;
    private TimePicker timePicker1;
    private TextInputLayout desp;
    private TextInputLayout amt;
    private TextView amt_txt;
    private TextView desp_txt;
    private TextView time_txt;
    private CashFlow cash;
    private Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_layout2);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    public void setCashFlow(CashFlow cash, Adapter adapter){
        this.cash = cash;
        this.adapter = adapter;
    }

    public Edit2(Context context) {
        super(context);
    }

    public Edit2(Context context, int theme) {
        super(context, theme);
    }

    protected Edit2(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


}
