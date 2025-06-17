package com.example.score_system1.mapper;

import com.example.score_system1.entity.Score;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScoreMapper {
    void insertScore(Score score);
    Score selectScoreById(String scoreId);
    List<Score> selectScoresByStudentId(String stId);
    List<Score> selectScoresByCourseId(String cId);
    void updateScore(Score score);
    void deleteScore(String scoreId);
}