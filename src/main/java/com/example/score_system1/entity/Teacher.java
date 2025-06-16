package com.example.score_system1.entity;

public class Teacher {
    private String teId;
    private String teName;
    private String teMajor;

    public Teacher(String teId, String teName, String teMajor) {
        this.teId = teId;
        this.teName = teName;
        this.teMajor = teMajor;
    }

    public String getTeId() {
        return teId;
    }

    public void setTeId(String teId) {
        this.teId = teId;
    }

    public String getTeName() {
        return teName;
    }

    public void setTeName(String teName) {
        this.teName = teName;
    }

    public String getTeMajor() {
        return teMajor;
    }

    public void setTeMajor(String teMajor) {
        this.teMajor = teMajor;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teId='" + teId + '\'' +
                ", teName='" + teName + '\'' +
                ", teMajor='" + teMajor + '\'' +
                '}';
    }
}
