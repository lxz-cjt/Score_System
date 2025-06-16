package com.example.score_system1.entity;

import java.util.Date;

public class AcademicAffairsStaff {
    private String aasId;
    private String aasName;

    public AcademicAffairsStaff(String aasId, String aasName) {
        this.aasId = aasId;
        this.aasName = aasName;
    }

    public String getAasId() {
        return aasId;
    }

    public void setAasId(String aasId) {
        this.aasId = aasId;
    }

    public String getAasName() {
        return aasName;
    }

    public void setAasName(String aasName) {
        this.aasName = aasName;
    }

    @Override
    public String toString() {
        return "AcademicAffairsStaff{" +
                "aasId='" + aasId + '\'' +
                ", aasName='" + aasName + '\'' +
                '}';
    }
}
