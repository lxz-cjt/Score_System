package com.example.score_system1.entity;

import java.util.Date;

public class Appeal {
    private String stId;
    private String aasId;
    private String apId;
    private Date apTime;
    private String apStatus;
    private String apContent;

    public Appeal(String stId, String aasId, String apId, Date apTime, String apStatus, String apContent) {
        this.stId = stId;
        this.aasId = aasId;
        this.apId = apId;
        this.apTime = apTime;
        this.apStatus = apStatus;
        this.apContent = apContent;
    }

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public String getAasId() {
        return aasId;
    }

    public void setAasId(String aasId) {
        this.aasId = aasId;
    }

    public String getApId() {
        return apId;
    }

    public void setApId(String apId) {
        this.apId = apId;
    }

    public Date getApTime() {
        return apTime;
    }

    public void setApTime(Date apTime) {
        this.apTime = apTime;
    }

    public String getApStatus() {
        return apStatus;
    }

    public void setApStatus(String apStatus) {
        this.apStatus = apStatus;
    }

    public String getApContent() {
        return apContent;
    }

    public void setApContent(String apContent) {
        this.apContent = apContent;
    }

    @Override
    public String toString() {
        return "Appeal{" +
                "stId='" + stId + '\'' +
                ", aasId='" + aasId + '\'' +
                ", apId='" + apId + '\'' +
                ", apTime=" + apTime +
                ", apStatus='" + apStatus + '\'' +
                ", apContent='" + apContent + '\'' +
                '}';
    }
}
