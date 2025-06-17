package com.example.score_system1.entity;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public class AcademicAffairsStaff {
    private String aasId;
    private String aasName;
    private String password;

    public AcademicAffairsStaff(String aasId, String aasName, String password) {
        this.aasId = aasId;
        this.aasName = aasName;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AcademicAffairsStaff{" +
                "aasId='" + aasId + '\'' +
                ", aasName='" + aasName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
