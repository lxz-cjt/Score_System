package com.example.score_system1.controller;

import com.example.score_system1.entity.Appeal;
import com.example.score_system1.service.AppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appeals")
public class AppealController {

    @Autowired
    private AppealService appealService;

    @PostMapping
    public ResponseEntity<String> insertAppeal(@RequestBody Appeal appeal) {
        try {
            appealService.insertAppeal(appeal);
            return ResponseEntity.ok("申诉添加成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("添加申诉失败: " + e.getMessage());
        }
    }

    @GetMapping("/{apId}")
    public ResponseEntity<Appeal> getAppealById(@PathVariable String apId) {
        try {
            Appeal appeal = appealService.getAppealById(apId);
            if (appeal == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(null);
            }
            return ResponseEntity.ok(appeal);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/student/{stId}")
    public ResponseEntity<List<Appeal>> getAppealsByStudentId(@PathVariable String stId) {
        try {
            List<Appeal> appeals = appealService.getAppealsByStudentId(stId);
            return ResponseEntity.ok(appeals);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @PutMapping
    public ResponseEntity<String> updateAppeal(@RequestBody Appeal appeal) {
        try {
            appealService.updateAppeal(appeal);
            return ResponseEntity.ok("申诉更新成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("更新申诉失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{apId}")
    public ResponseEntity<String> deleteAppeal(@PathVariable String apId) {
        try {
            appealService.deleteAppeal(apId);
            return ResponseEntity.ok("申诉删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("删除申诉失败: " + e.getMessage());
        }
    }
}