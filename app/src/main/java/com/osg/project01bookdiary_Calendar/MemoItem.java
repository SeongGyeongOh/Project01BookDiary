package com.osg.project01bookdiary_Calendar;

public class MemoItem {
    int year, month, date;
    String memo;

    public MemoItem(int year, int month, int date, String memo) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.memo = memo;
    }

    public MemoItem() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
