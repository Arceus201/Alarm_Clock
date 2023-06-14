package com.example.alarm.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Time implements  Parcelable{
    private String gio,repeat,phut,label,source;
    private int id,mode,mode_autoSilence;

    public Time() {
    }



    public Time(String gio, String phut, int mode) {
        this.gio = gio;
        this.phut = phut;
        this.mode = mode;
    }

    public Time(int id, String gio, String phut, int mode,String repeat,String label,String source,int mode_autoSilence) {
        this.id = id;
        this.gio = gio;
        this.phut = phut;
        this.mode = mode;
        this.repeat = repeat;
        this.label = label;
        this.source = source;
        this.mode_autoSilence = mode_autoSilence;
    }

    protected Time(Parcel in) {
        id = in.readInt();
        gio = in.readString();
        phut = in.readString();
        mode = in.readInt();
        repeat = in.readString();
        label = in.readString();
        source = in.readString();
        mode_autoSilence = in.readInt();
    }

    public static final Creator<Time> CREATOR = new Creator<Time>() {
        @Override
        public Time createFromParcel(Parcel in) {
            return new Time(in);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Time(String gio, String phut, int mode,String repeat,String label,String source,int mode_autoSilence) {
        this.gio = gio;
        this.phut = phut;
        this.mode = mode;
        this.repeat = repeat;
        this.label = label;
        this.source = source;
        this.mode_autoSilence = mode_autoSilence;
    }

    public String getPhut() {
        return phut;
    }

    public void setPhut(String phut) {
        this.phut = phut;
    }

    public String getGio() {
        return gio;
    }

    public void setGio(String gio) {
        this.gio = gio;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public int getMode_autoSilence() {
        return mode_autoSilence;
    }

    public void setMode_autoSilence(int mode_autoSilence) {
        this.mode_autoSilence = mode_autoSilence;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(gio);
            dest.writeString(phut);
            dest.writeInt(mode);
            dest.writeString(repeat);
            dest.writeString(label);
            dest.writeString(source);
            dest.writeInt(mode_autoSilence);
    }

}
