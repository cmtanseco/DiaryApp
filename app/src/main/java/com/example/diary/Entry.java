package com.example.diary;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Entry {
    public String userid;
    public String title;
    public String text;
    public String date;

    public Entry() {

    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public String getUserid() {
        return userid;
    }

    public Entry(String userid, String date, String title, String text) {
        this.userid = userid;
        this.date = date;
        this.title = title;
        this.text = text;

    }



}