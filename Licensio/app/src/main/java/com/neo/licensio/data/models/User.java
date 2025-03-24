package com.neo.licensio.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User implements Parcelable{
    private Long userId;
    private String fullName;
    private String username;
    private String emailAddress;
    private String password;
    private String phoneNumber;
    private String country;
    private String city;
    private String verificationToken;
    private Boolean verified;

    protected User(Parcel in) {
        if (in.readByte() == 0) {
            userId = null;
        } else {
            userId = in.readLong();
        }
        fullName = in.readString();
        username = in.readString();
        emailAddress = in.readString();
        password = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public User(Long userId, String fullName, String username, String emailAddress, String password) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public static User fromJson(String json) throws JSONException{
        JSONObject jsonObject = new JSONObject(json);
        return User.builder()
                .userId(jsonObject.getLong("userId"))
                .fullName(jsonObject.getString("fullName"))
                .username(jsonObject.getString("username"))
                .emailAddress(jsonObject.getString("emailAddress"))
                .password(jsonObject.getString("password"))
                .build();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (userId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(userId);
        }
        dest.writeString(fullName);
        dest.writeString(username);
        dest.writeString(emailAddress);
        dest.writeString(password);
    }
}
