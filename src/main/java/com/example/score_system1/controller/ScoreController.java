package com.example.score_system1.controller;

import com.example.score_system1.dto.ApiResponse;
import com.example.score_system1.entity.Score;
import com.example.score_system1.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @PostMapping
    public ApiResponse<String> insertScore(@RequestBody Score score) {
        try {
            scoreService.insertScore(score);
            return ApiResponse.success("成绩添加成功");
        } catch (Exception e) {
            return ApiResponse.error("添加成绩失败: " + e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<Score>> getAllScores() {
        try {
            List<Score> scores = scoreService.getAllScores();
            return ApiResponse.success("获取所有成绩列表成功", scores);
        } catch (Exception e) {
            return ApiResponse.error("获取所有成绩列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/{scoreId}")
    public ApiResponse<Score> getScoreById(@PathVariable String scoreId) {
        try {
            Score score = scoreService.getScoreById(scoreId);
            if (score == null) {
                return ApiResponse.error("成绩不存在", 404);
            }
            return ApiResponse.success("获取成绩信息成功", score);
        } catch (Exception e) {
            return ApiResponse.error("获取成绩信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/student/{stId}")
    public ApiResponse<List<Score>> getScoresByStudentId(@PathVariable String stId) {
        try {
            List<Score> scores = scoreService.getScoresByStudentId(stId);
            return ApiResponse.success("获取学生成绩列表成功", scores);
        } catch (Exception e) {
            return ApiResponse.error("获取学生成绩列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/course/{cId}")
    public ApiResponse<List<Score>> getScoresByCourseId(@PathVariable String cId) {
        try {
            List<Score> scores = scoreService.getScoresByCourseId(cId);
            return ApiResponse.success("获取课程成绩列表成功", scores);
        } catch (Exception e) {
            return ApiResponse.error("获取课程成绩列表失败: " + e.getMessage());
        }
    }

    @PutMapping
    public ApiResponse<String> updateScore(@RequestBody Score score) {
        try {
            scoreService.updateScore(score);
            return ApiResponse.success("成绩更新成功");
        } catch (Exception e) {
            return ApiResponse.error("更新成绩失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{scoreId}")
    public ApiResponse<String> deleteScore(@PathVariable String scoreId) {
        try {
            scoreService.deleteScore(scoreId);
            return ApiResponse.success("成绩删除成功");
        } catch (Exception e) {
            return ApiResponse.error("删除成绩失败: " + e.getMessage());
        }
    }
}