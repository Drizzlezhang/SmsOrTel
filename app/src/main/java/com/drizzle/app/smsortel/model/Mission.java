package com.drizzle.app.smsortel.model;

/**
 * Created by Administrator on 2015/5/19.
 */
public class Mission {

    private int missionId;
    private String missionNumber;
    private String missionWord;
    private String missionHour;
    private String missionMinute;
    private String missionYear;
    private String missionMonth;
    private String missionDay;
    private int imageId;
    private String missionCompany;

    public String getMissionCompany() {
        return missionCompany;
    }

    public void setMissionCompany(String missionCompany) {
        this.missionCompany = missionCompany;
    }

    public int getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public String getMissionNumber() {
        return missionNumber;
    }

    public void setMissionNumber(String missionNumber) {
        this.missionNumber = missionNumber;
    }

    public String getMissionWord() {
        return missionWord;
    }

    public void setMissionWord(String missionWord) {
        this.missionWord = missionWord;
    }

    public String getMissionHour() {
        return missionHour;
    }

    public void setMissionHour(String missionHour) {
        this.missionHour = missionHour;
    }

    public String getMissionMinute() {
        return missionMinute;
    }

    public void setMissionMinute(String missionMinute) {
        this.missionMinute = missionMinute;
    }

    public String getMissionYear() {
        return missionYear;
    }

    public void setMissionYear(String missionYear) {
        this.missionYear = missionYear;
    }

    public String getMissionMonth() {
        return missionMonth;
    }

    public void setMissionMonth(String missionMonth) {
        this.missionMonth = missionMonth;
    }

    public String getMissionDay() {
        return missionDay;
    }

    public void setMissionDay(String missionDay) {
        this.missionDay = missionDay;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
