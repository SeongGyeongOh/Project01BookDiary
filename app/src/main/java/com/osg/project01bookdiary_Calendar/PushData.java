package com.osg.project01bookdiary_Calendar;

public class PushData {
    String title;
    String message;
    String token;

    public PushData(String title, String message, String token) {
        this.title = title;
        this.message = message;
        this.token = token;
    }

    public PushData() {
    }
}
