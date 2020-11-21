package com.oss_net.choloeksathe.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.oss_net.choloeksathe.R;
import com.oss_net.choloeksathe.utils.CommonTask;


public class ForgetPasswordActivity extends AppCompatActivity{

    EditText email;
    Button buttonFindPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email = findViewById(R.id.tilEmail);
        buttonFindPassword = findViewById(R.id.buttonPasswordChange);
        buttonFindPassword.setVisibility(View.GONE);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>0){
                    if(Patterns.EMAIL_ADDRESS.matcher(charSequence).matches()){
                        buttonFindPassword.setVisibility(View.VISIBLE);
                    }else{
                        buttonFindPassword.setVisibility(View.GONE);
                    }
                }else{
                    buttonFindPassword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        buttonFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonTask.showAlert(ForgetPasswordActivity.this, "Forget Password", "Service is currently unavailable");
            }
        });



    }


}
