package com.example.alarm.Model;

public class MTimer {
    private int id;
    private String Time;

    public MTimer() {
    }

    public MTimer(int id, String time) {
        this.id = id;
        Time = time;
    }

    public MTimer(String time) {
        Time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
