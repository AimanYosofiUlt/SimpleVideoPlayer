package com.exmple.videoplayer_codewithnitish.Parsers;

public class Str {
    int num;
    int startTime,endTime;
    String text;

    public Str(){}

    public Str(int num, int startTime, int endTime, String text) {
        this.num = num;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = text;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
