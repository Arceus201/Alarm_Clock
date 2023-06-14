package com.example.alarm.Model;

public class Setting {
    private int id,auto_silence;
    private String source;

    public Setting(int id, int auto_silence, String source) {
        this.id = id;
        this.auto_silence = auto_silence;
        this.source = source;
    }

    public Setting() {
    }

    public Setting(int auto_silence, String source) {
        this.auto_silence = auto_silence;
        this.source = source;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuto_silence() {
        return auto_silence;
    }

    public void setAuto_silence(int auto_silence) {
        this.auto_silence = auto_silence;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
