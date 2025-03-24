package com.neo.licensio.ui.activities.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.neo.licensio.R;
import com.neo.licensio.data.models.LoggedUser;
import com.neo.licensio.network.ApiService;
import com.neo.licensio.network.JwtToken;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements ApiService.UpdateCallback{

    private TextView initialsIconTextView;
    private TextView username;
    private TextView fullName;
    private TextView emailAddress;
    private TextView phoneNumber;
    private TextView country;
    private TextView city;
    private Button changePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialsIconTextView = findViewById(R.id.profileIconInitialsTextView);
        username = findViewById(R.id.usernameTextView);
        fullName = findViewById(R.id.valueFullNameTextView);
        emailAddress = findViewById(R.id.valueEmailTextView);
        phoneNumber = findViewById(R.id.valuePhoneTextView);
        country = findViewById(R.id.valueCountryTextView);
        city = findViewById(R.id.valueCityTextView);

        initialsIconTextView.setText(LoggedUser.getCurrentUser().getUsername().substring(0,2).toUpperCase());
        username.setText(LoggedUser.getCurrentUser().getUsername());
        fullName.setText(LoggedUser.getCurrentUser().getFullName());
        emailAddress.setText(LoggedUser.getCurrentUser().getEmailAddress());
        if (LoggedUser.getCurrentUser().getPhoneNumber() != null){
            phoneNumber.setText(LoggedUser.getCurrentUser().getPhoneNumber());
        }
        if (LoggedUser.getCurrentUser().getCountry() != null){
            country.setText(LoggedUser.getCurrentUser().getCountry());
        }
        if (LoggedUser.getCurrentUser().getCity() != null){
            city.setText(LoggedUser.getCurrentUser().getCity());
        }

        changePasswordButton = findViewById(R.id.changePasswordButton);

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(ProfileActivity.this).inflate(R.layout.dialog_change_password, null);

                EditText currentPassword = view.findViewById(R.id.etCurrentPassword);
                EditText newPassword = view.findViewById(R.id.etNewPassword);
                Button changePassword = view.findViewById(R.id.changePasswordDialogButton);

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setView(view);
                builder.create().show();

                changePassword.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(currentPassword.getText().toString().equals(LoggedUser.getCurrentUser().getPassword())) {
                            Map<String, String> requestBodyMap = new HashMap<>();
                            requestBodyMap.put("password", newPassword.getText().toString());
                            ApiService apiService = new ApiService();
                            apiService.setClient(JwtToken.getJwtToken());

                            apiService.makePartialUpdateAccount(LoggedUser.getCurrentUser().getUserId(),requestBodyMap, ProfileActivity.this);
                        } else {
                            Toast.makeText(ProfileActivity.this, "Incorrect Current Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void onSuccess(Boolean updated) {
        if(updated){
            Toast.makeText(this, "Password Changed Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(String errorMessage) {

    }

    @Override
    public void onFailure(IOException e) {

    }
}