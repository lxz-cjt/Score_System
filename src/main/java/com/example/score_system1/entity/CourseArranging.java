package com.example.score_system1.entity;

public class CourseArranging {
    private String cId;
    private String teId;
    private String caId;

    public CourseArranging(String cId, String teId, String caId) {
        this.cId = cId;
        this.teId = teId;
        this.caId = caId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getTeId() {
        return teId;
    }

    public void setTeId(String teId) {
        this.teId = teId;
    }

    public String getCaId() {
        return caId;
    }

    public void setCaId(String caId) {
        this.caId = caId;
    }

    @Override
    public String toString() {
        return "CourseArranging{" +
                "cId='" + cId + '\'' +
                ", teId='" + teId + '\'' +
                ", caId='" + caId + '\'' +
                '}';
    }
}
