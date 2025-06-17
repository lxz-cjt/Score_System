package com.example.score_system1.service;

import com.example.score_system1.entity.ScoreLog;
import com.example.score_system1.mapper.ScoreLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreLogService {

    @Autowired
    private ScoreLogMapper scoreLogMapper;

    public void insertScoreLog(ScoreLog scoreLog) {
        scoreLogMapper.insertScoreLog(scoreLog);
    }

    public ScoreLog getScoreLogById(int logId) {
        return scoreLogMapper.selectScoreLogById(logId);
    }

    public List<ScoreLog> getScoreLogsByScoreId(String scoreId) {
        return scoreLogMapper.selectScoreLogsByScoreId(scoreId);
    }

    public List<ScoreLog> getScoreLogsByStudentId(String stId) {
        return scoreLogMapper.selectScoreLogsByStudentId(stId);
    }

    public List<ScoreLog> getScoreLogsByCourseId(String cId) {
        return scoreLogMapper.selectScoreLogsByCourseId(cId);
    }
}