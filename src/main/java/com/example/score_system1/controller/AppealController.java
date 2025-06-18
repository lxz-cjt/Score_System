package com.example.score_system1.controller;

import com.example.score_system1.dto.ApiResponse;
import com.example.score_system1.entity.Appeal;
import com.example.score_system1.service.AppealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appeals")
public class AppealController {

    @Autowired
    private AppealService appealService;

    @PostMapping
    public ApiResponse<String> insertAppeal(@RequestBody Appeal appeal) {
        try {
            appealService.insertAppeal(appeal);
            return ApiResponse.success("申诉添加成功");
        } catch (Exception e) {
            return ApiResponse.error("添加申诉失败: " + e.getMessage());
        }
    }

    @GetMapping("/{apId}")
    public ApiResponse<Appeal> getAppealById(@PathVariable String apId) {
        try {
            Appeal appeal = appealService.getAppealById(apId);
            if (appeal == null) {
                return ApiResponse.error("申诉不存在", 404);
            }
            return ApiResponse.success("获取申诉信息成功", appeal);
        } catch (Exception e) {
            return ApiResponse.error("获取申诉信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/student/{stId}")
    public ApiResponse<List<Appeal>> getAppealsByStudentId(@PathVariable String stId) {
        try {
            List<Appeal> appeals = appealService.getAppealsByStudentId(stId);
            return ApiResponse.success("获取学生申诉列表成功", appeals);
        } catch (Exception e) {
            return ApiResponse.error("获取学生申诉列表失败: " + e.getMessage());
        }
    }

    @PutMapping
    public ApiResponse<String> updateAppeal(@RequestBody Appeal appeal) {
        try {
            appealService.updateAppeal(appeal);
            return ApiResponse.success("申诉更新成功");
        } catch (Exception e) {
            return ApiResponse.error("更新申诉失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{apId}")
    public ApiResponse<String> deleteAppeal(@PathVariable String apId) {
        try {
            appealService.deleteAppeal(apId);
            return ApiResponse.success("申诉删除成功");
        } catch (Exception e) {
            return ApiResponse.error("删除申诉失败: " + e.getMessage());
        }
    }
}