package com.neo.licensio.ui.activities.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.neo.licensio.R;
import com.neo.licensio.network.ApiService;

import org.apache.commons.lang3.BooleanUtils;

import java.io.IOException;

public class SignupActivity extends AppCompatActivity implements ApiService.RegisterCallback {

    //Edit Texts
    private EditText full_name_field;
    private EditText username_field;
    private EditText email_field;
    private EditText enter_password_field;
    private EditText reenter_password_field;
    //Buttons
    private Button signup_button;
    private Button back_button;

    //Inside
    private Boolean registered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Edit Texts
        full_name_field = (EditText) findViewById(R.id.etFullName);
        username_field = (EditText) findViewById(R.id.etUsername);
        email_field = (EditText) findViewById(R.id.etEmail);
        enter_password_field = (EditText) findViewById(R.id.etEnterPassword);
        reenter_password_field = (EditText) findViewById(R.id.etReEnterPassword);

        //Buttons
        signup_button = (Button) findViewById(R.id.applyButton);
        back_button = (Button) findViewById(R.id.backButton);

        //terminate activity (go back to login)
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

    }

    public void registerUser(){
        ApiService apiService = new ApiService();

        String fullName = this.full_name_field.getText().toString();
        String username = this.username_field.getText().toString();;
        String emailAddress = this.email_field.getText().toString();;
        String password = this.enter_password_field.getText().toString();;
        String reenteredPassword = this.reenter_password_field.getText().toString();

        if(!password.equals(reenteredPassword)){
            Toast.makeText(getApplicationContext(), "Re-entered password does not match", Toast.LENGTH_SHORT).show();
            this.enter_password_field.setText("");
            this.reenter_password_field.setText("");
        } else {
            apiService.makeRegisterAccount(fullName, username, emailAddress, password, this);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(BooleanUtils.isTrue(this.registered)){
                Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_SHORT).show();
                finish();
            }
        }


    }

    @Override
    public void onSuccess(Boolean registered) {
        this.registered = registered;
    }

    @Override
    public void onError(String errorMessage) {
        //idk
    }

    @Override
    public void onFailure(IOException e) {
        //idk
    }
}