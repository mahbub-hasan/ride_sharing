package com.oss_net.choloeksathe.utils;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

/**
 * Created by Noor Nabiul Alam Siddiqui on 12/30/2017.
 * siddiqui.sazal@gmail.com
 */

public class InputValidation  {

   private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
   private static final Pattern PHONE_NUMBER = Pattern.compile("^(8801|01)+([1356789])\\d{8}$");
   private static final Pattern NID = Pattern.compile("^\\d{17}$");

    private String hint;

    public InputValidation() {
    }


    private boolean validate(EditText editText){
          if (editText.getText().toString().trim().isEmpty()){
              editText.setError(hint + " is required!");
              return false;
          }else {
              editText.setError(null);
              return true;
          }
    }

    private boolean validate(TextView textView){
        if(textView.getText().toString().trim().isEmpty()){
            textView.setError(hint + " is required!");
            return false;
        }else{
            textView.setError(null);
            return true;
        }
    }

    private boolean validate(EditText editText, String otherType){
        otherType = otherType.toLowerCase();
        if (otherType.contentEquals("email") && validate(editText)){
            if (!EMAIL_PATTERN.matcher(editText.getText().toString().trim()).matches()){
                editText.setError(hint + " not correct!");
                 return false;
            }else {
                editText.setError(null);
                return true;
            }
        }else if (otherType.contentEquals("phone number") && validate(editText)){
            if (!PHONE_NUMBER.matcher(editText.getText().toString().trim()).matches()){
                editText.setError(hint +" not correct!");
                return false;
            }else {
                editText.setError(null);
                return true;
            }
        }else if (otherType.contentEquals("nid") && validate(editText)){
            if (!NID.matcher(editText.getText().toString().trim()).matches()){
                editText.setError(hint + " not correct!");
                return false;
            }else {
                editText.setError(null);
                return true;
            }
        }
        else return validate(editText);
    }

    private void setHint(String hint){ this.hint = hint;}

    public boolean getResult(EditText editText, @NonNull String otherValidation){
        try {
            setHint(((TextInputLayout)editText.getParent().getParent()).getHint().toString().trim());

        }catch (Exception e){e.printStackTrace();
            try {
                setHint(editText.getHint().toString().trim());
            }catch (Exception ex){ex.printStackTrace();
                setHint("");
            }
        }
        return validate(editText, otherValidation);
    }

    public boolean getResult(TextInputLayout textInputLayout, @NonNull String otherValidation){
        setHint(textInputLayout.getHint().toString().trim());
        return validate(textInputLayout.getEditText(), otherValidation);
    }

    public boolean getResult(EditText editText){
        try {
            setHint(((TextInputLayout)editText.getParent().getParent()).getHint().toString().trim());

        }catch (Exception e){e.printStackTrace();
            try {
                setHint(editText.getHint().toString().trim());
            }catch (Exception ex){ex.printStackTrace();
                setHint("");
            }
        }
        return validate(editText);
    }

    public boolean getResult(TextView textView){
        try {
            setHint(((TextView) textView.getParent().getParent()).getHint().toString().trim());
        }catch (Exception ex){
            ex.printStackTrace();
            try {
                setHint(textView.getHint().toString().trim());
            }catch (Exception e){
                e.printStackTrace();
                setHint("");
            }
        }
        return validate(textView);
    }

    public boolean getResult(TextInputLayout textInputLayout){
        setHint(textInputLayout.getHint().toString().trim());
        return validate(textInputLayout.getEditText());
    }

}
