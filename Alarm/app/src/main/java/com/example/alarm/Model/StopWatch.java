package com.example.alarm.Model;

public class StopWatch {
    private  int id;
    private String time, lap;

    public StopWatch() {
    }

    public StopWatch(String time) {
        this.time = time;
    }

    public StopWatch(String time, String lap) {
        this.time = time;
        this.lap = lap;
    }

    public StopWatch(int id, String time, String lap) {
        this.id = id;
        this.time = time;
        this.lap = lap;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLap() {
        return lap;
    }

    public void setLap(String lap) {
        this.lap = lap;
    }
}
