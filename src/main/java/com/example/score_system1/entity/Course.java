package com.example.score_system1.entity;

public class Course {
    private String cId;
    private String cName;
    private short cCredit;

    public Course(String cId, String cName, short cCredit) {
        this.cId = cId;
        this.cName = cName;
        this.cCredit = cCredit;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public short getcCredit() {
        return cCredit;
    }

    public void setcCredit(short cCredit) {
        this.cCredit = cCredit;
    }

    @Override
    public String toString() {
        return "Course{" +
                "cId='" + cId + '\'' +
                ", cName='" + cName + '\'' +
                ", cCredit=" + cCredit +
                '}';
    }
}
