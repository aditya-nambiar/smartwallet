package com.togather.me.smartwallet;

/**
 * Created by aditya on 3/12/16.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

public class Edit extends Dialog {
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
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_layout);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        timePicker1 = (TimePicker) findViewById(R.id.timePicker1);
        if ( cash.ampm.equals("PM") )
        timePicker1.setCurrentHour(cash.time_hours + 12 );
        else
            timePicker1.setCurrentHour(cash.time_hours);

        timePicker1.setCurrentMinute(cash.time_minutes);

        desp = (TextInputLayout) findViewById(R.id.desp_input);
        amt = (TextInputLayout) findViewById(R.id.amt_input);
        amt_txt = (TextView) findViewById(R.id.amt_txt);
        desp_txt = (TextView) findViewById(R.id.desp_txt);
        time_txt = (TextView) findViewById(R.id.time_txt);
        EditText temp = (EditText)findViewById(R.id.desp_t);
        temp.setText(cash.desp);
        EditText temp1 = (EditText)findViewById(R.id.amt_t);
        temp1.setText(String.valueOf(cash.amt));
        amt_txt.setText("Amount");
        desp_txt.setText("Description");
        time_txt.setText("Time");


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cash.desp = desp.getEditText().getText().toString();
                cash.amt = Integer.parseInt(amt.getEditText().getText().toString());
                int hour = timePicker1.getCurrentHour();
                int min = timePicker1.getCurrentMinute();
                if (hour == 0) {
                    hour += 12;
                    cash.ampm = "AM";
                } else if (hour == 12) {
                    cash.ampm = "PM";
                } else if (hour > 12) {
                    hour -= 12;
                    cash.ampm = "PM";
                } else {
                    cash.ampm = "AM";
                }

                cash.time_hours = hour;
                cash.time_minutes = min;
                ScrollingActivity.cashFlowList.set(position, cash);
                getActivity().refresh_file();
                adapter.notifyDataSetChanged();
                dismiss();
            }
        });
    }

    private ScrollingActivity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (ScrollingActivity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    public void setCashFlow(CashFlow cash, Adapter adapter){
        this.cash = cash;
        this.adapter = adapter;
    }

    public Edit(Context context) {
        super(context);
    }

    public Edit(Context context, int pos) {
        super(context);
        position = pos;
        Activity owner = (context instanceof Activity) ? (Activity)context : null;
        if (owner != null) {
            // owner activity is defined here
        }

    }

    protected Edit(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }




}
