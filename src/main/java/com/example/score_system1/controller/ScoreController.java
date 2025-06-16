package com.example.score_system1.controller;

import com.example.score_system1.entity.Score;
import com.example.score_system1.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/scores")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @PostMapping
    public ResponseEntity<String> insertScore(@RequestBody Score score) {
        try {
            scoreService.insertScore(score);
            return ResponseEntity.ok("成绩添加成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("添加成绩失败: " + e.getMessage());
        }
    }

    @GetMapping("/{scoreId}")
    public ResponseEntity<Score> getScoreById(@PathVariable String scoreId) {
        try {
            Score score = scoreService.getScoreById(scoreId);
            if (score == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
            return ResponseEntity.ok(score);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/student/{stId}")
    public ResponseEntity<List<Score>> getScoresByStudentId(@PathVariable String stId) {
        try {
            List<Score> scores = scoreService.getScoresByStudentId(stId);
            return ResponseEntity.ok(scores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/course/{cId}")
    public ResponseEntity<List<Score>> getScoresByCourseId(@PathVariable String cId) {
        try {
            List<Score> scores = scoreService.getScoresByCourseId(cId);
            return ResponseEntity.ok(scores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping
    public ResponseEntity<String> updateScore(@RequestBody Score score) {
        try {
            scoreService.updateScore(score);
            return ResponseEntity.ok("成绩更新成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("更新成绩失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{scoreId}")
    public ResponseEntity<String> deleteScore(@PathVariable String scoreId) {
        try {
            scoreService.deleteScore(scoreId);
            return ResponseEntity.ok("成绩删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("删除成绩失败: " + e.getMessage());
        }
    }
}