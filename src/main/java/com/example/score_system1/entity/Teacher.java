package com.example.score_system1.entity;

public class Teacher {
    private String teId;
    private String teName;
    private String teMajor;
    private String password;

    public Teacher(String teId, String teName, String teMajor, String password) {
        this.teId = teId;
        this.teName = teName;
        this.teMajor = teMajor;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "teId='" + teId + '\'' +
                ", teName='" + teName + '\'' +
                ", teMajor='" + teMajor + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
