package com.example.score_system1.mapper;


import com.example.score_system1.entity.ScoreLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScoreLogMapper {
    void insertScoreLog(ScoreLog scoreLog);
    ScoreLog selectScoreLogById(int logId);
    List<ScoreLog> selectScoreLogsByScoreId(String scoreId);
    List<ScoreLog> selectScoreLogsByStudentId(String stId);
    List<ScoreLog> selectScoreLogsByCourseId(String cId);
}