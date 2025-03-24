package com.neo.licensio.data.models;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Medicine implements Parcelable {
    private Long id;
    private String name;

    private String substitute0;
    private String substitute1;
    private String substitute2;
    private String substitute3;
    private String substitute4;

    private String sideEffect1;
    private String sideEffect2;
    private String sideEffect3;
    private String sideEffect4;
    private String sideEffect5;

    private String use0;
    private String use1;
    private String use2;
    private String use3;

    private String chemicalClass;
    private String habitForming;
    private String therapeuticClass;
    private String actionClass;

    protected Medicine(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        substitute0 = in.readString();
        substitute1 = in.readString();
        substitute2 = in.readString();
        substitute3 = in.readString();
        substitute4 = in.readString();
        sideEffect1 = in.readString();
        sideEffect2 = in.readString();
        sideEffect3 = in.readString();
        sideEffect4 = in.readString();
        sideEffect5 = in.readString();
        use0 = in.readString();
        use1 = in.readString();
        use2 = in.readString();
        use3 = in.readString();
        chemicalClass = in.readString();
        habitForming = in.readString();
        therapeuticClass = in.readString();
        actionClass = in.readString();
    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (id == null){
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(name);

        dest.writeString(substitute0);
        dest.writeString(substitute1);
        dest.writeString(substitute2);
        dest.writeString(substitute3);
        dest.writeString(substitute4);

        dest.writeString(sideEffect1);
        dest.writeString(sideEffect2);
        dest.writeString(sideEffect3);
        dest.writeString(sideEffect4);
        dest.writeString(sideEffect5);

        dest.writeString(use0);
        dest.writeString(use1);
        dest.writeString(use2);
        dest.writeString(use3);

        dest.writeString(chemicalClass);
        dest.writeString(habitForming);
        dest.writeString(therapeuticClass);
        dest.writeString(actionClass);
    }
}
