package com.example.score_system1.entity;

import java.sql.Date;

public class ScoreLog {
    private int logId;
    private String scoreId;
    private String stId;
    private String cId;
    private String operationType;
    private int oldTotalScore;
    private int newTotalScore;
    private String oldCreditCondition;
    private String newCreditCondition;
    private Date operationTime;
    private String operationUser;

    public ScoreLog(int logId, String scoreId, String stId, String cId, String operationType, int oldTotalScore, int newTotalScore, String oldCreditCondition, String newCreditCondition, Date operationTime, String operationUser) {
        this.logId = logId;
        this.scoreId = scoreId;
        this.stId = stId;
        this.cId = cId;
        this.operationType = operationType;
        this.oldTotalScore = oldTotalScore;
        this.newTotalScore = newTotalScore;
        this.oldCreditCondition = oldCreditCondition;
        this.newCreditCondition = newCreditCondition;
        this.operationTime = operationTime;
        this.operationUser = operationUser;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
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

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public int getOldTotalScore() {
        return oldTotalScore;
    }

    public void setOldTotalScore(int oldTotalScore) {
        this.oldTotalScore = oldTotalScore;
    }

    public int getNewTotalScore() {
        return newTotalScore;
    }

    public void setNewTotalScore(int newTotalScore) {
        this.newTotalScore = newTotalScore;
    }

    public String getOldCreditCondition() {
        return oldCreditCondition;
    }

    public void setOldCreditCondition(String oldCreditCondition) {
        this.oldCreditCondition = oldCreditCondition;
    }

    public String getNewCreditCondition() {
        return newCreditCondition;
    }

    public void setNewCreditCondition(String newCreditCondition) {
        this.newCreditCondition = newCreditCondition;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public String getOperationUser() {
        return operationUser;
    }

    public void setOperationUser(String operationUser) {
        this.operationUser = operationUser;
    }

    @Override
    public String toString() {
        return "ScoreLog{" +
                "logId=" + logId +
                ", scoreId='" + scoreId + '\'' +
                ", stId='" + stId + '\'' +
                ", cId='" + cId + '\'' +
                ", operationType='" + operationType + '\'' +
                ", oldTotalScore=" + oldTotalScore +
                ", newTotalScore=" + newTotalScore +
                ", oldCreditCondition='" + oldCreditCondition + '\'' +
                ", newCreditCondition='" + newCreditCondition + '\'' +
                ", operationTime=" + operationTime +
                ", operationUser='" + operationUser + '\'' +
                '}';
    }
}
