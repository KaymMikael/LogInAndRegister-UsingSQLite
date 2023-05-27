package com.example.loginandregister.Models;

import android.annotation.SuppressLint;

import java.time.LocalTime;

public class Day {
    @SuppressLint("NewApi")
    LocalTime time = LocalTime.now();
    @SuppressLint("NewApi")
    int hour = time.getHour();

    public Day(int hour) {
        this.hour = hour;
    }

    public boolean isMorning() {
        return hour > 0 && hour < 12;
    }
    public boolean isAfternoon() {
        return hour >= 12 && hour < 18;
    }
    public boolean isEvening (){
        return hour >=18 && hour <=23;
    }
}
