package com.neo.licensio.ui.activities.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.neo.licensio.R;
import com.neo.licensio.data.models.LoggedUser;
import com.neo.licensio.data.models.User;
import com.neo.licensio.network.ApiService;
import com.neo.licensio.network.JwtToken;
import com.neo.licensio.network.Verifier;

import java.io.IOException;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements ApiService.UpdateCallback {

    private EditText fullNameEditText;
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText countryEditText;
    private EditText cityEditText;
    private Button applyButton;
    private Button backButton;
    private Boolean updated_status;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fullNameEditText = (EditText) findViewById(R.id.etFullName);
        usernameEditText = (EditText) findViewById(R.id.etUsername);
        emailEditText = (EditText) findViewById(R.id.etEmail);
        phoneEditText = (EditText) findViewById(R.id.etPhoneNumber);
        countryEditText = (EditText) findViewById(R.id.etCountry);
        cityEditText = (EditText) findViewById(R.id.etCity);

        fullNameEditText.setText(LoggedUser.getCurrentUser().getFullName());
        usernameEditText.setText(LoggedUser.getCurrentUser().getUsername());
        emailEditText.setText(LoggedUser.getCurrentUser().getEmailAddress());
        if(!phoneEditText.getText().toString().isEmpty()){
            phoneEditText.setText(LoggedUser.getCurrentUser().getPhoneNumber());
        }
        if(!countryEditText.getText().toString().isEmpty()){
            countryEditText.setText(LoggedUser.getCurrentUser().getCountry());
        }
        if(!cityEditText.getText().toString().isEmpty()){
            cityEditText.setText(LoggedUser.getCurrentUser().getCity());
        }

        applyButton = findViewById(R.id.applyButton);
        backButton = findViewById(R.id.backButton);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiService = new ApiService();
                apiService.setClient(JwtToken.getJwtToken());

                User user = new User();
                String fullName = fullNameEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String emailAddress = emailEditText.getText().toString();
                String phoneNumber = phoneEditText.getText().toString();
                String country = countryEditText.getText().toString();
                String city = cityEditText.getText().toString();
                if(user.getFullName() != fullName){
                    user.setFullName(fullName);
                }
                if(user.getUsername() != username){
                    user.setUsername(username);
                }
                if(user.getEmailAddress() != emailAddress){
                    user.setEmailAddress(emailAddress);
                }
                if(user.getPhoneNumber() != phoneNumber){
                    user.setPhoneNumber(phoneNumber);
                }
                if(user.getCountry() != country){
                    Map<String, String> countryMap = Verifier.readCsvFile();
                    boolean isCountryValid = countryMap.containsValue(country);
                    if(isCountryValid){
                        user.setCountry(country);
                    }
                }
                if(user.getCity() != city){
                    user.setCity(city);
                }
                apiService.makeFullUpdateAccount(user, EditProfileActivity.this);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



    @Override
    public void onSuccess(Boolean updated) {
        updated_status = updated;
        if(updated_status){
            Toast.makeText(this,"Changes have been succesfully made!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Update has failed. Please try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(String errorMessage) {

    }

    @Override
    public void onFailure(IOException e) {

    }
}