package com.example.score_system1.entity;

public class Student {
    private String stId;
    private String stName;
    private String stMajor;
    private int stGetCredit;

    public Student(String stId, String stName, String stMajor, int stGetCredit) {
        this.stId = stId;
        this.stName = stName;
        this.stMajor = stMajor;
        this.stGetCredit = stGetCredit;
    }

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public String getStName() {
        return stName;
    }

    public void setStName(String stName) {
        this.stName = stName;
    }

    public String getStMajor() {
        return stMajor;
    }

    public void setStMajor(String stMajor) {
        this.stMajor = stMajor;
    }

    public int getStGetCredit() {
        return stGetCredit;
    }

    public void setStGetCredit(int stGetCredit) {
        this.stGetCredit = stGetCredit;
    }

    @Override
    public String toString() {
        return "Student{" +
                "stId='" + stId + '\'' +
                ", stName='" + stName + '\'' +
                ", stMajor='" + stMajor + '\'' +
                ", stGetCredit=" + stGetCredit +
                '}';
    }
}
