package com.example.score_system1.controller;

import com.example.score_system1.dto.ApiResponse;
import com.example.score_system1.entity.ScoreLog;
import com.example.score_system1.service.ScoreLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/score-logs")
public class ScoreLogController {

    @Autowired
    private ScoreLogService scoreLogService;

    @PostMapping
    public ApiResponse<String> insertScoreLog(@RequestBody ScoreLog scoreLog) {
        try {
            scoreLogService.insertScoreLog(scoreLog);
            return ApiResponse.success("成绩日志添加成功");
        } catch (Exception e) {
            return ApiResponse.error("添加成绩日志失败: " + e.getMessage());
        }
    }

    @GetMapping("/{logId}")
    public ApiResponse<ScoreLog> getScoreLogById(@PathVariable int logId) {
        try {
            ScoreLog scoreLog = scoreLogService.getScoreLogById(logId);
            if (scoreLog == null) {
                return ApiResponse.error("成绩日志不存在", 404);
            }
            return ApiResponse.success("获取成绩日志信息成功", scoreLog);
        } catch (Exception e) {
            return ApiResponse.error("获取成绩日志信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/score/{scoreId}")
    public ApiResponse<List<ScoreLog>> getScoreLogsByScoreId(@PathVariable String scoreId) {
        try {
            List<ScoreLog> scoreLogs = scoreLogService.getScoreLogsByScoreId(scoreId);
            return ApiResponse.success("获取成绩日志列表成功", scoreLogs);
        } catch (Exception e) {
            return ApiResponse.error("获取成绩日志列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/student/{stId}")
    public ApiResponse<List<ScoreLog>> getScoreLogsByStudentId(@PathVariable String stId) {
        try {
            List<ScoreLog> scoreLogs = scoreLogService.getScoreLogsByStudentId(stId);
            return ApiResponse.success("获取学生成绩日志列表成功", scoreLogs);
        } catch (Exception e) {
            return ApiResponse.error("获取学生成绩日志列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/course/{cId}")
    public ApiResponse<List<ScoreLog>> getScoreLogsByCourseId(@PathVariable String cId) {
        try {
            List<ScoreLog> scoreLogs = scoreLogService.getScoreLogsByCourseId(cId);
            return ApiResponse.success("获取课程成绩日志列表成功", scoreLogs);
        } catch (Exception e) {
            return ApiResponse.error("获取课程成绩日志列表失败: " + e.getMessage());
        }
    }
}