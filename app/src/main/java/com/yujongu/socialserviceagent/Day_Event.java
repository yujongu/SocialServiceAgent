package com.yujongu.socialserviceagent;

import android.util.Pair;

import java.util.Calendar;
import java.util.Date;

public class Day_Event {

    //type = 0 -> day
    //type = 1 -> empty

    static int EMPTY = 1;
    static int DAY = 0;

    private boolean isClicked;
    private int type;
    private int year;
    private int month;
    private int date;
    private Pair<Date, Date> paidLeave;
    private Pair<Date, Date> sickLeave;
//    private Date paidLeave; //연차 시간
//    private Date sickLeave; //병가 시간
    private boolean hold;   //분할 복무 유무

    Day_Event(int type) {
        this.isClicked = false;
        this.type = type;
        this.hold = false;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setPaidLeave(Pair<Date, Date> paidLeave) {
        this.paidLeave = paidLeave;
    }

    public void setSickLeave(Pair<Date, Date> sickLeave) {
        this.sickLeave = sickLeave;
    }

    public Pair<Date, Date> getPaidLeave() {
        return paidLeave;
    }

    public Pair<Date, Date> getSickLeave() {
        return sickLeave;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public boolean isHold() {
        return hold;
    }

    public void setHold(boolean hold) {
        this.hold = hold;
    }
}
