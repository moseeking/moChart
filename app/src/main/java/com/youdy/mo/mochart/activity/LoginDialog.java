package com.youdy.mo.mochart.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.youdy.mo.mochart.R;

/**
 * Created by mo on 2017/6/22.
 */

public class LoginDialog extends Dialog {
    private EditText etPassword;
    private EditText etUsername;
    private EditText etRandomCode;
    private ImageView ivRandomCode;
    private Button btnCancel;
    private Button btnLogin;
    public LoginDialog(Context context) {
        super(context, R.style.DialogStyle);
        setupLoginDialog(context);
    }
    private void setupLoginDialog(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // instantiate the dialog with the custom Theme
         //  final LoginDialog dialog = new LoginDialog(context, R.style.DialogStyle);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.login_dialog_layout, null);
        this.addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        btnCancel = (Button) view.findViewById(R.id.btn_cancel);
        btnLogin = (Button) view.findViewById(R.id.btn_login) ;
        etUsername = (EditText) view.findViewById(R.id.et_username) ;
        etPassword = (EditText) view.findViewById(R.id.et_password) ;
        etRandomCode = (EditText) view.findViewById(R.id.et_random_code);
        ivRandomCode = (ImageView) view.findViewById(R.id.iv_random_code);
    }

    public void setBtnCancel(View.OnClickListener listener){
        btnCancel.setOnClickListener(listener);
    }
    public void setBtnLogin(View.OnClickListener listener){
        btnLogin.setOnClickListener(listener);
    }
    public void setRandomCode(Bitmap b){
        ivRandomCode.setImageBitmap(b);

    }
    public String getName(){
        return etUsername.getText().toString();
    }
    public String getPassword(){
        return etPassword.getText().toString();
    }
    public String getRandomCode(){
        return etRandomCode.getText().toString();
    }
}
