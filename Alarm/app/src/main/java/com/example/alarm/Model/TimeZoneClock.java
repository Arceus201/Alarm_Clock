package com.example.alarm.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class TimeZoneClock implements Parcelable {
    private int id;
    private String idtimezone;
    private String City,time,date;


    public TimeZoneClock(int id, String idtimezone) {
        this.id = id;
        this.idtimezone = idtimezone;
    }

    protected TimeZoneClock(Parcel in) {
        id = in.readInt();
        idtimezone = in.readString();
        City = in.readString();
        time = in.readString();
        date = in.readString();
    }

    public static final Creator<TimeZoneClock> CREATOR = new Creator<TimeZoneClock>() {
        @Override
        public TimeZoneClock createFromParcel(Parcel in) {
            return new TimeZoneClock(in);
        }

        @Override
        public TimeZoneClock[] newArray(int size) {
            return new TimeZoneClock[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdtimezone() {
        return idtimezone;
    }

    public void setIdtimezone(String idtimezone) {
        this.idtimezone = idtimezone;
    }

    public TimeZoneClock(String city, String time, String date) {
        City = city;
        this.time = time;
        this.date = date;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(idtimezone);
        dest.writeString(City);
        dest.writeString(time);
        dest.writeString(date);
    }
}
