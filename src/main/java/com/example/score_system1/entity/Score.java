package com.example.score_system1.entity;

public class Score {
    private int dailyScore;
    private int examScore;
    private int totalScore;
    private String creditGainCondition;
    private  boolean makeUpExam;
    private String scoreId;
    private String stId;
    private String cId;

    public Score(int dailyScore, int examScore, int totalScore, String creditGainCondition, boolean makeUpExam, String scoreId, String stId, String cId) {
        this.dailyScore = dailyScore;
        this.examScore = examScore;
        this.totalScore = totalScore;
        this.creditGainCondition = creditGainCondition;
        this.makeUpExam = makeUpExam;
        this.scoreId = scoreId;
        this.stId = stId;
        this.cId = cId;
    }

    public int getDailyScore() {
        return dailyScore;
    }

    public void setDailyScore(int dailyScore) {
        this.dailyScore = dailyScore;
    }

    public int getExamScore() {
        return examScore;
    }

    public void setExamScore(int examScore) {
        this.examScore = examScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public String getCreditGainCondition() {
        return creditGainCondition;
    }

    public void setCreditGainCondition(String creditGainCondition) {
        this.creditGainCondition = creditGainCondition;
    }

    public boolean isMakeUpExam() {
        return makeUpExam;
    }

    public void setMakeUpExam(boolean makeUpExam) {
        this.makeUpExam = makeUpExam;
    }

    public String getScoreId() {
        return scoreId;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
    }

    public String getStId() {
        return stId;
    }

    public void setStId(String stId) {
        this.stId = stId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    @Override
    public String toString() {
        return "Score{" +
                "dailyScore=" + dailyScore +
                ", examScore=" + examScore +
                ", totalScore=" + totalScore +
                ", creditGainCondition='" + creditGainCondition + '\'' +
                ", makeUpExam=" + makeUpExam +
                ", scoreId='" + scoreId + '\'' +
                ", stId='" + stId + '\'' +
                ", cId='" + cId + '\'' +
                '}';
    }
}
