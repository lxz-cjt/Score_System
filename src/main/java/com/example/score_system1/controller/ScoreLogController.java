package com.example.score_system1.controller;

import com.example.score_system1.entity.ScoreLog;
import com.example.score_system1.service.ScoreLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/score-logs")
public class ScoreLogController {

    @Autowired
    private ScoreLogService scoreLogService;

    @PostMapping
    public ResponseEntity<String> insertScoreLog(@RequestBody ScoreLog scoreLog) {
        try {
            scoreLogService.insertScoreLog(scoreLog);
            return ResponseEntity.ok("成绩日志添加成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("添加成绩日志失败: " + e.getMessage());
        }
    }

    @GetMapping("/{logId}")
    public ResponseEntity<ScoreLog> getScoreLogById(@PathVariable int logId) {
        try {
            ScoreLog scoreLog = scoreLogService.getScoreLogById(logId);
            if (scoreLog == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
            return ResponseEntity.ok(scoreLog);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/score/{scoreId}")
    public ResponseEntity<List<ScoreLog>> getScoreLogsByScoreId(@PathVariable String scoreId) {
        try {
            List<ScoreLog> scoreLogs = scoreLogService.getScoreLogsByScoreId(scoreId);
            return ResponseEntity.ok(scoreLogs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/student/{stId}")
    public ResponseEntity<List<ScoreLog>> getScoreLogsByStudentId(@PathVariable String stId) {
        try {
            List<ScoreLog> scoreLogs = scoreLogService.getScoreLogsByStudentId(stId);
            return ResponseEntity.ok(scoreLogs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/course/{cId}")
    public ResponseEntity<List<ScoreLog>> getScoreLogsByCourseId(@PathVariable String cId) {
        try {
            List<ScoreLog> scoreLogs = scoreLogService.getScoreLogsByCourseId(cId);
            return ResponseEntity.ok(scoreLogs);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}