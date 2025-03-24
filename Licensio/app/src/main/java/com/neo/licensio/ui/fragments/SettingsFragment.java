package com.neo.licensio.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import com.neo.licensio.R;
import com.neo.licensio.data.models.FeedbackCategories;
import com.neo.licensio.data.models.LoggedUser;
import com.neo.licensio.data.models.User;
import com.neo.licensio.network.ApiService;
import com.neo.licensio.network.JwtToken;
import com.neo.licensio.ui.activities.profile.EditProfileActivity;
import com.neo.licensio.ui.activities.homepage.Homepage;
import com.neo.licensio.ui.activities.login.LoginActivity;
import com.neo.licensio.ui.activities.profile.ProfileActivity;

import java.io.IOException;

public class SettingsFragment extends Fragment implements ApiService.DeleteCallback, ApiService.FeedbackCallback{

    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView changePermissions;
    private TextView giveFeedback;
    private Button deleteAccountButton;
    private Button editProfileButton;
    private User user;
    private String selectedCategory;
    public SettingsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        user = ((Homepage) getContext()).getUser();

        usernameTextView = rootView.findViewById(R.id.username_settings);
        emailTextView = rootView.findViewById(R.id.email_settings);
        changePermissions = rootView.findViewById(R.id.permissionsTextView);
        giveFeedback = rootView.findViewById(R.id.feedbackTextView);
        deleteAccountButton = rootView.findViewById(R.id.deleteAccButton);
        editProfileButton = rootView.findViewById(R.id.editProfileButton);

        usernameTextView.setText(user.getUsername());
        emailTextView.setText(user.getEmailAddress());

        changePermissions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));
                startActivity(intent);
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService apiService = new ApiService();
                apiService.setClient(JwtToken.getJwtToken());

                apiService.makeDeleteUser(user.getUserId(), SettingsFragment.this);
            }
        });

        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });

        giveFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(SettingsFragment.this.getActivity()).inflate(R.layout.dialog_feedback, null);

                Spinner spinner = view.findViewById(R.id.categorySpinner);
                EditText feedback = view.findViewById(R.id.feedbackEditText);
                Button submitFeedback = view.findViewById(R.id.submitFeedbackButton);

                ArrayAdapter<FeedbackCategories> adapter = new ArrayAdapter<>(SettingsFragment.this.getActivity(), android.R.layout.simple_spinner_item, FeedbackCategories.values());
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(adapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        FeedbackCategories feedbackCategory = (FeedbackCategories) parent.getSelectedItem();
                        selectedCategory = feedbackCategory.toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsFragment.this.getActivity());
                builder.setView(view);
                AlertDialog dialog = builder.create();
                dialog.show();

                submitFeedback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedCategory = spinner.getSelectedItem().toString();
                        if(!feedback.getText().toString().isEmpty() && !selectedCategory.isEmpty()){
                            ApiService apiService = new ApiService();
                            apiService.setClient(JwtToken.getJwtToken());

                            apiService.makeSubmitFeedback(feedback.getText().toString(), selectedCategory, LoggedUser.getCurrentUser().getUserId(), SettingsFragment.this);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getActivity(), "Please fill in the text field.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return rootView;
    }

    @Override
    public void onSuccess(Boolean deleted) {
        Toast.makeText(getActivity(), "Your account has been deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onError(String errorMessage) {

    }

    @Override
    public void onFailure(IOException e) {

    }

    @Override
    public void onSuccess() {
        SettingsFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SettingsFragment.this.getActivity(), "Feedback Submitted Successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}