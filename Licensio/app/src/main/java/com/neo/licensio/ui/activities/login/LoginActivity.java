package com.neo.licensio.ui.activities.login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.neo.licensio.R;
import com.neo.licensio.data.models.LoggedUser;
import com.neo.licensio.data.models.User;
import com.neo.licensio.network.ApiService;
import com.neo.licensio.ui.activities.homepage.Homepage;
import com.neo.licensio.ui.activities.signup.SignupActivity;

import org.apache.commons.lang3.BooleanUtils;
import org.opencv.android.OpenCVLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements ApiService.UserCallBack, ApiService.UserByUsernameCallback, ApiService.UserByEmailCallback, ApiService.UpdateCallback {

    static{

        if(OpenCVLoader.initDebug()){

            Log.d("Check","OpenCv configured successfully");

        } else{

            Log.d("Check","OpenCv doesn’t configured successfully");

        }

    }

    private EditText username_field;
    private EditText password_field;
    private TextView forgot_password;
    private TextView create_account;
    private EditText newPassword;
    private Button loginButton;

    //Authentication
    private Boolean authenticated;
    private String username;
    private User user;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.authenticated = false;

        //Edit Texts
        username_field = findViewById(R.id.etUsername);
        password_field = findViewById(R.id.etPassword);

        //Text Views
        forgot_password = findViewById(R.id.textViewForgotPassword);
        create_account = findViewById(R.id.textViewCreateAcc);

        //Buttons
        loginButton = findViewById(R.id.login_button);

        //Open SignUp Activity (when "Create an account!" is pressed)
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSignUpActivity();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startHomepageActivity();
            }
        });

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_forgot_password, null);

                EditText emailAddress = view.findViewById(R.id.recoverEmailEditText);
                newPassword = view.findViewById(R.id.recoverNewPasswordEditText);
                EditText reenterPassword = view.findViewById(R.id.recoverReenterPasswordEditText);
                Button recoverAccount = view.findViewById(R.id.recoverAccountDialogButton);

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setView(view);
                builder.create().show();

                recoverAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(newPassword.getText().toString().equals(reenterPassword.getText().toString())){
                            apiService.makeGetUserByEmail(emailAddress.getText().toString(), LoginActivity.this);
                        } else {
                            Toast.makeText(LoginActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    //SIGNUP ACTION
    public void startSignUpActivity(){
        this.username_field.setText("");
        this.password_field.setText("");

        Intent intent = new Intent(LoginActivity.this, SignupActivity.class); //create activity
        LoginActivity.this.startActivity(intent);  //start activity

    }

    //LOGIN ACTION
    public void startHomepageActivity(){
        apiService = new ApiService();
        //text in username field, to check user exists
        username = this.username_field.getText().toString();
        String password = this.password_field.getText().toString();

        //call to API to get user by username (if exists)
        apiService.makeGetAuthentication(username, password, this);
        //wait for resource to be available (user)
    }

    //for authentication
    @Override
    public void onSuccess(Boolean authenticated, String jwtToken) {
        this.authenticated = authenticated;

        if (BooleanUtils.isTrue(this.authenticated)){
            apiService.makeGetUserByUsername(username, this);
        } else {
            this.password_field.setText("");
            Handler mainHandler = new Handler(Looper.getMainLooper());
            Runnable toastRunnable = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Username and password do not match", Toast.LENGTH_SHORT).show();
                }
            };
            mainHandler.post(toastRunnable);
        }
    }

    //for searching by username
    @Override
    public void onSuccess(User user) {
        this.user = user;
        if(user.getVerified()) {
            LoggedUser.setCurrentUser(user);
            Intent intent = new Intent(LoginActivity.this, Homepage.class); //create activity
            intent.putExtra("User", this.user);
            LoginActivity.this.startActivity(intent);  //start activity
        } else {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            Runnable toastRunnable = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Account has not been verified yet.", Toast.LENGTH_LONG).show();
                }
            };
            mainHandler.post(toastRunnable);
        }
    }

    @Override
    public void onSuccess(Long userId) {
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("password", newPassword.getText().toString());
        ApiService apiService = new ApiService();

        apiService.makePartialUpdateAccount(LoggedUser.getCurrentUser().getUserId(),requestBodyMap, LoginActivity.this);
    }

    @Override
    public void onSuccess(Boolean updated) {
        Toast.makeText(this, "Password successfully modified", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String errorMessage) {
        Toast.makeText(this, "This user does not exist", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(IOException e) {
        Toast.makeText(this, "This user does not exist", Toast.LENGTH_SHORT).show();
    }
}