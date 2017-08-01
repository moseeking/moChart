package com.youdy.mo.mochart.activity;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.youdy.mo.mochart.R;

/**
 * Created by mo on 2017/7/1.
 */

public class FirstDaySetDialog extends Dialog {
    private EditText etYear;
    private EditText etMonth;
    private EditText etDay;
    private Button btnCancel;
    private Button btnSetup;

    public FirstDaySetDialog(@NonNull Context context) {
        super(context);
        setupFirstDayDialog(context);
    }

   private void setupFirstDayDialog(final Context context) {

       View view = LayoutInflater.from(getContext()).inflate(R.layout.first_day_set_dialog_layout, null);
       this.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

       etYear = (EditText) view.findViewById(R.id.et_year) ;
       etMonth = (EditText) view.findViewById(R.id.et_month) ;
       etDay = (EditText) view.findViewById(R.id.et_day);
       btnCancel = (Button) view.findViewById(R.id.btn_cancel_first_day);
       btnCancel.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });
       btnSetup = (Button) view.findViewById(R.id.btn_setup_first_day);
       btnSetup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });
    }

    public int getYear(){
        return Integer.parseInt(etYear.getText().toString());
    }
    public int getMonth(){
        return Integer.parseInt(etMonth.getText().toString());
    }
    public int getDay(){
        return Integer.parseInt(etDay.getText().toString());
    }


    public void setBtnCancel(View.OnClickListener listener){
        btnCancel.setOnClickListener(listener);
    }
    public void setBtnSetup(View.OnClickListener listener){
        btnSetup.setOnClickListener(listener);
    }
}
